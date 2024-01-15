import * as cheerio from 'cheerio';
import * as fs from 'fs';


console.log("hello")

const html = fs.readFileSync('index.html', 'utf8');
const $ = cheerio.load(html)

console.log(`Title ${$('title').text()}`)