/*
 * Ref: https://shengyu7697.github.io/googletest/
 * google test
 */
#include "./vector.h"
#include "stddef.h"
#include <gtest/gtest.h>

TEST(testCase, test1) { EXPECT_EQ(vector_add(2, 3), 5); }

TEST(testCase, vector_cap_should_be_gte_than_length) {
  EXPECT_EQ(vector_new(2, 1), (vector *)(NULL));
}

int main(int argc, char **argv) {
  testing::InitGoogleTest(&argc, argv);
  return RUN_ALL_TESTS();
}