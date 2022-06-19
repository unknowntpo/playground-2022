#ifndef VA_LIST_H
#define VA_LIST_H

#include <stdarg.h>
#include <stdint.h>
#include <stdio.h>

typedef struct {
    uint32_t len;
    double *b;
} buf_t;

buf_t *foo(int len, double *params);
void foo_va(int len, ...);
void print_positive_ints(int, ...);
void foo_va_no_len(double first, ...);

#endif /* VA_LIST_H */
