#include <stdio.h>

#define DEBUG(fmt, ...)                                                        \
  printf("[INFO](%s:%d) \t" fmt "\n", __FILE__, __LINE__, __VA_ARGS__)

int main(int argc, char *argv[]) {
  DEBUG("Hello, %s", "world");

  return 0;
}
