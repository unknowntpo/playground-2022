#include "add.h"
#include "stdio.h"

struct data
{
    short a;
    char b;
    double c;
};

int main()
{
    printf("myadd.c: %d\n", myadd(1, 2));

    struct data x = {.a = 25, .b = 'A', .c = 12.45};
    char *p = (char *)&x;
    printf("a=%d\n", *(short *)p);
    // go to next struct field: b
    p += sizeof(short);
    printf("b=%c\n", *(char *)p);

    // go to next struct field: c
    p += sizeof(char);
    printf("b=%c\n", *(double *)p);
    return 0;
}