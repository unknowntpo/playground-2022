import os
import sys
from datetime import datetime, timedelta

sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from airflow import DAG
from airflow.operators.python import PythonOperator

from services.github_service import GitHubTrendingService
from services.s3_service import S3StorageService


def fetch_github_trending(**context):
    import logging
    logger = logging.getLogger(__name__)

    logger.info("Starting GitHub trending repositories fetch")
    logger.info(f"Context: {context}")

    try:
        if 'execution_date' in context and context['execution_date']:
            execution_date = context['execution_date'].strftime('%Y-%m-%d')
        else:
            execution_date = datetime.now().strftime('%Y-%m-%d')

        logger.info(f"Execution date: {execution_date}")

        logger.info("Initializing GitHub service")
        github_service = GitHubTrendingService()

        logger.info("Fetching trending repositories")
        repositories = github_service.fetch_trending_repositories(
            since="daily",
            limit=10
        )
        logger.info(f"Successfully fetched {len(repositories)} repositories")

        # Save locally first (fallback)
        output_dir = "/tmp/github_trending"
        logger.info(f"Creating output directory: {output_dir}")
        os.makedirs(output_dir, exist_ok=True)

        local_file = f"{output_dir}/trending_{execution_date}.json"
        logger.info(f"Saving to local file: {local_file}")
        github_service.save_to_json(repositories, local_file)

        # Save to S3/MinIO
        logger.info("Initializing S3 storage service")
        s3_service = S3StorageService(
            endpoint="minio:9000",  # Use docker service name
            access_key="minioadmin",
            secret_key="minioadmin123",
            bucket_name="github-trending",
            secure=False
        )

        # Prepare data for S3
        data_payload = {
            'repositories': repositories,
            'count': len(repositories),
            'execution_date': execution_date,
            'crawled_at': datetime.utcnow().isoformat(),
            'source': 'github_trending_crawler'
        }

        # Upload to S3
        s3_object_name = f"trending/{execution_date}/repositories.json"
        logger.info(f"Uploading to S3: {s3_object_name}")

        s3_path = s3_service.upload_json_data(
            data=data_payload,
            object_name=s3_object_name,
            metadata={
                'dag_id': 'github_trending_crawler',
                'execution_date': execution_date,
                'repository_count': str(len(repositories))
            }
        )

        logger.info(f"Successfully saved {len(repositories)} repositories to S3: {s3_path}")
        print(f"✅ Fetched {len(repositories)} trending repositories")
        print(f"✅ Saved locally: {local_file}")
        print(f"✅ Saved to S3: {s3_path}")

        result = {
            'repositories_count': len(repositories),
            'local_file': local_file,
            's3_path': s3_path,
            's3_object_name': s3_object_name,
            'execution_date': execution_date
        }
        logger.info(f"Returning result: {result}")
        return result

    except Exception as e:
        logger.error(f"Error in fetch_github_trending: {str(e)}")
        logger.exception("Full exception traceback:")
        raise


def process_repositories(**context):
    import logging
    logger = logging.getLogger(__name__)

    logger.info("Starting process_repositories task")
    logger.info(f"Context: {context}")

    try:
        task_instance = context['task_instance']
        logger.info("Getting result from fetch_trending task via XCom")

        result = task_instance.xcom_pull(task_ids='fetch_trending')
        logger.info(f"XCom result: {result}")

        if result is None:
            logger.error("No result received from fetch_trending task")
            raise ValueError("No data received from fetch_trending task")

        logger.info(f"Processing {result['repositories_count']} repositories")
        logger.info(f"Local file: {result['local_file']}")
        logger.info(f"S3 path: {result['s3_path']}")

        print(f"✅ Processing {result['repositories_count']} repositories")
        print(f"✅ Local file: {result['local_file']}")
        print(f"✅ S3 path: {result['s3_path']}")
        print(f"✅ Execution date: {result['execution_date']}")

        # Verify local file exists
        if os.path.exists(result['local_file']):
            file_size = os.path.getsize(result['local_file'])
            logger.info(f"Local file exists and is {file_size} bytes")
            print(f"✅ Local file verified: {file_size} bytes")
        else:
            logger.warning(f"Local file does not exist: {result['local_file']}")
            print(f"⚠️  Local file not found: {result['local_file']}")

        # Verify S3 object exists
        try:
            s3_service = S3StorageService(
                endpoint="minio:9000",
                access_key="minioadmin",
                secret_key="minioadmin123",
                bucket_name="github-trending",
                secure=False
            )

            # Try to list the object to verify it exists
            objects = s3_service.list_objects(prefix=f"trending/{result['execution_date']}")
            if result['s3_object_name'] in objects:
                logger.info(f"S3 object verified: {result['s3_object_name']}")
                print(f"✅ S3 object verified: {result['s3_object_name']}")
            else:
                logger.warning(f"S3 object not found: {result['s3_object_name']}")
                print(f"⚠️  S3 object not found: {result['s3_object_name']}")

        except Exception as e:
            logger.error(f"Error verifying S3 object: {e}")
            print(f"⚠️  Error verifying S3 object: {e}")

        logger.info("process_repositories completed successfully")
        return result

    except Exception as e:
        logger.error(f"Error in process_repositories: {str(e)}")
        logger.exception("Full exception traceback:")
        raise


default_args = {
    'owner': 'airflow',
    'depends_on_past': False,
    'start_date': datetime(2024, 1, 1),
    'email_on_failure': False,
    'email_on_retry': False,
    'retries': 1,
    'retry_delay': timedelta(minutes=5),
}

dag = DAG(
    'github_trending_crawler',
    default_args=default_args,
    description='Fetch GitHub trending repositories daily',
    schedule='@daily',
    catchup=False,
    tags=['github', 'trending', 'crawler'],
)

fetch_task = PythonOperator(
    task_id='fetch_trending',
    python_callable=fetch_github_trending,
    dag=dag,
)

process_task = PythonOperator(
    task_id='process_repositories',
    python_callable=process_repositories,
    dag=dag,
)

fetch_task >> process_task