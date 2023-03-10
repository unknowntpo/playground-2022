
export class ListNode {
    val: number
    next: ListNode | null
    constructor(val?: number, next?: ListNode | null) {
        this.val = (val === undefined ? 0 : val)
        this.next = (next === undefined ? null : next)
    }
    toString(): string {
        let nums: number[] = []
        let cur: ListNode | null = this
        while (cur !== null) {
            nums.push(cur.val)
            cur = cur.next
        }
        return "[" + nums.join(",") + "]"
    }
}

export function makeList(nums: number[]): ListNode | null {
    if (nums.length === 0) {
        return null
    }
    let head: ListNode = new ListNode(nums[0])
    let cur: ListNode = head;
    for (let i = 1; i < nums.length; i++) {
        cur.next = new ListNode(nums[i])
        cur = cur.next
    }
    return head
}