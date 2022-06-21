#include "va_list.h"
#include <assert.h>
#include <stdarg.h>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>

buf_t *new_buf(int cap)
{
    buf_t *buf = malloc(sizeof(buf_t));
    assert(buf);
    buf->len = 0;
    buf->b = malloc(cap * sizeof(int));
    assert(buf);

    return buf;
}

void show_buf(buf_t *buf)
{
    for (int i = 0; i < buf->len; i++)
        printf("buf[%d]: %d\n", i, buf->b[i]);
}

buf_t *foo(int len, int *params)
{
    // buf_t *buf = malloc(sizeof(buf_t));
    // buf->len = 0;
    // buf->b = malloc(len * sizeof(int));
    buf_t *buf = new_buf(len);
    for (int i = 0; i < len; i++) {
        printf("%d\n", params[i]);
        // append output to buf
        buf->b[i] = params[i];
        buf->len++;
    }
    return buf;
}

buf_t *foo_va(int len, ...)
{
    buf_t *buf = new_buf(len);
    va_list args;
    va_start(args, len);

    for (int i = 0; i < len; i++) {
        buf->b[i] = va_arg(args, int);
        buf->len++;
        printf("%d\n", buf->b[i]);
    }

#ifndef NDEBUG
    show_buf(buf);
#endif

    va_end(args);
    return buf;
}

void foo_va_no_len(int first, ...)
{
    va_list args;
    va_start(args, first);

    for (int arg = first; arg > 0; arg = va_arg(args, int))
        printf("%d\n", arg);

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
