import fs from 'node:fs';
import { PassThrough, Readable, Transform } from 'node:stream';
import { pipeline, finished } from 'node:stream/promises';
import readline from 'node:readline';

interface User {
	Id: number;
	name: string;
	email: string;
	age: number;
}

function randString(length: number) {
	let result = '';
	const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
	const charactersLength = characters.length;
	let counter = 0;
	while (counter < length) {
		result += characters.charAt(Math.floor(Math.random() * charactersLength));
		counter += 1;
	}
	return result;
}

class UserGenerator {
	currentId: number;
	count: number;
	constructor(count = 10) {
		this.count = count;
		this.currentId = 0;
	}

	*[Symbol.iterator]() {
		for (let value = 1; value <= this.count; value++) {
			yield this.generateUser()
		}
	}
	generateUser() {
		const name = randString(5);
		return {
			Id: this.currentId,
			name,
			email: `${name}@example.com`,
			age: Math.floor(Math.random() * 50) + 18 // Random age between 18 and 67
		};
	}
}


async function prepareData() {
	const stream = fs.createWriteStream(`${__dirname}/../data/input.json`, { flags: 'w' })

	const userGenerator = new UserGenerator();

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
	const writeStream = fs.createWriteStream(`${__dirname}/../data/output.json`, { encoding: 'utf-8' })

	fsReadStream.on('data', (data) => {
		console.log(`readStream got data: ${data.toString()}`);
	})

	writeStream.on('data', (data) => {
		console.log(`writeStream got data: ${data.toString()}`);
	})

	fsReadStream.once('readable', () => {
		// passThrough.end();
		console.log(`readStream readable`);
		// const by = fsReadStream.read(8);
		// console.log(`got 8 bytes: ${by.toString()}`)
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

	// for await (const chunk of fsReadStream) {
	// 	console.log(`iterate: readStream: ${chunk.toString()}`)
	// }

	console.log("beforePipeline")

	const ps = new PassThrough();

	readable.on('data', (chunk) => {
		console.log(`readable: ${chunk.toString()}`)
	})


	await pipeline(fsReadStream, tf, writeStream);
	// await pipeline(readable, tf, writeStream);

	console.log("afterPipeline")

	await finished(fsReadStream)
	await finished(writeStream)
}

main()