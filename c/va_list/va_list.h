#ifndef VA_LIST_H
#define VA_LIST_H

#include <stdarg.h>
#include <stdint.h>
#include <stdio.h>

typedef struct {
    uint32_t len;
    int *b;
} buf_t;

buf_t *new_buf(int cap);
void show_buf(buf_t *buf);

buf_t *foo(int len, int *params);
buf_t *foo_va(int len, ...);
void print_positive_ints(int, ...);
void foo_va_no_len(int first, ...);

#endif /* VA_LIST_H */
