#include "gtest/gtest.h"

extern "C"{
    #include "testy/customer.h"
}

TEST(customer, ok) {
    ASSERT_EQ(customer_check(5), 1);
}

TEST(customer, not_ok) {
    ASSERT_EQ(customer_check(0), 0);
}
