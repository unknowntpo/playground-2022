import fs from 'node:fs';
import { Readable, Writable } from 'node:stream';
import { finished } from 'node:stream/promises';
import { User, UserGenerator } from './user.js';

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
