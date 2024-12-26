import pytest

from binary_tree.tree import BinaryTree

testCases = [
    {"name": "case0", "vals": [3, 9, 20, None, None, 15, 7, None, None, None, None]},
    # {"name": "case1", "vals": [1, None,None]},
    # {"name": "3 layer, root.left has left leaf", "vals": [1,2,3, None, None,4,5]},
]


@pytest.mark.parametrize("testCase", testCases, ids=lambda testCase: testCase["name"])
def test_sum_of_left_root(testCase):
    vals = testCase["vals"]

    tree = BinaryTree.build_tree(vals)
    # tree.show_tree()
    f = lambda n: print(
        f" val: {n.val}, left: {"None" if n.left is None else n.left.val}, right: {"None" if n.right is None else n.right.val}  "
    )
    # tree.prefix_recursive_traversal(f)
    # got [3,

    """
        3
       9 20
     x x 15 7
    
    """
    tree.prefix_iter_traversal(f)
    assert tree.to_array() == vals
