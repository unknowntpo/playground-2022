#include <gtest/gtest.h>
#include <iostream>
#include <vector>

using namespace std;

/**
 * Definition for singly-linked list.
 * struct ListNode {
 *     int val;
 *     ListNode *next;
 *     ListNode() : val(0), next(nullptr) {}
 *     ListNode(int x) : val(x), next(nullptr) {}
 *     ListNode(int x, ListNode *next) : val(x), next(next) {}
 * };
 */
struct ListNode {
  int val;
  ListNode *next;
  ListNode() : val(0), next(nullptr) {}
  ListNode(int x) : val(x), next(nullptr) {}
  ListNode(int x, ListNode *next) : val(x), next(next) {}
};

class Solution {
public:
  ListNode *buildList(vector<int> list) {
    ListNode *head = NULL;
    ListNode **cur = &head;
    for (int n : list) {
      *cur = new ListNode(n);
      cur = &((*cur)->next);
    }
    return head;
  }
  vector<int> listToVector(ListNode *head) {
    vector<int> out;
    while (head != NULL) {
      out.push_back(head->val);
    }
    return out;
  }
  ListNode *reverseList(ListNode *head) {
    ListNode *p = NULL;
    ListNode *c = head;
    while (c != NULL) {
      ListNode *tmp = c->next;
      c->next = p;
      p = c;
      c = tmp;
    }
    return p;
  }
};

struct TestParams {
  vector<int> input;
  vector<int> want;
};

const vector<TestParams> testCases = {
    {{}, {}},
    {{1, 2, 3}, {3, 2, 1}},
    {{1}, {1}},
};

TEST(ReverseLinkedList, reverse) {
  Solution solution;

  for (const auto &testCase : testCases) {
    ListNode *head = solution.buildList(testCase.input);
    /*
    GTEST_LOG_(INFO) << "testCase: input: " << testCase.input
                     << "want: " << testCase.want << endl;
                     */

    head = solution.reverseList(head);
    vector<int> out = solution.listToVector(head);
    EXPECT_EQ(testCase.want, out);
  }
  GTEST_LOG_(INFO) << "OK" << endl;
}
