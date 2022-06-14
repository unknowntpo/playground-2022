#include <stdio.h>
#include <stdarg.h>

void foo(int len, double *params);
void foo_va(int len, ...);

int main(void)
{
    double x = 1.1, y = 2.1, z = 3.9;
    double a = 0.1, b = 0.2, c = 0.3;

    puts("3 params");
    foo(3, (double[]){x, y, z});

    puts("6 params");
    foo(6, (double[]){x, y, z, a, b, c});

    puts("va args 3 params");
    foo_va(3, x, y, z);

    puts("va args 6 params");

    foo_va(6, x, y, z, a, b, c);

    return 0;
}

void foo(int len, double *params)
{
    for (int i = 0; i < len; i++)
        printf("%.1f\n", params[i]);
}

void foo_va(int len, ...)
{
    va_list args;
    va_start(args, len);

    for (int i = 0; i < len; i++)
        printf("%.1f\n", va_arg(args, double));

    va_end(args);
}
