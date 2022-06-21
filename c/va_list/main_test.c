#include <math.h>
#include "minunit.h"
#include "va_list.h"

char *test_hello()
{
    return TEST_PASS;
}

char *test_foo()
{
    int x = 1, y = 2, z = 3;
    buf_t *buf;
    buf = foo(3, (int[]){x, y, z});
    mu_assert(buf->len == 3, "length of buf should be 3");
    mu_assert(buf->b[0] == x, "should be equal");
    mu_assert(buf->b[1] == y, "should be equal");
    mu_assert(buf->b[2] == z, "should be equal");

    return TEST_PASS;
}

char *test_foo_va()
{
    int x = 1, y = 2, z = 3;
    buf_t *buf;
    buf = foo_va(3, x, y, z);
    mu_assert(buf->len == 3, "length of buf should be 3");
    mu_assert(buf->b[0] == x, "should be equal");
    mu_assert(buf->b[1] == y, "should be equal");
    mu_assert(buf->b[2] == z, "should be equal");

    return TEST_PASS;
}

char *all_tests()
{
    mu_suite_start();
    mu_run_test(test_hello);
    mu_run_test(test_foo);
    mu_run_test(test_foo_va);

    return TEST_PASS;
}

// int main(void)
// {
//     double x = 1.1, y = 2.1, z = 3.9;
//     double a = 0.1, b = 0.2, c = 0.3;

//     puts("3 params");
//     foo(3, (double[]){x, y, z});

//     puts("6 params");
//     foo(6, (double[]){x, y, z, a, b, c});

//     puts("va args 3 params");
//     foo_va(3, x, y, z);

//     puts("va args 6 params");

//     foo_va(6, x, y, z, a, b, c);

//     puts("print positive ints");
//     print_positive_ints(1, 2, 3, 4, 5, -1);

//     puts("va args no len: ");

//     foo_va_no_len(6.2, x, y, z, a, b, c);

//     return 0;
// }

RUN_TESTS(all_tests);