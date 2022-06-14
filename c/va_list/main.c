#include <stdio.h>

void foo(int len, double *params);

int main(void)
{
    double x = 1.1, y = 2.1, z = 3.9;
    double a = 0.1, b = 0.2, c = 0.3;

    puts("3 params");
    foo(3, (double[]){x, y, z});

    puts("6 params");
    foo(6, (double[]){x, y, z, a, b, c});

    return 0;
}

void foo(int len, double *params)
{
    for (int i = 0; i < len; i++)
        printf("%.1f\n", params[i]);
}
