import os.path
import pathlib

import polars as pl

def test_polars():
    df = pl.DataFrame({
        "col1": [1, 3, 4],
        "col2": [5,6,7],
    })
    res = df.select("col1").filter(pl.col("col1") % 2 == 1).sum()
    assert res.item() == 4

def test_polars_parquet():
    df = pl.DataFrame({
        "col1": [1, 3, 4],
        "col2": [5,6,7],
    })

    path = "/tmp/test0031.parquet"

    filepath = pathlib.Path(path)

    if os.path.exists(filepath):
        os.remove(filepath)

    df.write_parquet(path)

    assert filepath.is_file()

    df2 = pl.read_parquet(filepath)
    print(df2)

    assert 1 == 1

