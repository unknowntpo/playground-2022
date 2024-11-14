import { PassThrough, Writable, Transform } from 'node:stream';
import { pipeline } from 'node:stream/promises';
import { User, UserGenerator } from './user.js';


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
