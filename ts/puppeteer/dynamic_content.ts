import puppeteer from 'puppeteer';
import fs from 'node:fs';
import { resolve } from 'node:path';


async function crawAndGetRenderedHtml(url: string): Promise<string> {
    const browser = await puppeteer.launch({ headless: false }); // Launch in non-headless mode for observation
    const page = await browser.newPage();
    await page.goto(url);

    // Attempt to jump to the bottom of the page
    await page.evaluate(() => {
        window.scrollTo(0, document.body.scrollHeight);
    });

    // Wait for the expected structure to be present
    await page.waitForSelector('*', { timeout: 5000 })

    let content = '';

    await page.evaluate(() => {
        content = document.querySelectorAll('*').values;
    })

    const links = await page.evaluate(() => {
        const linkElements = document.querySelectorAll('div.lawLinkList a')
        return Array.from(linkElements).map(
            ele => {
                if (ele instanceof HTMLAnchorElement) { // Check
                    return {
                        title: ele.getAttribute('title'),
                        href: ele.getAttribute('href') // Safe to
                    };
                } else {
                    // Optional: Handle cases when the element is
                    return null; // Or an alternative format
                }
            }
            // return Array.from(linkElements).map(link => link);
        )
    });
    console.log(links)

    const data = await page.evaluate(() => document.querySelector('*')!.outerHTML);


    await sleep(1000);

    // Capture the HTML of the entire page
    // const content = await page.evaluate(() => document.documentElement.outerHTML);
    // await page.screenshot({ path: 'example.png', fullPage: true });

    // await sleep(30000);
    // #frame_declare_judgment_video > li > a > p
    // const content = await page.content();
    await browser.close()
    // If no match in iframes, return content of the main page
    return content
}

function sleep(ms: number) {
    return new Promise(resolve => setTimeout(resolve, ms));
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

    // try {
    //     await pageFromHtml()
    // } catch (e) {
    //     console.error(e)
    // }
}

async function pageFromHtml() {
    // Launch a new browser instance
    const browser = await puppeteer.launch();
    // Create a new page (tab)
    const page = await browser.newPage();

    const htmlContent = fs.readFileSync('rendered.html', 'utf8');

    // Set the content of the page to your HTML string
    await page.setContent(htmlContent);

    // Take a screenshot of the page
    await page.screenshot({ path: 'screenshot.png' });


    // Wait for the expected structure to be present
    await page.waitForSelector('*', { timeout: 5000 })

    const links = await page.evaluate(() => {
        const linkElements = document.querySelectorAll('div.lawLinkList a')
        return Array.from(linkElements).map(
            ele => {
                if (ele instanceof HTMLAnchorElement) { // Check
                    return {
                        title: ele.getAttribute('title'),
                        href: ele.getAttribute('href') // Safe to
                    };
                } else {
                    // Optional: Handle cases when the element is
                    return null; // Or an alternative format
                }
            }
            // return Array.from(linkElements).map(link => link);
        )
    });
    console.log(links)

    // Close the browser
    await browser.close();
}

main()