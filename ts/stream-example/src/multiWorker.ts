import fs from 'node:fs';
import { PassThrough, Readable, Writable, Transform } from 'node:stream';
import { pipeline, finished } from 'node:stream/promises';
import readline from 'node:readline';
import { GeneratedIdentifierFlags, TransformationContext } from 'typescript';
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

async function startWorkerGroup(numOfWorkers: number, workerFn: (workerId: number) => Promise<void>) {
	for (let i = 0; i < numOfWorkers; i++) {
		workerFn(i);
	}
}

async function sleep(ms: number) {
	return await new Promise(resolve => setTimeout(resolve, ms))
}

async function main() {
	const ps = new PassThrough({
		objectMode: true
	})
	await startWorkerGroup(3, async (workerId: number) => {
		console.log(`worker[${workerId}] started...`)
		const gen = new UserGenerator(10);
		for (const user of gen) {
			ps.write(user)
			await sleep(100)
		}
	})

	const Uppercase = new Transform({
		transform: (chunk: any, _encoding, callback) => {
			const user = chunk;
			callback();
		}
	})

	// await pipeline(ps, Uppercase, tf1, tf2, ws);
}


main()
