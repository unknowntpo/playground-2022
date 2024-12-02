import { describe, expect, test, it } from '@jest/globals';

/**
 * 
 * Constraints:
 *  1 <= s.length <= 104
 *  s consists of parentheses only '()[]{}'.
 */
function isValid(s: string): boolean {
	const st: string[] = []
	// 
	const chars = s.split('')
	for (const v of chars) {
		console.log("before", st)
		switch (v) {
			case '(':
			case '[':
			case '{':
				st.push(v);
				// st: ['(']
				console.log("in {", st)
				break;

			case ')':
				console.log('got ): ')
				if (st[st.length - 1] !== '(') { return false; }
				st.pop();
				console.log("in )", st)
				break;
			case ']':
				if (st[st.length - 1] !== '[') { return false; }
				st.pop();
				break;
			case '}':
				if (st[st.length - 1] !== '{') { return false; }
				st.pop();
				break;
		}
	}
	// check if len of st is 0    
	return st.length === 0
}

// [1], 
//   [1, 2], [1, 3], [1, 4]


// [2]

// [3]

// [4]

// start = 1
//   - [1, 2], [1, 3], [1, 4]



interface testCase {
	input: string, want: boolean
}

describe("20-valid-parentheses", () => {
	const testCases: testCase[] = [
		{ input: "()", want: true },
		{ input: "(}", want: false },
		{ input: "(())", want: true },
		{ input: "{([])}", want: true },
	]

	testCases.forEach((tCase) => {
		test(`case of input='${tCase.input}'`, () => {
			const got = isValid(tCase.input)
			console.log(`got: ${got.toString()}`)
			console.log(`want: ${tCase.want.toString()}`)
			expect(got).toEqual(tCase.want)
		})
	})
})
