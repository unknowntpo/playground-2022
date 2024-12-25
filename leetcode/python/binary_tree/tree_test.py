import pytest

from binary_tree.tree import BinaryTree

testCases = [
    {"name": "case0", "vals": [3,9,20, None, None, 15, 7, None, None, None, None]},
    # {"name": "case1", "vals": [1, None,None]},
    # {"name": "3 layer, root.left has left leaf", "vals": [1,2,3, None, None,4,5]},
]

@pytest.mark.parametrize("testCase", testCases, ids=lambda testCase: testCase["name"])
def test_sum_of_left_root(testCase):
    vals = testCase["vals"]

    tree = BinaryTree.build_tree(vals)
    BinaryTree.show_tree(tree)
    assert tree.to_array() == vals
