import nodeHtmlToImage from 'node-html-to-image';
import { createHash } from 'node:crypto';
import fs from 'fs-extra';

async function main() {
  nodeHtmlToImage({
    html: '<h1>Hello</h1>',
    output: './hello.png'
  }).then(() => console.log('done'))
}

async function testHtmlFile() {
  // .html
  // https://law.banking.gov.tw/Chi/FLAW/FLAWDESC.aspx?lsid=FL006481&ldate=20180608
  const res = await fetch(`https://law.banking.gov.tw/Chi/FLAW/FLAWDESC.aspx?lsid=FL006481&ldate=20180608`);
  const html = await res.text();
  await nodeHtmlToImage({
    html: html,
    output: './html1.png'
  });

  const hash1 = createHash('md5');
  const hash1Str = hash1.update(await fs.readFile('./html1.png')).digest('hex');

  const res2 = await fetch(`https://law.banking.gov.tw/Chi/FLAW/FLAWDESC.aspx?lsid=FL006481&ldate=20180608`);
  const html2 = await res2.text();
  await nodeHtmlToImage({
    html: html2,
    output: './html2.png'
  });

  const hash2 = createHash('md5');
  const hash2Str = hash2.update(await fs.readFile('./html2.png')).digest('hex');

  console.log(`hash1: ${hash1Str} == hash2: ${hash2Str} ? ${hash1Str == hash2Str}`)

}

// main()
testHtmlFile()

