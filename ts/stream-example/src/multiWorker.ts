import { PassThrough, Writable, Transform } from 'node:stream';
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

	*[Symbol.iterator]() {
		for (; this.currentId <= this.count; this.currentId++) {
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

const generator = new UserGenerator(30);

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
		for (const user of generator) {
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

	await pipeline(ps, toLowercaseName, ws);
}


main()
