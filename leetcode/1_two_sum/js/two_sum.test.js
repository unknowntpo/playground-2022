// https://leetcode.com/problems/two-sum/
var twoSum = function (nums, target) {
    m = {}
    for (let idx = 0; idx < nums.length; idx++) {
        e = nums[idx]
        pairIdx = m[e.toString()]
        if (pairIdx != undefined && nums[pairIdx.toString()] + e === target) {
            return [pairIdx, idx]
        }
        m[(target - e).toString()] = idx
    }
};

describe("twoSum", () => {
    describe("input: [2,7,11,15], target: 9", () => {
        beforeEach(() => {
            input = [2, 7, 11, 15]
            target = 9
        })
        test("should return correct output", () => {
            expect(twoSum(input, target)).toEqual([0, 1])
        })
    })
})