import { describe, expect, test, it } from '@jest/globals';
import { ListNode, makeList } from './list';

// https://leetcode.com/problems/remove-element/

describe("test ", () => {
  test("1+1", () => {
    expect(1 + 1).toEqual(2)
  })
})


// Recursive
function reverseList(head: ListNode | null): ListNode | null {
  return rev(null, head)
};

function rev(pre: ListNode | null, cur: ListNode | null): ListNode | null {
  if (cur?.next === null) {
    cur.next = pre!
    return cur
  }
  const nxt: ListNode | null = cur?.next || null
  if (cur) {
    cur.next = pre || null
  }
  return rev(cur, nxt)
}


interface tCase {
  input: number[]
  expect: string
}

describe("206-reverse-linked-list", () => {
  const testCases: tCase[] = [
    { input: [5, 4, 3, 2, 1], expect: '[1,2,3,4,5]' }
  ]
  testCases.forEach((tCase) => {
    test(`input: ${tCase.input}`, () => {
      const input: ListNode | null = makeList(tCase.input)
      const res: ListNode | null = reverseList(input);
      expect(res!.toString()).toEqual(tCase.expect)
    })
  })
})