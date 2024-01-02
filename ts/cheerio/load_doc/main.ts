import * as cheerio from 'cheerio';
import axios from 'axios';

// https://cheerio.js.org/docs/basics/loading
const parse = async () => {
    const response = await axios.get("https://example.com");
    const html = response.data;

    // cheerio.fromURL is missing, so use axios instead.
    // https://github.com/cheeriojs/cheerio/issues/3391#issuecomment-1737780743
    const $ = cheerio.load(html)
    console.log($('title').text());
}
parse()