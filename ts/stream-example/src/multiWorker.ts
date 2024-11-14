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
		for (let currentId = 0; currentId <= this.count; currentId++) {
			yield this.generateUser()
		}
	}

	generateUser(): User {
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
		// should set writableObjectMode, not objectMode
		// https://stackoverflow.com/a/54401036
		objectMode: true,
		// writableObjectMode: true,
		// readableObjectMode: true
	})

	const ws = new Writable({
		objectMode: true,
		write: (chunk, encoding, callback) => {
			console.dir(chunk)
			callback();
		}
	})


	await startWorkerGroup(3, async (workerId: number) => {
		console.log(`worker[${workerId}] started...`)
		const gen = new UserGenerator(10);
		for (const user of gen) {
			console.log(`${workerId} try to write user: ${JSON.stringify(user)}`)
			ps.write(user)
			console.log(`${workerId} write user done`)

			await sleep(100)
		}
	})

	const toLowercaseName = new Transform({
		objectMode: true,
		transform: (chunk: any, _encoding, callback) => {
			let user = chunk as User;
			user.name = user.name.toLowerCase()
			callback(null, user);
		}
	})

	// await pipeline(ps, ws);

	await pipeline(ps, toLowercaseName, ws);

	// await pipeline(ps, Uppercase, tf1, tf2, ws);
}


main()
