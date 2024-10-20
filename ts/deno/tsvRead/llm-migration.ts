import readline from 'node:readline';

import fs from 'fs-extra';
import mongoose from 'mongoose';

interface TsvRows {
	header: string[];
	rows: string[][];
}

function replaceNewlinesInQuotes(str: string): string {
	return str.replace(/"[^"]*"/g, function (match) {
		return match.replace(/\n/g, '\\n');
	});
}

async function getTsvRowsFromFileNoReadLine(fileName: string) {
	const rawTsv = await fs.readFile(fileName, { flag: 'r' });

	// const rl = readline.createInterface({ input: fs.createReadStream(fileName), crlfDelay: Infinity });

	const rawTsvRows = replaceNewlinesInQuotes(rawTsv.toString()).split('\n');

	const trimmedTsvRows: string[] = [];
	for (const rawTsvRow of rawTsvRows) {
		const lastIdx = rawTsvRow.lastIndexOf(`"`)
		trimmedTsvRows.push(rawTsvRow.slice(0, lastIdx + 1));
		// if (rawTsvRow.endsWith("\\n")) {
		// 	console.log(`xxx end with newline`)
		// 	// tirm last \n
		// 	const lastCell = rawTsvRows[rawTsvRows.length - 1];
		// 	rawTsvRows[rawTsvRows.length - 1] = lastCell.slice(0, lastCell.length - 1)
		// }
	}

	const tsvRows: TsvRows = { header: [], rows: [] };

	let headerRead = false;
	for (const row of trimmedTsvRows) {
		// eslint-disable-next-line no-console
		// console.log(`readline line: ${row}`);
		if (!headerRead) {
			tsvRows.header = getTsvRow(row);
			headerRead = true;
			continue;
		}
		tsvRows.rows.push(getTsvRow(row));
	}

	return tsvRows;
}

async function getTsvRowsFromFile(fileName: string) {
	const rawTsv = await fs.readFile(fileName, { flag: 'r' });

	const rl = readline.createInterface({ input: fs.createReadStream(fileName), crlfDelay: Infinity });

	const tsvRows: TsvRows = { header: [], rows: [] };

	let headerRead = false;
	for await (const line of rl) {
		// eslint-disable-next-line no-console
		// console.log(`readline line: ${line}`);
		if (!headerRead) {
			tsvRows.header = getTsvRow(line);
			headerRead = true;
			continue;
		}
		tsvRows.rows.push(getTsvRow(line));
	}

	return tsvRows;
}

function getTsvRow(rowStr: string): string[] {
	return rowStr.split('\t').map((quotedCell) => unquote(quotedCell));
	// return rowStr.split('\t');
}

function unquote(quotedCell: string) {
	// eslint-disable-next-line no-console
	// console.log(`got cell: ${quotedCell}`);
	if (quotedCell.length < 2) {
		throw new Error(`cell ${quotedCell} is not quoted`);
	}
	if (!quotedCell.startsWith('"') || !quotedCell.endsWith('"')) {
		throw new Error(`cell ${quotedCell} is not quoted`);
	}
	return quotedCell.slice(1, quotedCell.length - 1);
}

// const res = getTsvRowsFromFile('./test.tsv');
const res = await getTsvRowsFromFileNoReadLine('./test.tsv');

// eslint-disable-next-line no-console
console.log(JSON.stringify(res, null, '\t'));
