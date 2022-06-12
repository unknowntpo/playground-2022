#ifndef __SLICE_H
#define __SLICE_H

typedef struct
{
    int len;
    int cap;
    int *arr;
} slice_t;

slice_t *new_slice(int len, int cap);

#endif /* __SLICE_H */