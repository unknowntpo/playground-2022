"use strict";

// src/index.ts
var import_duckdb_async = require("duckdb-async");
async function fetchFromHttp() {
  const db = await import_duckdb_async.Database.create(":memory:");
  db.register_udf("css_selector", "string", (url) => {
    console.log(`fetching from url: ${url}`);
    const data = fetch(url).then((res) => res.text());
    return data;
  });
  const rows = await db.all(`select css_selector('https://example.com/') as html`);
  await db.wait();
  console.log(rows);
}
fetchFromHttp();
