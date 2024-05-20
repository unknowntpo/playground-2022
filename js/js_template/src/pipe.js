import { Readable, pipeline } from 'stream';
import fs from 'fs';
import zlib from 'zlib';


async function readAndWritePipe() {
	// const input = Readable.from(['hello world']);
	// const output = fs.createWriteStream('/tmp/test');
	// const readStream = fs.createReadStream('/tmp/test');

	await pipeline(
		fs.createReadStream('archive.tar'),
		zlib.createGzip(),
		fs.createWriteStream('/tmp/test'),
	)
}

readAndWritePipe();

export { readAndWritePipe };