import os
import tempfile

import polars as pl


def main():
    print('Hello from 1brc-parquet!')

    temp_parquet_file = create_temp_file()

    sample_data = make_sample_data()

    df = pl.DataFrame(sample_data)

    df.write_parquet(temp_parquet_file)

    df_subset = pl.read_parquet(temp_parquet_file, columns=['name', 'age'])

    print(df_subset.describe())


def make_sample_data() -> dict:
    sample_data = {
        'id': [1, 2, 3, 4, 5],
        'name': ['Alice', 'Bob', 'Charlie', 'David', 'Allen'],
        'age': [25, 3, 22, 36, 10],
        'salary': [33000.0, 22000.0, 35000.0, 23435.0, 23434.0],
    }
    return sample_data


def create_temp_file() -> str:
    fd, temp_parquet_file = tempfile.mkstemp(suffix='.parquet')
    os.close(fd)
    return temp_parquet_file


if __name__ == '__main__':
    main()
