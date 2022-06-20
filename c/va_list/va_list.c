#include "va_list.h"
#include <assert.h>
#include <stdarg.h>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>

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

buf_t *new_buf(int cap)
{
    buf_t *buf = malloc(sizeof(buf_t));
    assert(buf);
    buf->len = 0;
    buf->b = malloc(cap * sizeof(double));
    assert(buf);

    return buf;
}

buf_t *foo(int len, double *params)
{
    // buf_t *buf = malloc(sizeof(buf_t));
    // buf->len = 0;
    // buf->b = malloc(len * sizeof(double));
    buf_t *buf = new_buf(len);
    for (int i = 0; i < len; i++) {
        printf("%.1f\n", params[i]);
        // append output to buf
        buf->b[i] = params[i];
        buf->len++;
    }
    return buf;
}

void foo_va(int len, ...)
{
    va_list args;
    va_start(args, len);

    for (int i = 0; i < len; i++)
        printf("%.1f\n", va_arg(args, double));

    va_end(args);
}

void foo_va_no_len(double first, ...)
{
    va_list args;
    va_start(args, first);

    for (double arg = first; arg > 0; arg = va_arg(args, double))
        printf("%.1f\n", arg);

    va_end(args);
}

void print_positive_ints(int first, ...)
{
    va_list args;
    va_start(args, first);

    for (int arg = first; arg > 0; arg = va_arg(args, int)) {
        printf("%d\n", arg);
    }

    va_end(args);
}
