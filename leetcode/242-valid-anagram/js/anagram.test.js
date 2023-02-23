https://leetcode.com/problems/valid-anagram/

// Given two strings s and t, return true if t is an anagram of s, and false otherwise.

// An Anagram is a word or phrase formed by rearranging the letters of a different word or phrase, typically using all the original letters exactly once.

// Example 1:

// Input: s = "anagram", t = "nagaram"
// Output: true
// Example 2:

// Input: s = "rat", t = "car"
// Output: false

// Constraints:

// 1 <= s.length, t.length <= 5 * 104
// s and t consist of lowercase English letters.


/**
 * @param {string} s
 * @param {string} t
 * @return {boolean}
 */
var isAnagramMap = function (s, t) {
    // using array as map
    let alphabetMap = new Array(26).fill(0)

    // append all elements in s to alphabetMap
    // s.forEach((e) => {
    for (const e of s) {
        alphabetMap[getAlphaIdx(e)]++
    }

    for (const e of t) {
        alphabetMap[getAlphaIdx(e)]--
    }

    for (const refCnt of alphabetMap) {
        if (refCnt != 0) {
            return false
        }
    }
    console.log("map", alphabetMap)

    return true
};

// only lowercase alphabet
const getAlphaIdx = (a) => {
    return a.charCodeAt(0) - 97
}

describe("getAlphaIdx", () => {
    const testCases = [
        // with dup letter
        { letter: "a", want: 0 },
        { letter: "h", want: 7 },
        { letter: "z", want: 25 },
    ]
    testCases.forEach((tCase) => {
        it(`letter = ${tCase.letter} should return correct answer`, () => {
            expect(getAlphaIdx(tCase.letter)).toEqual(tCase.want)
        })
    })

})

describe("isAnagram", () => {
    const testCases = [
        // with dup letter
        { s: "anagram", t: "nagaram", want: true },
        { s: "rat", t: "car", want: false },
    ]
    testCases.forEach((tCase) => {
        it(`s = ${tCase.s}, t = ${tCase.t} should return correct answer`, () => {
            expect(isAnagramMap(tCase.s, tCase.t)).toEqual(tCase.want)
        })
    })
})
