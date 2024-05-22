import fs from 'fs';
import stream from 'stream';
async function readAndWrite() {
  const data = await fetch('https://google.com');

  const writeStream = fs.createWriteStream('/tmp/body');
  if (!data.body) {
    throw new Error(`no body`)
  }
  const [fileReadStream, stdoutReadStream] = data.body.tee();
  await Promise.all([
    stream.Readable.fromWeb(fileReadStream).pipe(writeStream),
    stream.Readable.fromWeb(stdoutReadStream).pipe(process.stdout)
  ]);
}

export { readAndWrite };
