#ifndef __VECTOR_H
#define __VECTOR_H

// for cpp
// Ref: https://hackmd.io/@rhythm/HyOxzDkmD
#ifdef __cplusplus
extern "C" {
#endif

int vector_add(int a, int b);

typedef struct vector vector;

vector *vector_new(int length, int cap);

#ifdef __cplusplus
}
#endif

#endif /* __VECTOR_H */
