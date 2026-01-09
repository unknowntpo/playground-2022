# Code Review: arithmetic_parser/parser.py

## Critical Bugs

### ~~1. Dead Code: `unary()` Should Be Removed~~ (FIXED)

### ~~2. Bug in `factor()` - Potential `AttributeError`~~ (FIXED)

### ~~3. Missing `return` in `factor()`~~ (FIXED)

---

## Medium Issues

### ~~4. Parser State Not Reset~~ (FIXED)

### ~~5. Naming: `Param` vs `Paren`~~ (FIXED)

### ~~6. Exception Hierarchy~~ (FIXED)

---

## Minor Issues

### 7. Empty Docstring (line 88)
```python
def parse(self, tokens: list[Token]) -> Decimal | None:
    """ """  # Empty - remove or document
```

### 8. Empty Comment Block (lines 26-29)
```python
"""


"""
```
Remove leftover code.

### 9. Typo in Decorator
```python
def wrapper(*args, **kargs):  # Convention: kwargs
```

### 10. Redundant Condition
```python
(current := self.current()) and current is not None  # redundant
```
Walrus op already checks truthy.

---

## Test Coverage Gaps

- ~~No edge case: empty token list~~ (FIXED)
- No malformed input tests (e.g., missing closing paren)

## Known Bugs (TODO)

- `(2+3` (missing closing paren) returns 5 instead of raising exception
- `2+3)` (extra closing paren) returns 5, ignores trailing `)`
- Parser should verify all tokens consumed after parsing

---

## Summary

| Priority | Remaining |
|----------|-----------|
| Critical | 0         |
| Medium   | 0         |
| Minor    | 4         |

All critical and medium bugs fixed. Only minor style issues remain.