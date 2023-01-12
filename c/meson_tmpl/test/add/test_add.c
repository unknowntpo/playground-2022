#include <glib.h>
#include <module/add.h>

int main(int argc, char **argv)
{
    g_assert_cmpint(3, ==, myad(1, 2));
}