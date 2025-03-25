use crate::linked_list::ListNode;

struct Solution {}

impl Solution {
    ///
    /// 提示：
    ///
    /// 链表中节点的数目范围是 [0, 5000]
    /// -5000 <= Node.val <= 5000
    ///
    ///
    /// 进阶：链表可以选用迭代或递归方式完成反转。你能否用两种方法解决这道题？
    pub fn reverse_list(head: Option<Box<ListNode>>) -> Option<Box<ListNode>> {
        let mut head = head;
        let mut p: Option<Box<ListNode>> = None;
        while let Some(mut node) = head {
            head = node.next.take();
            node.next = p.take();
            p = Some(node);
        }

        p
    }
}

#[cfg(test)]
mod test {
    use crate::linked_list::ListNode;
    use crate::reverse_linked_list_206::Solution;
    use rstest::rstest;

    #[rstest]
    #[case::basic(vec![1,2,3], vec![3,2,1])]
    fn test_reverse(#[case] eles: Vec<i32>, #[case] want: Vec<i32>) {
        let l = ListNode::from(eles);
        assert_eq!(
            Solution::reverse_list(Some(Box::new(l))),
            Some(Box::new(ListNode::from(want)))
        );
    }
}
