/*
 * Ref: https://shengyu7697.github.io/googletest/
 * google test
 */
#include "stddef.h"
#include "vector/vector.h"
#include "gtest/gtest.h"

TEST(testCase, vector_append_normal) {
  int cap = 3;
  vector *vec = vector_new(cap);
  EXPECT_NE(vec, (vector *)(NULL));
  for (int i = 0; i < cap; i++) {
    vector_append(vec, i);
  }

  EXPECT_EQ(vector_len(vec), cap);
  EXPECT_EQ(vector_cap(vec), cap);

  for (int i = 0; i < cap; i++) {
    EXPECT_EQ(vector_get(vec, i), i);
  }
}

TEST(testCase, vector_append_resize) {
  size_t cap = 3;
  size_t wantLen = cap + 1;
  vector *vec = vector_new(cap);
  EXPECT_NE(vec, (vector *)(NULL));
  for (int i = 0; i < wantLen; i++) {
    vector_append(vec, i);
  }

  EXPECT_EQ(vector_len(vec), wantLen);
  // Resize strategry: new_cap = old_cap * 2
  EXPECT_EQ(vector_cap(vec), cap * 2);

  // GTEST_LOG_(INFO) << "len" << vec->len << std::endl;

  for (int i = 0; i < wantLen; i++) {
    EXPECT_EQ(vector_get(vec, i), i);
  }
}

// TODO: test append when len >= cap

int main(int argc, char **argv) {
  testing::InitGoogleTest(&argc, argv);
  return RUN_ALL_TESTS();
}
