from decimal import Decimal
from enum import Enum


class Type(Enum):
    Number = "number"
    Plus = "+"


class Token:
    def __init__(self, type: Type, value: str):
        self._type = type
        self._value = value

"""


"""
class Parser:
    """
    arithemetic parser
    expr   → term ( ( "+" | "-" ) term )*
    term   → factor ( ( "*" | "/" ) factor )*
    factor → unary | NUMBER | "(" expr ")"
    unary  → ( "-" | "+" )? factor
    NUMBER → digit+ ( "." digit+ )?
    digit  → "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9"
    """

    def __init__(self):
        self._tokens: list[Token] | None = None
        self.pos = 0

    def current(self) -> Token | None:
        return self._tokens[self.pos] if len(self._tokens) > 0 and self.pos < len(self._tokens) else None
    def parse(self, tokens: list[Token])-> Decimal:
        self._tokens = tokens
        return self.term()

    def term(self) -> Decimal:
        return self.factor()

    def factor(self) -> Decimal:
        return self.unary()




