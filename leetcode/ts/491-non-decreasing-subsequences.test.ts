import { describe, expect, test, it } from '@jest/globals';

interface testCase {
    nums: Array<number>;
    want: Array<Array<number>>;
}

function findSubsequences(nums: number[]): number[][] {
    let res: Array<Array<number>> = [];
    let path: Array<number> = [];
    const n = nums.length;
    const pick = (start: number): void => {
        for (let i = start; i < n; i++) {
            let cond = false;
            const last = path[path.length - 1];
            if (path.length == 0 || nums[i] >= last && nums[i] != last) {
                path.push(nums[i]);
                res.push(path.slice());
                pick(i + 1);
                path.pop();
            }
        }
    }
    pick(0);
    return res;
};
// 示例 1：

// 输入：nums = [4,6,7,7]
// 输出：[[4,6],[4,6,7],[4,6,7,7],[4,7],[4,7,7],[6,7],[6,7,7],[7,7]]
// 示例 2：

// 输入：nums = [4,4,3,2,1]
// 输出：[[4,4]]
const testCases = [
    { nums: [4, 6, 7, 7], want: [[4, 6], [4, 6, 7], [4, 6, 7, 7], [4, 7], [4, 7, 7], [6, 7], [6, 7, 7], [7, 7]] },
    { nums: [4, 4], want: [[4, 4]] },
]

describe('491-on-decreasing-subsequences', () => {
    testCases.forEach((tCase) => {
        const ans = findSubsequences(tCase.nums);
        console.error(`ans: ${JSON.stringify(ans, null, "\t")}`)
        expect(ans).toBe(tCase.want)
    })

})






