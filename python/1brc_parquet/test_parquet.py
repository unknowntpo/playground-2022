import os
import tempfile

import polars as pl
import pytest


@pytest.fixture
def sample_data():
    """Create sample data for testing."""
    return {
        'id': [1, 2, 3, 4, 5],
        'name': ['Alice', 'Bob', 'Charlie', 'David', 'Eve'],
        'age': [25, 30, 35, 40, 45],
        'salary': [50000.0, 60000.0, 70000.0, 80000.0, 90000.0],
    }


@pytest.fixture
def temp_parquet_file():
    """Create a temporary parquet file path."""
    fd, path = tempfile.mkstemp(suffix='.parquet')
    os.close(fd)
    yield path
    if os.path.exists(path):
        os.remove(path)


def test_write_parquet(sample_data, temp_parquet_file):
    """Test writing data to a parquet file."""
    df = pl.DataFrame(sample_data)
    df.write_parquet(temp_parquet_file)

    assert os.path.exists(temp_parquet_file)
    assert os.path.getsize(temp_parquet_file) > 0


def test_read_parquet(sample_data, temp_parquet_file):
    """Test reading data from a parquet file."""
    df_write = pl.DataFrame(sample_data)
    df_write.write_parquet(temp_parquet_file)

    df_read = pl.read_parquet(temp_parquet_file)

    assert df_write.equals(df_read)


def test_parquet_schema(sample_data, temp_parquet_file):
    """Test reading parquet schema."""
    df = pl.DataFrame(sample_data)
    df.write_parquet(temp_parquet_file)

    df_read = pl.read_parquet(temp_parquet_file)
    schema = df_read.schema

    assert len(schema) == len(sample_data)
    assert 'name' in schema
    assert 'age' in schema


def test_dataframe_info(sample_data, temp_parquet_file):
    """Test getting DataFrame info."""
    df = pl.DataFrame(sample_data)
    df.write_parquet(temp_parquet_file)

    df_read = pl.read_parquet(temp_parquet_file)

    assert df_read.shape == (5, 4)
    assert df_read.height == len(sample_data['id'])
    assert df_read.width == len(sample_data)


def test_parquet_filter(sample_data, temp_parquet_file):
    """Test reading parquet with filter."""
    df = pl.DataFrame(sample_data)
    df.write_parquet(temp_parquet_file)

    df_filtered = pl.read_parquet(temp_parquet_file).filter(pl.col('age') > 30)

    assert df_filtered.height == 3
    assert all(df_filtered['age'] > 30)


def test_parquet_column_selection(sample_data, temp_parquet_file):
    """Test reading specific columns from parquet."""
    df = pl.DataFrame(sample_data)
    df.write_parquet(temp_parquet_file)

    df_subset = pl.read_parquet(temp_parquet_file, columns=['name', 'age'])

    assert df_subset.columns == ['name', 'age']
    assert df_subset.height == len(sample_data['id'])


def test_parquet_compression(sample_data, temp_parquet_file):
    """Test writing parquet with compression."""
    df = pl.DataFrame(sample_data)
    df.write_parquet(temp_parquet_file, compression='snappy')

    assert os.path.exists(temp_parquet_file)
    # Verify data integrity after compression
    df_read = pl.read_parquet(temp_parquet_file)
    assert df.equals(df_read)


def test_dataframe_operations(sample_data):
    """Test basic Polars DataFrame operations."""
    df = pl.DataFrame(sample_data)

    assert df.height == 5
    assert df.width == 4
    assert 'name' in df.columns

    # Test column access
    age_series = df['age']
    assert len(age_series) == 5
    assert age_series[0] == 25

    # Test describe
    stats = df.describe()
    assert stats is not None
    assert stats.height > 0


def test_dataframe_aggregations(sample_data):
    """Test Polars aggregation operations."""
    df = pl.DataFrame(sample_data)

    # Test mean
    mean_age = df['age'].mean()
    assert mean_age == 35.0

    # Test max
    max_salary = df['salary'].max()
    assert max_salary == 90000.0

    # Test min
    min_age = df['age'].min()
    assert min_age == 25


def test_dataframe_transformations(sample_data):
    """Test Polars transformation operations."""
    df = pl.DataFrame(sample_data)

    # Test select
    df_selected = df.select(['name', 'age'])
    assert df_selected.columns == ['name', 'age']

    # Test with_columns
    df_with_bonus = df.with_columns((pl.col('salary') * 1.1).alias('salary_with_bonus'))
    assert 'salary_with_bonus' in df_with_bonus.columns
    assert df_with_bonus.height == 5
