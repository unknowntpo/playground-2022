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
void vector_append(vector *vector, int element);
int vector_len(vector *vec);
int vector_cap(vector *vec);
int vector_get(vector *vec, int idx);
#ifdef __cplusplus
}
#endif

#endif /* __VECTOR_H */
