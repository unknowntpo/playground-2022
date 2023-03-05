import {describe, expect, test,it} from '@jest/globals';



// https://leetcode.com/problems/remove-element/
// Given the head of a linked list and an integer val,
// remove all the nodes of the linked list that has Node.val == val,
// and return the new head.

// Input: head = [1,2,6,3,4,5,6], val = 6
// Output: [1,2,3,4,5]
// Example 2:

// Input: head = [], val = 1
// Output: []
// Example 3:

// Input: head = [7,7,7,7], val = 7
// Output: []

// Constraints:

// The number of nodes in the list is in the range [0, 104].
// 1 <= Node.val <= 50
// 0 <= val <= 50

/**
 * Definition for singly-linked list.
 * class ListNode {
 *     val: number
 *     next: ListNode | null
 *     constructor(val?: number, next?: ListNode | null) {
 *         this.val = (val===undefined ? 0 : val)
 *         this.next = (next===undefined ? null : next)
 *     }
 * }
 */

class ListNode {
  val: number
  next: ListNode | null
  constructor(val?: number, next?:ListNode | null) {
    this.val = (val===undefined ? 0 : val)
    this.next = (next===undefined? null: next)
  }
  toString(): string {
    let nums: number[] = []
    let cur: ListNode | null = this
    while (cur !== null) {
      nums.push(cur.val)
      cur = cur.next
    }
    return "["+nums.join(",")+"]"
  }
}

function makeList(nums: number[]): ListNode | null {
  if ( nums.length === 0 ) {
    return null
  }
  let head : ListNode = new ListNode(nums[0])
  let cur: ListNode = head;
  for (let i = 1; i < nums.length; i++) {
    cur.next = new ListNode(nums[i])
    cur = cur.next
  }
  return head
}


function removeElements(head: ListNode | null, val: number): ListNode | null {
  return null
};

// Input: head = [1,2,6,3,4,5,6], val = 6
// Output: [1,2,3,4,5]
// Example 2:

// Input: head = [], val = 1
// Output: []
// Example 3:

// Input: head = [7,7,7,7], val = 7
// Output: []
describe("203-remove-linked-list-elements",()=>{
  test("head = [1,2,6,3,4,5,6], val = 6",()=>{
    let input: ListNode | null = makeList([1,2,6,3,4,5,6])
    console.log(input?.toString())
    expect(input?.toString()).toEqual("[1,2,6,3,4,5,6]")
    expect(1+1).toEqual(2)
  })
})