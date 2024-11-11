import fs from 'node:fs';
import { PassThrough } from 'node:stream';
import { pipeline } from 'node:stream/promises';

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

	const readStream = fs.createReadStream(`${__dirname}/../data/input.json`)
	const writeStream = fs.createWriteStream(`${__dirname}/../data/output.json`)

	readStream.pipe(writeStream);

	const passThrough = new PassThrough()

	passThrough.on('data', (chunk) => {
		console.log(chunk.toString());
	});

	await pipeline(readStream, passThrough, writeStream);

	readStream.on('data', (data) => {
		console.log(data.toString());

		// passThrough.end();
		// writeStream.end();
	})


	readStream.on('end', () => {
		passThrough.end();
		writeStream.end();
	})
}

main()