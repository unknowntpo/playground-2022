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
	const gen = new UserGenerator(30);
	const rs = Readable.from(gen, { objectMode: true, highWaterMark: 10 });

	rs.on('drain', () => {
		console.log(`rs: drain`)
	})

	rs.on('data', () => {
		console.log(`readable length: ${rs.readableLength}`)
	})

	const ws = new Writable({
		objectMode: true, highWaterMark: 10, write: async (
			chunk: any,
			encoding: BufferEncoding,
			callback: (error?: Error | null) => void,
		) => {
			const ch = chunk as User;
			console.log(`User: id: ${ch.Id}, name: ${ch.name}, age: ${ch.age}, email: ${ch.email}`)
			await sleep(100);
			console.log(`ws: write one data, ${ws.writableLength}`)
			callback();
		},
	});

	ws.on('data', () => {
		console.log(`writable length: ${ws.writableLength}`)
	})

	ws.on('drain', () => {
		console.log(`ws: drain`)
	})

	for await (const chunk of rs) {
		console.log(`read one data from rs`)
		const canWrite = ws.write(chunk)
		if (!canWrite) {
			// sleep for a while
			console.log(`writer reached highWaterMark, slow down...`)
			await sleep(1000);
		}
	}

	await finished(rs);
	await finished(ws);
}


main()

async function sleep(ms: number) {
	return await new Promise(resolve => setTimeout(resolve, ms))
}