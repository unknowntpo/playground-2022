import json
import logging
from datetime import datetime
from typing import Dict, List, Optional

import boto3
from botocore.exceptions import ClientError, NoCredentialsError
from minio import Minio
from minio.error import S3Error

logger = logging.getLogger(__name__)


class S3StorageService:
    def __init__(
        self,
        endpoint: str = "localhost:9000",
        access_key: str = "minioadmin",
        secret_key: str = "minioadmin123",
        bucket_name: str = "github-trending",
        secure: bool = False,
        use_minio_client: bool = True
    ):
        self.endpoint = endpoint
        self.access_key = access_key
        self.secret_key = secret_key
        self.bucket_name = bucket_name
        self.secure = secure
        self.use_minio_client = use_minio_client

        if use_minio_client:
            self._init_minio_client()
        else:
            self._init_boto3_client()

    def _init_minio_client(self):
        try:
            self.client = Minio(
                self.endpoint,
                access_key=self.access_key,
                secret_key=self.secret_key,
                secure=self.secure,
            )
            logger.info(f"MinIO client initialized for endpoint: {self.endpoint}")
            self._ensure_bucket_exists()
        except Exception as e:
            logger.error(f"Failed to initialize MinIO client: {e}")
            raise

    def _init_boto3_client(self):
        try:
            self.client = boto3.client(
                's3',
                endpoint_url=f"{'https' if self.secure else 'http'}://{self.endpoint}",
                aws_access_key_id=self.access_key,
                aws_secret_access_key=self.secret_key,
                region_name='us-east-1'  # MinIO doesn't care about region
            )
            logger.info(f"Boto3 S3 client initialized for endpoint: {self.endpoint}")
            self._ensure_bucket_exists()
        except Exception as e:
            logger.error(f"Failed to initialize Boto3 client: {e}")
            raise

    def _ensure_bucket_exists(self):
        try:
            if self.use_minio_client:
                if not self.client.bucket_exists(self.bucket_name):
                    self.client.make_bucket(self.bucket_name)
                    logger.info(f"Created bucket: {self.bucket_name}")
                else:
                    logger.info(f"Bucket {self.bucket_name} already exists")
            else:
                # Using boto3
                self.client.head_bucket(Bucket=self.bucket_name)
                logger.info(f"Bucket {self.bucket_name} exists")
        except ClientError as e:
            if e.response['Error']['Code'] == '404':
                self.client.create_bucket(Bucket=self.bucket_name)
                logger.info(f"Created bucket: {self.bucket_name}")
            else:
                logger.error(f"Error checking/creating bucket: {e}")
                raise
        except S3Error as e:
            logger.error(f"MinIO error with bucket operations: {e}")
            raise
        except Exception as e:
            logger.error(f"Unexpected error with bucket operations: {e}")
            raise

    def upload_json_data(
        self,
        data: Dict,
        object_name: str,
        metadata: Optional[Dict] = None
    ) -> str:
        try:
            # Convert data to JSON string
            json_data = json.dumps(data, indent=2, ensure_ascii=False)
            json_bytes = json_data.encode('utf-8')

            # Prepare metadata
            upload_metadata = {
                'Content-Type': 'application/json',
                'uploaded_at': datetime.utcnow().isoformat(),
                'data_size': str(len(json_bytes))
            }
            if metadata:
                upload_metadata.update(metadata)

            logger.info(f"Uploading {len(json_bytes)} bytes to s3://{self.bucket_name}/{object_name}")

            if self.use_minio_client:
                # Using MinIO client
                from io import BytesIO
                data_stream = BytesIO(json_bytes)

                self.client.put_object(
                    self.bucket_name,
                    object_name,
                    data_stream,
                    length=len(json_bytes),
                    content_type='application/json',
                    metadata=upload_metadata
                )
            else:
                # Using boto3
                self.client.put_object(
                    Bucket=self.bucket_name,
                    Key=object_name,
                    Body=json_bytes,
                    ContentType='application/json',
                    Metadata=upload_metadata
                )

            s3_path = f"s3://{self.bucket_name}/{object_name}"
            logger.info(f"Successfully uploaded to {s3_path}")
            return s3_path

        except Exception as e:
            logger.error(f"Failed to upload data to S3: {e}")
            raise

    def download_json_data(self, object_name: str) -> Dict:
        try:
            logger.info(f"Downloading s3://{self.bucket_name}/{object_name}")

            if self.use_minio_client:
                # Using MinIO client
                response = self.client.get_object(self.bucket_name, object_name)
                data = response.read()
                response.close()
                response.release_conn()
            else:
                # Using boto3
                response = self.client.get_object(Bucket=self.bucket_name, Key=object_name)
                data = response['Body'].read()

            # Parse JSON
            json_data = json.loads(data.decode('utf-8'))
            logger.info(f"Successfully downloaded and parsed JSON data")
            return json_data

        except Exception as e:
            logger.error(f"Failed to download data from S3: {e}")
            raise

    def list_objects(self, prefix: str = "") -> List[str]:
        try:
            objects = []

            if self.use_minio_client:
                # Using MinIO client
                for obj in self.client.list_objects(self.bucket_name, prefix=prefix):
                    objects.append(obj.object_name)
            else:
                # Using boto3
                paginator = self.client.get_paginator('list_objects_v2')
                for page in paginator.paginate(Bucket=self.bucket_name, Prefix=prefix):
                    if 'Contents' in page:
                        for obj in page['Contents']:
                            objects.append(obj['Key'])

            logger.info(f"Found {len(objects)} objects with prefix '{prefix}'")
            return objects

        except Exception as e:
            logger.error(f"Failed to list objects: {e}")
            raise

    def delete_object(self, object_name: str) -> bool:
        try:
            if self.use_minio_client:
                self.client.remove_object(self.bucket_name, object_name)
            else:
                self.client.delete_object(Bucket=self.bucket_name, Key=object_name)

            logger.info(f"Successfully deleted s3://{self.bucket_name}/{object_name}")
            return True

        except Exception as e:
            logger.error(f"Failed to delete object: {e}")
            return False

    def get_object_url(self, object_name: str, expires: int = 3600) -> str:
        try:
            if self.use_minio_client:
                url = self.client.presigned_get_object(
                    self.bucket_name,
                    object_name,
                    expires=timedelta(seconds=expires)
                )
            else:
                url = self.client.generate_presigned_url(
                    'get_object',
                    Params={'Bucket': self.bucket_name, 'Key': object_name},
                    ExpiresIn=expires
                )

            logger.info(f"Generated presigned URL for {object_name}")
            return url

        except Exception as e:
            logger.error(f"Failed to generate presigned URL: {e}")
            raise