import * as cheerio from 'cheerio';
import * as fs from 'fs';


console.log("hello")

const html = fs.readFileSync('index.html', 'utf8');
const $ = cheerio.load(html)

console.log(`Title ${$('title').text()}`)

// const $trs = $('table.table > tbody > tr:not(:first-child)');

const $trs = $('table.table > tbody > tr');

// console.log(`rows ${$trs}`)

// $trs.each((_i, tr) => {
//   const $tr = $(tr);
//   const publishedDateText = $tr.find('td:nth-child(2)').text().trim();
//   const hasAbolishableTag = $tr.find('td:nth-child(3) > span').hasClass('label-fei');
//   const link = $tr.find('td:nth-child(3) > a').attr('href');
//   const title = $tr.find('td:nth-child(3) > a').text().trim();
//   const type = $tr.find('td:nth-child(4)').text().trim();

//   if (!link) {
//     throw new Error('無法取得函釋連結');
//   }

//   const rawItpRuleEntry: RawItpRuleEntry = {
//     publishedDateText,
//     hasAbolishableTag,
//     url: new URL(link, url).toString(),
//     extraInfos: [
//       {
//         sourceId: this.sourceId,
//         ['法規類別']: type,
//       },
//     ],
//     title,
//   };
//   rawItpRuleEntriesByUrl.push(rawItpRuleEntry);
// });

type Row = {
    ID: number,
    Date: string,
    Title: string,
    Type: string,
}

const rows: Array<Row> = [];

$trs.each((_i, tr) => {
    const $tr = $(tr)
    const id = Number($tr.find('td:nth-child(1)').text().trim().slice(0, -1));
    const date = $tr.find('td:nth-child(2)').text();
    const title = $tr.find('td:nth-child(3) > a').text().trim();
    const type = $tr.find('td:nth-child(4)').text().trim();

    // console.log($tr.text())
    rows.push({
        ID: id,
        Date: date,
        Title: title,
        Type: type,
    })
})

console.log(`rows: ${JSON.stringify(rows, null, '\t')}`)