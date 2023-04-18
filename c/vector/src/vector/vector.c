#include "./vector.h"
#include "stddef.h"
#include <assert.h>
#include <stdlib.h>

struct vector {
  int len;
  int cap;
  void *ptr;
};

int vector_add(int a, int b) { return a + b; }

vector *vector_new(int length, int cap) {
  vector *new = malloc(sizeof(vector));
  assert(new);

  new->len = length;
  new->cap = cap;

  return new;
}
