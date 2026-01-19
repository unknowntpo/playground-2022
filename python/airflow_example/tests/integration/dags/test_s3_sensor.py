import boto3
import pytest
from types_boto3_s3 import S3Client


@pytest.fixture
def s3():
    s3: S3Client = boto3.client(
        "s3",
        endpoint_url=ENDPOINT,
        aws_access_key_id="test",
        aws_secret_access_key="test",
    )

    try:
        s3.create_bucket(
            Bucket=BUCKET,
            CreateBucketConfiguration={"LocationConstraint": "ap-northeast-1"},
        )
    except s3.exceptions.BucketAlreadyOwnedByYou:
        pass

    # clear all objects
    resp = s3.list_objects_v2(Bucket=BUCKET)
    if "Contents" in resp:
        for obj in resp["Contents"]:
            s3.delete_object(Bucket=BUCKET, Key=obj["Key"])

    return s3


BUCKET = "test-bucket"
KEY = "data/input.json"
ENDPOINT = "http://localhost:4566"


def test_s3_sensor(s3: S3Client):
    # create bucket & upload file
    body = b'{"msg": "hello"}'
    s3.put_object(Bucket=BUCKET, Key=KEY, Body=body)
    obj = s3.get_object(Bucket=BUCKET, Key=KEY)
    content = obj["Body"].read()
    assert content == body
