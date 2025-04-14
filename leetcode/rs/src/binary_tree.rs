use std::{cell::RefCell, rc::Rc};

#[derive(Debug, PartialEq)]
struct BinaryTree<T> {
    pub root: NodeType<T>,
}

impl<T> Into<Vec<T>> for BinaryTree<T> {
    fn into(self) -> Vec<T> {
        vec![]
    }
}

impl<T> From<Vec<T>> for BinaryTree<T> {
    fn from(eles: Vec<T>) -> Self {
        BinaryTree::new(Some(Rc::new(RefCell::new(TreeNode {
            left: None,
            right: None,
        }))))
    }
}

type NodeType<T> = Option<Rc<RefCell<TreeNode<T>>>>;

#[derive(Debug, PartialEq)]
struct TreeNode<T> {
    left: NodeType<T>,
    right: NodeType<T>,
}

impl<T> TreeNode<T> {
    fn new(left: NodeType<T>, right: NodeType<T>) -> Self {
        Self { left, right }
    }
}

impl<T> BinaryTree<T> {
    fn new(root: NodeType<T>) -> BinaryTree<T> {
        Self { root }
    }
}

#[cfg(test)]
mod tests {
    use super::*;
    use rstest::rstest;

    fn add(a: i32, b: i32) -> i32 {
        a + b
    }

    #[rstest]
    #[case(1, 2, 3)]
    fn test_eq(#[case] a: i32, #[case] b: i32, #[case] want: i32) {
        assert_eq!(want, add(a, b));
    }

    #[rstest]
    fn test_new() {
        let root = Some(Rc::new(RefCell::new(TreeNode {
            left: None,
            right: None,
        })));
        let t: BinaryTree<i32> = BinaryTree::new(root.clone());
        assert_eq!(t.root, root);
    }

    #[rstest]
    #[case::empty(vec![], vec![])]
    #[case::only_one_element(vec![1], vec![1])]
    fn test_from(#[case] eles: Vec<i32>, #[case] want_eles: Vec<i32>) {
        let t: BinaryTree<i32> = BinaryTree::from(eles);
        let a: Vec<i32> = t.into();
        assert_eq!(a, want_eles);
    }
}
