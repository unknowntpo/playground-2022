import fs from 'node:fs';
import { PassThrough, Transform } from 'node:stream';
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

	[Symbol.iterator]() {
		return {
			next: () => {
				if (this.currentId < this.count) {
					this.currentId++;
					return {
						value: this.generateUser(),
						done: false
					};
				}
				return { done: true }
			}
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
	const f = fs.openSync(`${__dirname}/../data/input.json`, "a+")

	const stream = fs.createWriteStream(`${__dirname}/../data/input.json`)

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
	}
}

async function main() {
	await prepareData();

	const readStream = fs.createReadStream(`${__dirname}/../data/input.json`, { encoding: 'utf-8' })
	const writeStream = fs.createWriteStream(`${__dirname}/../data/output.json`, { encoding: 'utf-8' })

	readStream.on('data', (data) => {
		console.log(`readStream got data: ${data.toString()}`);
	})

	writeStream.on('data', (data) => {
		console.log(`writeStream got data: ${data.toString()}`);
	})

	readStream.on('end', () => {
		// passThrough.end();
		console.log(`readStream ended`);
	})

	readStream.on('error', function (err) {
		console.log(err.stack);
	});

	const tf = new Transform({
		transform(chunk, _encoding, callback) {
			let str = chunk.toString();
			console.log(`Transform got data: ${str}`)
			this.push(str);
			callback();
		},
	});

	console.log("beforePipeline")

	await pipeline(readStream, tf, writeStream);

	console.log("afterPipeline")

	await finished(readStream)
	await finished(writeStream)
}

main()