import { describe, expect, test, it } from '@jest/globals';


// https://leetcode.com/problems/palindrome-partitioning/
function partition(s: string): string[][] {
  return [["a"]]
};

/*
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
