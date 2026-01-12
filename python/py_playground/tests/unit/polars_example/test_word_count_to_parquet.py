import polars as pl

def test_polars():
    df = pl.DataFrame({
        "col1": [1, 3, 4],
        "col2": [5,6,7],
    })
    res = df.select("col1").filter(pl.col("col1") % 2 == 1).sum()
    assert res.item() == 4
