import { Database } from "duckdb-async";

async function simpleTest() {
  const db = await Database.create(":memory:");
  const rows = await db.all("select * from range(1,10)");

  console.log(rows);

  db.register_udf('mysum', 'integer', (n) => {
    let acc = 0;
    for (let i = 0; i < n; i++) {
      acc += 1
    }
    return acc;
  })

  const sumN = await db.all("select mysum(10)");
  console.log(sumN);
}

// simpleTest();

async function fetchFromHttp() {
  const db = await Database.create(":memory:");

  db.register_udf('css_selector', 'string', async (url) => {
    console.log(`fetching from url: ${url}`);
    const data = fetch(url)
      .then(res => res.text());
    return data;
  });

  const rows = await db.all(`select css_selector('https://example.com/') as html`);
  await db.wait();

  console.log(await String(rows[0].html));
}

fetchFromHttp();
