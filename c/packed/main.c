#include <stdio.h>

struct MyStruct {
  char a;
  int b;
  char c;
} __attribute__((__packed__)); // Specify that the struct should be packed
                               //
int main() {
  printf("Size of MyStruct: %ld\n", sizeof(struct MyStruct));

  return 0;
}
