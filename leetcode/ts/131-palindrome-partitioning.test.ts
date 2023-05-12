import { describe, expect, test, it } from '@jest/globals';


// https://leetcode.com/problems/palindrome-partitioning/
function partition(s: string): string[][] {
  let out: string[][] = []
  let acc: string[] = []
  backtrack(s, 0, acc, out)
  return out
};

function backtrack(s: string, startIdx: number, acc: string[], out: string[][]) {
  if (startIdx === s.length) {
    console.log("acc: ", acc)
    // FIXME: Why need acc.slice() ?
    out.push(acc.slice())
    console.log("out: ", out)
    return
  }
  for (let i = startIdx; i < s.length; i++) {
    if (isPalimdrome(s.slice(startIdx, i + 1))) {
      acc.push(s.slice(startIdx, i + 1))
      backtrack(s, i + 1, acc, out)
      acc.pop()
    }
  }
};

function isPalimdrome(str: string): boolean {
  return [...str].reverse().join("") === str
}

// aab
// sub: a
// []






/*
    r
 a aa aab
        (x)

   b

a ab(x)

a

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
