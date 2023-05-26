#include <stdio.h>

#define st(val) #val

const char *colors[] = {st(RED), st(BLUE), st(GREEN)};

int main(int argc, char *argv[]) {
  printf(st(abc) "\n");
  for (int i = 0; i < 3; i++) {
    printf("%s\t", colors[i]);
  }

  return 0;
}
