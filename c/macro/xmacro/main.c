#include <stdio.h>

#define COLOR_ENUM \
X(RED) \
X(GREEN) \
X(BLUE) \
X(YELLOW)


#define X(color) color, 
enum Colors {COLOR_ENUM};
#undef X

#define X(color) #color, 
const char *color_strings[] = {COLOR_ENUM};
#undef X

#define PRINT_ENUM_LITERAL(enum_var) \
  printf(#enum_var " = \"%s\"\n",color_strings[enum_var])

int main(int argc, char *argv[])
{
  enum Colors color = YELLOW;

  PRINT_ENUM_LITERAL(RED);
  PRINT_ENUM_LITERAL(GREEN);
  PRINT_ENUM_LITERAL(BLUE);
  PRINT_ENUM_LITERAL(color);
}
