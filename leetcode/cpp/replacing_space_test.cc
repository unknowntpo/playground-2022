#include <gtest/gtest.h>

use namespace std;

class Solution {
public:
  string replaceSpace(string s) { return ""; }
};

struct TestParams {
  string input;
  string want;
};

const vector<TestParams> testCases = {
    {"", ""},
    {"HelloWorld", "HelloWorld"},
    {"Hello World", "Hello%20World"},
};

TEST(ReplaceSpaceTest, TableDrive) {
  Solution solution;

  for (const auto &testCase : testCases) {
    string out = solution.replaceSpace(testCase.input);
    EXPECT_EQ(testCase.want, out);
  }
}
