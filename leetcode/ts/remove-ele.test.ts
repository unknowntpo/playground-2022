import { describe, expect, test, it } from '@jest/globals';
import { reverse } from 'dns';

import { ListNode, makeList } from './list';

// https://leetcode.com/problems/remove-element/

function combine(n: number, k: number): number[][] {
  return [][1, 2, 3];
};

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
    { n: 4, k: 2, want: [[1]] }
  ]

  testCases.forEach((tCase) => {
    test(`combinations of n=${tCase.n}, k=${tCase.k}`, () => {
      expect(combine(tCase.n, tCase.k)).toEqual(tCase.want)
    })
  })
})
