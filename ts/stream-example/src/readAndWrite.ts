import fs from 'node:fs';
import { PassThrough, Readable, Transform } from 'node:stream';
import { pipeline, finished } from 'node:stream/promises';
import readline from 'node:readline';
import { UserGenerator } from './user.js';

import path from 'path';
import { fileURLToPath } from 'url';
const __filename = fileURLToPath(import.meta.url); // get the resolved path to the file
const __dirname = path.dirname(__filename); // get the name of the directory

async function prepareData() {
	const stream = fs.createWriteStream(`${__dirname}/../data/input.json`, { flags: 'w' })

	const userGenerator = new UserGenerator(5000);

	try {
		for (const user of userGenerator) {
			console.log("got user")
			stream.write(JSON.stringify(user) + "\n")
		}
	} catch (err) {
		console.log(err)
	} finally {
		stream.end();

		// If we don't wait for finished, in main function, fsReadStream can not read data.
		await finished(stream);
	}
}

async function main() {
	await prepareData();

	const fsReadStream = fs.createReadStream(`${__dirname}/../data/input.json`, { encoding: 'utf-8' })
	const fsWriteStream = fs.createWriteStream(`${__dirname}/../data/output.json`, { encoding: 'utf-8' })

	fsReadStream.on('data', (data) => {
		console.log(`readStream got data: ${data.toString()}`);
	})

	fsWriteStream.on('data', (data) => {
		console.log(`writeStream got data: ${data.toString()}`);
	})

	fsReadStream.once('readable', () => {
		console.log(`readStream readable`);
	})

	fsReadStream.on('end', () => {
		// passThrough.end();
		console.log(`readStream ended`);
	})

	fsReadStream.on('error', function (err) {
		console.log(err.stack);
	});

	const readable = Readable.from(fsReadStream, { objectMode: true, highWaterMark: 1 });

	const tf = new Transform({
		transform(chunk, _encoding, callback) {
			let str = chunk.toString();
			console.log(`Transform got data: ${str}`)
			this.push(str);
			callback();
		},
	});

	const ps = new PassThrough();

	readable.on('data', (chunk) => {
		console.log(`readable: ${chunk.toString()}`)
	})

	await pipeline(fsReadStream, tf, fsWriteStream);

	await finished(fsReadStream)
	await finished(fsWriteStream)
}

main()