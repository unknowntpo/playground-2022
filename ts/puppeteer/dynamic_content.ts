import puppeteer from 'puppeteer';

async function crawlAndExtractLinks(url: string): Promise<string[]> {
    const browser = await puppeteer.launch();
    const page = await browser.newPage();
    await page.goto(url);

    // Wait for JS-generated content (adjust selectors and timeout as needed)
    await page.waitForSelector('.lawLinkList');

    const links = await page.evaluate(() => {
        const linkElements = document.querySelectorAll('div.lawLinkList > a');
        return Array.from(linkElements).map(
            ele => {
                if (ele instanceof HTMLAnchorElement) { // Check if it's an anchor element
                    return {
                        title: ele.getAttribute('title'),
                        href: ele.getAttribute('href') // Safe to access now
                    };
                } else {
                    // Optional: Handle cases when the element is not an anchor
                    return null; // Or an alternative format
                }
            }
            // return Array.from(linkElements).map(link => link);
        )
    });
    // <div class="lawLinkList" id="frame_oral_debate_video"><li><a title="111年度憲民字第4096號─擴大利得沒收案112年11月27日言詞辯論" href="https://cons.judicial.gov.tw/docdata.aspx?fid=89&amp;id=351122&amp;cat=1" target="_blank">
    // <i class="fas fa-link"></i>
    // <p>111年度憲民字第4096號─擴大利得沒收案112年11月27日言詞辯論</p>
    // </a></li></div>
    await browser.close();
    return links;
}

const targetUrl = 'https://cons.judicial.gov.tw/docdata.aspx?fid=38&id=346741';
crawlAndExtractLinks(targetUrl)
    .then(links => console.log(links))
    .catch(error => console.error(error));
