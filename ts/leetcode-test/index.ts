
interface lawContent {
	content: string
}

interface Law {
	stratum?: string,
	list?: Law[],
	child?: Law
	content?: string,
}

const levelStrings = [
	"paragraphs",
	"subparagraphs",
	"items"
]
enum Level {
	paragraphs = 0,
	subparagraphs = 1,
	items = 2,
}

class Parser {
	private rawContent: string;
	private lawContents: string[];
	/**
	 * Index of current lawContent
	 */
	private idx: number;

	constructor(content: string) {
		this.rawContent = content;
		this.lawContents = [];
		this.idx = 0;
	}

	parse(): Law {
		this.lawContents = this.flatLawContent(this.rawContent);
		return this.parseLaw(0);
	}

	private flatLawContent(rawData: string) {
		return rawData.split("\n");
	}

	private nextContent(): string | null {
		if (this.idx >= this.lawContents.length) {
			return null;
		}
		const content = this.lawContents[this.idx];
		this.idx++;
		return content;
	}

	getLevel(content: string): Level {
		// 項:沒有數字
		const regList = [
			//  // 項:沒有數字
			// 廢棄
			new RegExp(/^[^一二三四五六七八九十]/, 'g'),
			// 「款冠以一、二、三等數字」：特徵必定為 一、、二、 ... 諸此類推，使用的標點符號必定為全行頓號 、。
			new RegExp(/^[一二三四五六七八九十]+、/, 'g'),
			// 「目冠以（一）、（二）、（三）等數字」：特徵必定為 （一）、（二） ... 諸此類推，使用的標點符號必定為全形括號 （） 或半形括號 ()。
			new RegExp(/^[\(（][一二三四五六七八九十]+[\)）]/, 'g'),
		];
		for (const [i, parser] of regList.slice(1).entries()) {
			if (parser.test(content)) {
				return i + 1;
			}
		}
		// nothing match: it's 項
		return 0;
		// throw new Error(`No matched RegExp at ${this.lawContents[this.idx]}`);
	}


	private parseLaw(level: Level): Law {
		const curLaw: Law = {
			stratum: levelStrings[level],
			list: []
		};
		for (let content = this.nextContent(); content !== null; content = this.nextContent()) {
			// e.g.
			// [a], push a
			// [a, b], push b
			console.log(`curLevel: ${level}, level of ${content}: ${this.getLevel(content)}`)
			switch (this.getLevel(content)) {
				case level: {
					curLaw.list!.push({
						// if has child, then we should set stratum 
						content: content,
					})
					// same level
					break;
				}
				case level + 1: {
					this.stepBack();
					const last = curLaw.list!.pop();
					last!.child! = this.parseLaw(level + 1);
					curLaw.list?.push(last!);
					break;
				}
				case level - 1: {
					// done, let caller keep parsing this.lawsContents
					this.stepBack();
					return curLaw;
				}
				default:
					throw new Error(`Wrong Level: ${level}`);
			}
		}
		return curLaw;
	}
	stepBack() {
		if (this.idx > 0)
			this.idx--;
	}
}



/*
 

A: asdfs
B: adsf
1. asdfsadf
2. sdfds
3. dfsdfa
C: dfasd
D: asdfsdf

// indent rules: if same as stack.top() -1, then outdent:
// else: indent
// parseLaw(0) // A
// parseLaw(0) // B
// parseLaw(1) // B.child[0]
// parseLaw(1) // B.child[1]
// parseLaw(1) // B.child[1]
// parseLaw(0) // C


// list
// child



 */

export { Parser } 
