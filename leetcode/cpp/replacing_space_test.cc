#include <gtest/gtest.h>
#include <iostream>

using namespace std;

class Solution {
public:
  string replaceSpace(string s) {
    int oldLen = s.size();
    int whiteSpace = countWhiteSpace(s);
    int newLen = oldLen + 2 * whiteSpace;

    s.resize(newLen);

    for (int left = oldLen - 1, right = newLen - 1; left < right;
         left--, right--) {
      if (s[left] != ' ') {
        s[right] = s[left];
      } else {
        // got white space
        s[right] = '0';
        s[right - 1] = '2';
        s[right - 2] = '%';
        right -= 2;
      }
    }
    return s;
  }

private:
  int countWhiteSpace(string s) {
    int count = 0;
    for (int i = 0; i < s.size(); i++) {
      if (s[i] == ' ') {
        count++;
      }
    }
    return count;
  }
};

/*
 * []
 * l
 *  r
 * [%20]
 *
 *    l
 *    r
 * [hi%20hi]
 */

struct TestParams {
  string input;
  string want;
};

const vector<TestParams> testCases = {
    {" ", "%20"},
    {"", ""},
    {"HelloWorld", "HelloWorld"},
    {"Hello World", "Hello%20World"},
};

TEST(ReplaceSpaceTest, replaceSpace) {
  Solution solution;

  for (const auto &testCase : testCases) {
    GTEST_LOG_(INFO) << "testCase: input: " << testCase.input << "want: " << testCase.want << endl;
    string out = solution.replaceSpace(testCase.input);
    EXPECT_EQ(testCase.want, out);
  }
}
