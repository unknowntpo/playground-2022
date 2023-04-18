#include "./vector.h"
#include "stddef.h"
#include <assert.h>
#include <stdio.h>
#include <stdlib.h>

struct vector {
  int len;
  int cap;
  int *arr;
};

int vector_add(int a, int b) { return a + b; }

vector *vector_new(int length, int cap) {
  if (cap < length) {
    perror("cap should be greater or equal than length");
    return NULL;
  }
  int *arr = malloc(cap * sizeof(int));
  if (!arr) {
    perror("failed on malloc on arr");
    return NULL;
  }

  vector *new = malloc(sizeof(vector));
  if (!new) {
    perror("failed on malloc on vector");
    return NULL;
  }

  new->len = length;
  new->cap = cap;
  new->arr = arr;

  return new;
}
