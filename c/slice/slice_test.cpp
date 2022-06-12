#include <gtest/gtest.h>
#include "slice.h"

TEST(testCase, test_new_slice)
{
    slice_t *s = new_slice(3, 0);
    EXPECT_EQ(s->len, 3);
    EXPECT_EQ(s->cap, 0);
}

int main(int argc, char **argv)
{
    testing::InitGoogleTest(&argc, argv);
    return RUN_ALL_TESTS();
}