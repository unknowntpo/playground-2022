#include "gtest/gtest.h"

TEST(HelloTest, BasicAssertions) {
  EXPECT_STRNE("hello", "world");
  EXPECT_EQ(7 * 6, 42);
}

TEST(HelloTest, Vector) {
  std::vector<int> vec;

  int len = 3;
  for (int i = 0; i < len; i++) {
    vec.push_back(i);
  }

  ASSERT_EQ(vec.size(), len);

  for (int i = 0; i < len; i++) {
    EXPECT_EQ(vec[i], i);
  }
}