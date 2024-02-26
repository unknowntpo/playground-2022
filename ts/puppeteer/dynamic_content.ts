import puppeteer from 'puppeteer';
import fs from 'node:fs';


async function crawAndGetRenderedHtml(url: string): Promise<string> {
    const browser = await puppeteer.launch({ headless: false }); // Launch in non-headless mode for observation
    const page = await browser.newPage();
    await page.goto(url);

    // Wait for the expected structure to be present
    await page.waitForSelector('div.lawLinkList a', { visible: true, timeout: 5000 });

    // // Check if iframes are a factor
    // const frames = page.frames();
    // for (const frame of frames) {
    //     console.log("processing frame");
    //     try {
    //         await frame.waitForSelector('li a');
    //         return await frame.content();
    //     } catch (e) {
    //         // Continue to the next frame or handle iframe content extraction differently
    //         console.error(e);
    //     }
    // }
    console.log("BBB before page.content")

    const content = await page.content();
    await browser.close()
    // If no match in iframes, return content of the main page
    return content
}
async function main() {
    // const targetUrl = 'https://cons.judicial.gov.tw/docdata.aspx?fid=38&id=346741';
    const targetUrl = 'https://cons.judicial.gov.tw/docdata.aspx?fid=38&id=309577';
    // crawlAndExtractLinks(targetUrl)
    //     .then(links => console.log(links))
    //     .catch(error => console.error(error));

    try {
        const content = await crawAndGetRenderedHtml(targetUrl)
        console.log("got content")
        fs.writeFileSync('rendered.html', content);
        console.log("Everything is Done")
    } catch (e) {
        console.error(e)
    }
}

main()