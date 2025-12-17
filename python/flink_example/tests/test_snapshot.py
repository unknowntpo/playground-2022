# https://nightlies.apache.org/flink/flink-docs-master/docs/dev/python/table/intro_to_table_api/
from ast import Mult
from unittest import result
from numpy import tensordot
from pyflink.common import Row
from pyflink.table.udf import AggregateFunction, udf
import pytest
import pandas as pd
from pyflink.table import (
    ScalarFunction,
    TableEnvironment,
    EnvironmentSettings,
)
from pyflink.table.expressions import col


@pytest.fixture
def t_env() -> TableEnvironment:
    t_env = TableEnvironment.create(EnvironmentSettings.in_streaming_mode())
    t_env.get_config().set("parallelism.default", "1")
    return t_env


def test_flink_table(t_env: TableEnvironment):
    t_env.execute_sql(
        """
        CREATE TABLE datagen (
            id INT,
            data STRING
        ) WITH (
            'connector' = 'datagen',
            'fields.id.kind' = 'sequence',
            'fields.id.start' = '1',
            'fields.id.end' = '10'
        )   
    """
    )

    t_env.execute_sql(
        """
        CREATE TABLE print (
            id INT,
            data STRING
        ) WITH (
            'connector' = 'print'
        )
    """
    )

    src_table = t_env.from_path("datagen")

    results = src_table.select(col("id") + 1, col("data")).execute().collect()
    result_list = sorted([row for row in results], key=lambda r: r[0])

    assert len(result_list) == 10
    assert result_list[0][0] == 2  # 1+1=2
    assert result_list[9][0] == 11  # 10+1=11


# def test_flink_table_from_dataframe(t_env: TableEnvironment):
#     # from dataframe
#     df = t_env.from_pandas(
#         pd.DataFrame(
#             {
#                 "id": [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
#                 "price": [100, 200, 300, 400, 500, 600, 700, 800, 900, 1000],
#             }
#         )
#     )

#     # sum prices from dataframe
#     results = df.select(sum_col(col("price"))).execute().collect()
#     assert results[0][0] == 5500


def test_pandas_source_to_sink(t_env: TableEnvironment):
    """
    Test creating a source from pandas DataFrame, writing to a sink,
    and asserting the data is correct.
    """
    # Step 1: Create source from pandas DataFrame
    source_df = pd.DataFrame(
        {
            "id": [1, 2, 3, 4, 5],
            "name": ["Alice", "Bob", "Charlie", "David", "Eve"],
            "age": [25, 30, 35, 40, 45],
            "salary": [50000, 60000, 70000, 80000, 90000],
        }
    )

    # Convert pandas DataFrame to Flink table (source)
    source_table = t_env.from_pandas(source_df)

    # Step 2: Create a sink table
    t_env.execute_sql(
        """
        CREATE TABLE print_sink (
            avg_salary DOUBLE,
            avg_age DOUBLE
        ) WITH (
            'connector' = 'print'
        )
    """
    )

    # Step 3: Calculate average salary and age
    result_table = source_table.select(
        col("salary").avg.alias("avg_salary"), col("age").avg.alias("avg_age")
    )

    # Step 4: Write to sink table
    result_table.execute_insert("print_sink").wait()

    # Step 5: Verify results using assert
    results = list(result_table.execute().collect())

    # In streaming mode, aggregation produces a changelog (stream of updates).
    # We verify the final result (the last element in the collected list).
    assert len(results) > 0
    final_row = results[-1]
    assert final_row[0] == 70000.0  # Avg Salary
    assert final_row[1] == 35.0  # Avg Age


class MultBy2(ScalarFunction):
    def eval(self, v):
        return v * 2


def test_custom_udf(t_env: TableEnvironment):
    # t_env.execute_sql(
    #     """
    # CREATE TABLE datagen (
    #     id INT,
    #     data STRING
    # ) WITH (
    #     'connector' = 'datagen',
    #     'fields.id.kind' = 'sequence',
    #     'fields.id.start' = '1',
    #     'fields.id.end' = '10'
    # )
    # """
    # )

    df = pd.DataFrame({"id": [1, 2, 3, 4], "data": [200, 400, 500, 600]})

    src = t_env.from_pandas(df)
    t_env.create_temporary_view("datagen", src)

    t_env.execute_sql(
        """
    CREATE TABLE print (
        id INT,
        data INT
    ) WITH (
        'connector' = 'print'
    ) 
"""
    )

    t_env.create_temporary_function("mult_by_2", udf(MultBy2(), result_type="INT"))

    results = (
        t_env.sql_query("SELECT id, mult_by_2(data) FROM datagen").execute().collect()
    )

    result_list = sorted([tuple(row) for row in results], key=lambda r: r[0])

    print(result_list)
    assert len(result_list) == 4
    assert result_list == [(1, 400), (2, 800), (3, 1000), (4, 1200)]
