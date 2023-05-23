#include <stdio.h>

// Define the enum using the X-Macro technique
#define COLOR_ENUM                                                             \
  X(RED)                                                                       \
  X(GREEN)                                                                     \
  X(BLUE)                                                                      \
  X(YELLOW)

// Generate the enum and the conversion function declarations
#define X(color) color,
enum Colors { COLOR_ENUM };
#undef X

#define X(color) #color,
const char *color_strings[] = {COLOR_ENUM};
#undef X

// Macro to print enum literals
#define PRINT_ENUM_LITERAL(enum_var)                                           \
  printf(#enum_var " = \"%s\"\n", color_strings[enum_var])

int main() {
  // Example usage
  enum Colors color = GREEN;

  PRINT_ENUM_LITERAL(RED);
  PRINT_ENUM_LITERAL(GREEN);
  PRINT_ENUM_LITERAL(BLUE);
  PRINT_ENUM_LITERAL(color);

  return 0;
}
