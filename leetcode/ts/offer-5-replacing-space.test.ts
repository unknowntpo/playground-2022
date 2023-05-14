import { describe, expect, test, it } from '@jest/globals';

// https://leetcode.cn/problems/ti-huan-kong-ge-lcof/
function replacing_space(s: string): string {
  let num_of_white_space = find_num_of_space(s)
  let old_len = s.length
  let new_len = old_len + num_of_white_space * 2
  let new_array = s.split('')

  // fill white space
  while (new_array.length < new_len) {
    new_array.push(' ');
  }

  let left = old_len, right = new_len
  while (left < right) {
    left--
    right--
    if (new_array[left] == ' ') {
      new_array[right--] = '0'
      new_array[right--] = '2'
      new_array[right] = '%'
      // right -= 2
    } else {
      new_array[right] = new_array[left]
    }
  }
  return new_array.join('')
}

const find_num_of_space = (s: string): number => {
  // "hello world" -> ["hello", "world"]
  return s.split(' ').length - 1
}

interface testCase {
  s: string, want: string
}

describe("replacing_space", () => {
  const testCases: testCase[] = [
    { s: "", want: "" },
    { s: "a aa", want: "a%20aa" },
    { s: "hello world.", want: "hello%20world." },
    { s: "   ", want: "%20%20%20" },
  ]

  testCases.forEach((tCase) => {
    test(`replacing_space of s = ${tCase.s} `, () => {
      const got = replacing_space(tCase.s)
      console.log(`got: ${got.toString()} `)
      console.log(`want: ${tCase.want.toString()} `)
      expect(got).toEqual(tCase.want)
    })
  })
})
