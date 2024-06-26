#include "vector/vector.h"
#include "stddef.h"
#include <assert.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

struct vector
{
  int len;
  int cap;
  int *arr;
};

int vector_add(int a, int b) { return a + b; }

vector *vector_new(int cap)
{
  int *arr = malloc(cap * sizeof(int));
  if (!arr)
  {
    perror("failed on malloc on arr");
    return NULL;
  }

  vector *new = malloc(sizeof(vector));
  if (!new)
  {
    perror("failed on malloc on vector");
    return NULL;
  }

  new->len = 0;
  new->cap = cap;
  new->arr = arr;

  return new;
}

int vector_len(vector *vec) { return vec->len; }
int vector_cap(vector *vec) { return vec->cap; }

int vector_get(vector *vec, int idx)
{
  assert(idx < vec->len);
  return vec->arr[idx];
}

void vector_append(vector *vec, int element)
{
  // TODO: if cap > len then append
  // else maloc new slice then append
  // return
  //
  assert(vec);
  if (vec->len < vec->cap)
  {
    goto APPEND;
  }

  // Resize strategry: new size_t new_cap = vec->cap * 2;
  size_t new_cap = 2 * vec->cap;
  int *new_arr = malloc(new_cap * sizeof(*vec->arr));
  assert(new_arr);
  memcpy(new_arr, vec->arr, vec->cap * sizeof(*vec->arr));
  free(vec->arr);
  vec->arr = new_arr;
  vec->cap = new_cap;

APPEND:
  vec->arr[vec->len] = element;
  vec->len++;
  return;
}
