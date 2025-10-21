#!/usr/bin/env python3
"""
Simple script to perform addition of two numbers.
"""

def add(a, b):
    """Add two numbers and return the result."""
    return a + b


if __name__ == "__main__":
    # Example usage
    a = 5
    b = 3
    result = add(a, b)
    print(f"{a} + {b} = {result}")

    # You can also use it with different values
    x = 10
    y = 20
    result2 = add(x, y)
    print(f"{x} + {y} = {result2}")
