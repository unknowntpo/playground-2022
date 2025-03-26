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
    pub fn reverse_list_iter(head: Option<Box<ListNode>>) -> Option<Box<ListNode>> {
        let mut cur = head;
        let mut pre: Option<Box<ListNode>> = None;
        while let Some(mut node) = cur {
            cur = node.next;
            node.next = pre;
            pre = Some(node);
        }

        pre
    }

    pub fn reverse_list_recr(head: Option<Box<ListNode>>) -> Option<Box<ListNode>> {
        fn reverse_and_get_head(
            pre: Option<Box<ListNode>>,
            cur: Option<Box<ListNode>>,
        ) -> Option<Box<ListNode>> {
            if cur.is_none() {
                return pre;
            }
            let mut cur = cur.unwrap();
            let nxt = cur.next;
            cur.next = pre;
            reverse_and_get_head(Some(cur), nxt)
        }
        reverse_and_get_head(None, head)
    }
}

#[cfg(test)]
mod test {
    use crate::linked_list::ListNode;
    use crate::reverse_linked_list_206::Solution;
    use rstest::rstest;

    type TestFn = fn(Option<Box<ListNode>>) -> Option<Box<ListNode>>;

    #[rstest]
    #[case::basic(vec![1,2,3], vec![3,2,1], vec![Solution::reverse_list_iter, Solution::reverse_list_recr]
    )]
    #[case::empty(vec![], vec![], vec![Solution::reverse_list_iter, Solution::reverse_list_recr])]
    fn test_reverse(#[case] eles: Vec<i32>, #[case] want: Vec<i32>, #[case] methods: Vec<TestFn>) {
        for f in methods {
            let l = if eles.len() == 0 {
                None
            } else {
                Some(Box::new(ListNode::from(eles.clone())))
            };
            let want = if want.len() == 0 {
                None
            } else {
                Some(Box::new(ListNode::from(want.clone())))
            };
            assert_eq!(
                f(l),
                want
            );
        }
    }
}
