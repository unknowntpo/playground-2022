import nodeHtmlToImage from 'node-html-to-image';

async function main() {
    nodeHtmlToImage({
        html: '<h1>Hello</h1>',
        output: './hello.png'
    }).then(() => console.log('done'))
}

main()

