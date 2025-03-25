#[derive(PartialEq, Eq, Clone, Debug)]
pub struct ListNode {
    pub val: i32,
    pub next: Option<Box<ListNode>>,
}

impl ListNode {
    #[inline]
    fn new(val: i32) -> Self {
        ListNode { next: None, val }
    }
}

#[cfg(test)]

mod tests {
    // FIXME: why sometimes we can use crate ?
    use crate::linked_list::ListNode;

    #[test]
    fn test_basic_op() {
        let l = ListNode {
            val: 0,
            next: Some(Box::new(ListNode {
                val: 1,
                next: Some(Box::new(ListNode { val: 2, next: None })),
            })),
        };
        assert_eq!(0, l.val);
        assert_eq!(1, l.next.clone().unwrap().val);
        assert_eq!(2, l.next.clone().unwrap().next.clone().unwrap().val);
        assert_eq!(None, l.next.clone().unwrap().next.clone().unwrap().next);
    }
}
