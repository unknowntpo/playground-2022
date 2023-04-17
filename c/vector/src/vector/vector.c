#include "./vector.h"
#include "stddef.h"

struct vector {
  int len;
  int cap;
  void *ptr;
};

int vector_add(int a, int b) { return a + b; }

vector *vector_new(int length) { return NULL; }
