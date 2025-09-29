import os
import sys
from datetime import datetime, timedelta

sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from airflow import DAG
from airflow.operators.python import PythonOperator

from services.github_service import GitHubTrendingService


def fetch_github_trending(**context):
    execution_date = context['execution_date'].strftime('%Y-%m-%d')

    github_service = GitHubTrendingService()

    repositories = github_service.fetch_trending_repositories(
        since="daily",
        limit=10
    )

    output_dir = "/tmp/github_trending"
    os.makedirs(output_dir, exist_ok=True)

    output_file = f"{output_dir}/trending_{execution_date}.json"
    github_service.save_to_json(repositories, output_file)

    print(f"Fetched {len(repositories)} trending repositories")
    print(f"Saved to: {output_file}")

    return {
        'repositories_count': len(repositories),
        'output_file': output_file,
        'execution_date': execution_date
    }


def process_repositories(**context):
    task_instance = context['task_instance']
    result = task_instance.xcom_pull(task_ids='fetch_trending')

    print(f"Processing {result['repositories_count']} repositories")
    print(f"File: {result['output_file']}")

    return result


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