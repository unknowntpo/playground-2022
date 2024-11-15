import fs from 'node:fs';
import { Readable, Writable } from 'node:stream';
import { finished } from 'node:stream/promises';
import { User, UserGenerator } from './user.js';
import { readAndWrite } from './stream.js';

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
	const rs = Readable.from(gen, { objectMode: true, highWaterMark: 30 });

	rs.on('data', () => {
		console.log(`rs got data, readable length: ${rs.readableLength}`)
	})

	rs.on('end', () => {
		// end event ca
		console.log(`⛔ rs: ended`)
	})

	const ws = new Writable({
		objectMode: true, highWaterMark: 10, write: async (
			chunk: any,
			encoding: BufferEncoding,
			callback: (error?: Error | null) => void,
		) => {
			const ch = chunk as User;
			console.log(`ws: one data is written, ws.writableLength: ${ws.writableLength}`)
			console.log(`User: id: ${ch.Id}, name: ${ch.name}, age: ${ch.age}, email: ${ch.email}`)
			await sleep(50);
			callback();
		},
	});

	ws.on('data', () => {
		console.log(`writable length: ${ws.writableLength}`)
	})

	ws.on('drain', () => {
		console.log(`ws: drain`)
	})

	ws.on('finish', () => {
		// end event ca
		console.log(`👌 ws: finished`)
	})

	ws.on('end', () => {
		// end event ca
		console.log(`⛔ ws: ended, writableLength: ${ws.writableLength}`)
	})

	for await (const chunk of rs) {
		console.log(`read one data from rs`)
		console.log(`after read, rs.readableLength: ${rs.readableLength}`)

		console.log(`before write, rs.writableLength: ${ws.writableLength}`)
		const canWrite = ws.write(chunk)
		if (!canWrite) {
			// sleep for a while
			console.log(`❌ ws reached highWaterMark, slow down...`)
			await new Promise(resolve => ws.once('drain', resolve))
			console.log(`✅ got drain event from ws, continue ...`)
		}
	}

	await finished(rs);

	await finished(ws);
	await sleep(3000);
}


main()

async function sleep(ms: number) {
	return await new Promise(resolve => setTimeout(resolve, ms))
}
