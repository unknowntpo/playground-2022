use duckdb::{Connection, Result};

#[derive(Debug)]
#[warn(dead_code)]
struct Entry {
    tax_sn: i32,
    tax_act: String,
    tax_ver: String,
    chapter: String,
    article: String,
    cate: String,
    title: String,
    content: String,
}

fn main() -> Result<(), duckdb::Error> {
    let conn = Connection::open_in_memory()?;

    conn.execute("CREATE TABLE response AS SELECT * FROM 'data.json'", [])?;

    let mut stat = conn.prepare("SELECT * FROM response LIMIT 3")?;
    let entry_iter = stat.query_map([], |row| {
        Ok(Entry {
            tax_sn: row.get(0)?,
            tax_act: row.get(1)?,
            tax_ver: row.get(2)?,
            chapter: row.get(3)?,
            article: row.get(4)?,
            cate: row.get(5)?,
            title: row.get(6)?,
            content: row.get(7)?,
        })
    })?;

    for entry in entry_iter {
        println!("entry: {:?}", entry.unwrap());
    }

    Ok(())
}
