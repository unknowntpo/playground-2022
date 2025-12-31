from decimal import Decimal
from enum import Enum


class Type(Enum):
    Number = "number"
    Plus = "+"
    Minus = "-"


class Token:
    def __init__(self, type: Type, value: str):
        self.type = type
        self.value = value

"""


"""


class ParseError(Exception):
    pass

class Parser:
    """
    arithemetic parser
    expr   → term ( ( "+" | "-" ) term )*
    term   → factor ( ( "*" | "/" ) factor )*
    factor → unary | NUMBER | "(" expr ")"
    unary  → ( "-" | "+" )? factor
    NUMBER → <any valid number string from lexer>
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
        res = self.factor()
        if (current := self.current()) and current is not None and current.type in (Type.Plus, Type.Minus):
            rhr = self.term()
            res += rhr
        return res

    def factor(self) -> Decimal:
        if self.current().type == Type.Number:
            return self.number()

        return self.unary()

    def number(self) -> Decimal:
        if self.current() is None:
            raise ParseError(f"current token should not be None, pos: {self.pos}")
        d = Decimal(self.current().value)
        self.pos += 1
        return d


