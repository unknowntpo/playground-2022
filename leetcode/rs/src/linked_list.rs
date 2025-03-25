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

    fn from(eles: Vec<i32>) -> Option<Box<ListNode>> {
        if eles.len() == 0 {
            return None;
        }
        let mut root: Option<Box<ListNode>> = Some(Box::new(ListNode::new(eles[0])));
        let mut p = root.as_mut().unwrap();
        for e in eles.iter().skip(1) {
            p.next =Some(Box::new(ListNode::new(*e)));
            p = p.next.as_mut().unwrap();
        }

        root
    }
}
//
// fn from(eles: Vec<i32>) -> Option<Box<ListNode>> {
//     let mut root: Option<Box<ListNode>> = None;
//     let mut p= &mut root;
//     for e in eles {
//         p.as_mut().unwrap().val = e;
//         p.as_mut().unwrap().next = None;
//     }
//
//     root
// }

impl From<Vec<i32>> for ListNode {
    fn from(eles: Vec<i32>) -> Self {
        *ListNode::from(eles).unwrap()
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

    #[test]
    fn test_from() {
        // list with no element
        let res = ListNode::from(vec![]);
        assert_eq!(res, None);

        let l = ListNode::from(vec![0, 1, 2]).unwrap();
        assert_eq!(0, l.val);
        assert_eq!(1, l.next.as_ref().unwrap().val);
        assert_eq!(2, l.next.as_ref().unwrap().next.as_ref().unwrap().val);
        assert_eq!(None, l.next.as_ref().unwrap().next.as_ref().unwrap().next);
    }
}
