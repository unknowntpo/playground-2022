/*
 * Ref: https://shengyu7697.github.io/googletest/
 * google test
 */
#include "./vector.h"
#include "stddef.h"
#include <gtest/gtest.h>

TEST(testCase, test1) { EXPECT_EQ(vector_add(2, 3), 5); }

TEST(testCase, vector_new_cap_should_be_gte_than_length) {
  EXPECT_EQ(vector_new(2, 1), (vector *)(NULL));
}

TEST(testCase, vector_append_normal) {
  int cap = 3;
  vector *vec = vector_new(0, cap);
  EXPECT_NE(vec, (vector *)(NULL));
  for (int i = 0; i < cap; i++) {
    vector_append(vec, i);
  }

  EXPECT_EQ(vector_len(vec), cap);
  EXPECT_EQ(vector_cap(vec), cap);

  // for (int i = 0; i < cap; i++) {
  //   EXPECT_EQ(vec, i);
  // }
}

int main(int argc, char **argv) {
  testing::InitGoogleTest(&argc, argv);
  return RUN_ALL_TESTS();
}
