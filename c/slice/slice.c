#include <stdlib.h>
#include <stdio.h>
#include <assert.h>
#include "slice.h"

slice_t *new_slice(int len, int cap)
{
    slice_t *s = (slice_t *)malloc(len * sizeof(slice_t));
    assert(s);
    s->len = len;
    s->cap = cap;
    return s;
}