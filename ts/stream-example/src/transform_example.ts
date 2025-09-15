import {Readable, Transform} from "node:stream";
import {pipeline} from "node:stream/promises";

async function main() {
    // map reduce
    // 33, 3, 1, 44
    // read
    const s = `hello, 1
hello, 1
hello, 1
hello, 1
world, 1
world, 1
kk, 1
abc, 1`
    const strings = s.split("\n");
    console.log(strings);

    const input = Readable.from(s.split("\n"));

    const splitTf = new Transform({
        objectMode: true,
        transform(chunk, _encoding, callback) {
            console.log(typeof chunk);
            if (typeof chunk !== "string") {
                callback(new Error(`Expected string, got ${typeof chunk}`));
                return;
            }
            const columns = chunk.toString().split(', ');
            console.log(columns);
            if (columns.length !== 2) {
                callback(new Error(`Could not find ${columns.length} column`));
                return;
            }
            const key = columns[0];
            const value = columns[1];

            callback(null, {key, value});
        }
    })

    const counter = new Map<string, number>();

    const reduceTf = new Transform({
        objectMode: true,
        transform(chunk, _encoding, callback) {
            const {key, value} = chunk;
            counter.set(key, parseInt(value, 10));
            callback();
        }
    })
    await pipeline(input, splitTf, reduceTf);

    console.log(`counter: ${counter.size} rows, data: ${JSON.stringify(Object.fromEntries(counter), null, "\t")}`);
}

main()