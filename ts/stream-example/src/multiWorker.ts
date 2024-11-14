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

async function startWorkerGroup(numOfWorkers: number, workerFn: any) {
	for (let i = 0; i < numOfWorkers; i++) {
		setTimeout(() => {

		})
	}
}


async function main() {

}


main()

async function sleep(ms: number) {
	return await new Promise(resolve => setTimeout(resolve, ms))
}


