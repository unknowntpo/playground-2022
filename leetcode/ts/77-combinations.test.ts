import { describe, expect, test, it } from '@jest/globals';
import { reverse } from 'dns';
import { connect } from 'http2';

import { ListNode, makeList } from './list';

// https://leetcode.com/problems/remove-element/

// C42
// [1]

// [1, 2], [1, 3][1, 4][2, 3]

function combine(n: number, k: number): number[][] {
  let res: number[][] = []
  function pick(start: number, k: number, combo: number[]) {
    // end condition
    if (combo.length == k) {
      res.push(combo)
      return
    }

    for (let i = start; i <= n; i++) {
      // current level of all possible result
      const comb = combo.concat([start])

      // append possible number to com
      pick(i + 1, k, comb)
    }
  }

  pick(1, k, [])

  return res
};

// [1], 
//   [1, 2], [1, 3], [1, 4]


// [2]

// [3]

// [4]

// start = 1
//   - [1, 2], [1, 3], [1, 4]



describe("test ", () => {
  test("1+1", () => {
    expect(1 + 1).toEqual(2)
  })
})

interface testCase {
  n: number, k: number, want: number[][]
}

describe("77-combinations", () => {
  const testCases: testCase[] = [
    { n: 4, k: 2, want: [[1, 2], [1, 3], [1, 4], [2, 3], [2, 4], [3, 4]] },
    { n: 1, k: 1, want: [[1]] }
  ]

  testCases.forEach((tCase) => {
    test(`combinations of n=${tCase.n}, k=${tCase.k}`, () => {
      const got = combine(tCase.n, tCase.k)
      console.log(`got: ${got.toString()}`)
      console.log(`want: ${tCase.want.toString()}`)
      expect(got).toEqual(tCase.want)
    })
  })
})
