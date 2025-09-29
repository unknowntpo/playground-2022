import os
import sys
from datetime import datetime, timedelta

sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from airflow import DAG
from airflow.operators.python import PythonOperator

from services.github_service import GitHubTrendingService


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

        output_dir = "/tmp/github_trending"
        logger.info(f"Creating output directory: {output_dir}")
        os.makedirs(output_dir, exist_ok=True)

        output_file = f"{output_dir}/trending_{execution_date}.json"
        logger.info(f"Saving to file: {output_file}")
        github_service.save_to_json(repositories, output_file)

        logger.info(f"Successfully saved {len(repositories)} repositories to {output_file}")
        print(f"✅ Fetched {len(repositories)} trending repositories")
        print(f"✅ Saved to: {output_file}")

        result = {
            'repositories_count': len(repositories),
            'output_file': output_file,
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
        logger.info(f"File location: {result['output_file']}")

        print(f"✅ Processing {result['repositories_count']} repositories")
        print(f"✅ File: {result['output_file']}")
        print(f"✅ Execution date: {result['execution_date']}")

        # Verify file exists
        if os.path.exists(result['output_file']):
            file_size = os.path.getsize(result['output_file'])
            logger.info(f"Output file exists and is {file_size} bytes")
            print(f"✅ File verified: {file_size} bytes")
        else:
            logger.warning(f"Output file does not exist: {result['output_file']}")
            print(f"⚠️  File not found: {result['output_file']}")

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