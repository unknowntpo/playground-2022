#include <math.h>
#include "minunit.h"
#include "va_list.h"

int main()
{
    int x = 1, y = 2, z = 3;
    buf_t *buf;
    buf = foo(3, (int[]){x, y, z});
    show_buf(buf);

    return 0;
}