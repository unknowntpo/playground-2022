import {describe, expect, test,it} from '@jest/globals';

// Given a string array words, return an array of all characters that show up in all strings within the words (including duplicates). You may return the answer in any order.

 

// Example 1:

// Input: words = ["bella","label","roller"]
// Output: ["e","l","l"]
// Example 2:

// Input: words = ["cool","lock","cook"]
// Output: ["c","o"]
 

// Constraints:

// 1 <= words.length <= 100
// 1 <= words[i].length <= 100
// words[i] consists of lowercase English letters.

function commonChars(words: string[]): string[] {
    // build alphabet hashmap
    // append one by one
    // if showUp: means refcnt of that alphabet will be len(words) * N, where N is integer
    return ["aa", "bb", "cc"]
};

interface testCase {
    input: string[];
    want: string[];
}

describe("find common chars",()=> {
   const testCases:testCase[] = [
        // with dup letter
        { input: ["a"], want: ["0"] },
        { input: ["h"], want: ["7"] },
        { input: ["z"], want: ["2"] },
    ]
    testCases.forEach((tCase) => {
        it(`letter = ${tCase.input} should return correct answer`, () => {
            expect(commonChars(tCase.input)).toEqual(tCase.want)
        })
    })
})