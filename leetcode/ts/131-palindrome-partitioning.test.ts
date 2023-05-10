import { describe, expect, test, it } from '@jest/globals';


// https://leetcode.com/problems/palindrome-partitioning/
function partition(s: string): string[][] {
  let out: string[][] = [[]]
  dp(s, 0, out)
  return out
};

function dp(s: string, startIdx: number, out: string[][]) {
  if (s.length == 0) return
  if (s.slice(0, startIdx).split("").reverse().join("") !== s) return
  for (let i = startIdx; i < s.length; i++) {
    dp(s.slice(0, i), i, out)
  }
}
/*
aab

a
a
b

aa
aab (x

aab

(x)

aa


aab

a 
  aa 
  

*/

interface testCase {
  s: string, want: string[][]
}

describe("131-palindrome-partitioning", () => {
  const testCases: testCase[] = [
    { s: "aab", want: [["a", "a", "b"], ["aa", "b"]] }
  ]

  testCases.forEach((tCase) => {
    test(`partition of s=${tCase.s}`, () => {
      const got = partition(tCase.s)
      console.log(`got: ${got.toString()}`)
      console.log(`want: ${tCase.want.toString()}`)
      expect(got).toEqual(tCase.want)
    })
  })
})
