import fs from 'node:fs';
import { PassThrough, Readable, Writable, Transform } from 'node:stream';
import { pipeline, finished } from 'node:stream/promises';
import readline from 'node:readline';
import { GeneratedIdentifierFlags } from 'typescript';
import { readAndWrite } from './stream';

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
					// yield {
					// 	value: this.generateUser(),
					// 		done: false
					// };
					return {
						value: this.generateUser(),
						done: false
					};
				}
				return { done: true, value: undefined }
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
	// const f = fs.openSync(`${__dirname}/../data/input.json`, "a+")

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

		// If we don't wait for finished, in main function, readStream can not read data.
		await finished(stream);
	}
}

async function main() {
	const gen = new UserGenerator(10);
	const rs = Readable.from(gen, { objectMode: true, highWaterMark: 1 });

	rs.on('drain', () => {
		console.log(`rs: drain`)
	})

	const ws = new Writable({
		objectMode: true, highWaterMark: 2, write: (
			chunk: any,
			encoding: BufferEncoding,
			callback: (error?: Error | null) => void,
		) => {
			const ch = chunk as User;
			console.log(`User: id: ${ch.Id}, name: ${ch.name}, age: ${ch.age}, email: ${ch.email}`)
			callback();
		}
	});

	ws.on('drain', () => {
		console.log(`ws: drain`)
	})

	rs.pipe(ws);

	await finished(rs);
	await finished(ws);
}


main()