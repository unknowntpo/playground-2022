#include <stdio.h>

#define MAX_NAME 10

typedef struct person person;

struct person {
  int id;
  char name[MAX_NAME];
};

int main(int argc, char *argv[]) {
  person p1 = {.id = 1, .name = "eric"};
  person p2 = {.id = 1, .name = "kevin"};

  printf("p1.id = %d, p1.name = %s\n", p1.id, p1.name);
  printf("p2.id = %d, p2.name = %s\n", p2.id, p2.name);
  return 0;
}
