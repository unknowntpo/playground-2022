# https://nightlies.apache.org/flink/flink-docs-master/docs/dev/python/table/intro_to_table_api/
import pytest
from pyflink.table import TableEnvironment, EnvironmentSettings
from pyflink.table.expressions import col

@pytest.fixture
def env_settings() -> EnvironmentSettings:
    return EnvironmentSettings.in_streaming_mode()

@pytest.fixture
def t_env() -> TableEnvironment:
    return TableEnvironment.create(env_settings)

def test_flink_table(t_env: TableEnvironment):
    t_env.execute_sql("""
        CREATE TABLE datagen (
            id INT,
            data STRING
        ) WITH (
            'connector' = 'datagen',
            'fields.id.kind' = 'sequence',
            'fields.id.start' = '1',
            'fields.id.end' = '10'
        )   
    """)

    t_env.execute_sql("""
        CREATE TABLE print (
            id INT,
            data STRING
        ) WITH (
            'connector' = 'print'
        )
    """)

    src_table = t_env.from_path("datagen")

    results = src_table.select(col("id") + 1, col("data")).execute().collect()
    result_list = sorted([row for row in results], key=lambda r: r[0])

    assert len(result_list) == 10
    assert result_list[0][0] == 2  # 1+1=2
    assert result_list[9][0] == 11  # 10+1=11