import functools
import logging
from decimal import Decimal
from enum import Enum


class Type(Enum):
    Number = "number"
    Plus = "+"
    Minus = "-"
    Mult = "*"
    Div = "/"
    LeftParen = "("
    RightParen = ")"


class Token:
    def __init__(self, type: Type, value: str):
        self.type = type
        self.value = value

    def __repr__(self) -> str:
        return f"Token(type={self.type},value={self.value})"


"""


"""


def record(func):
    @functools.wraps(func)
    def wrapper(*args, **kargs):
        try:
            logging.info(
                f"before {func.__name__!r}, pos={args[0].pos!r}, kargs={kargs!r}"
            )
            res = func(*args, **kargs)
            logging.info(f"after {func.__name__!r}, pos={args[0].pos!r}, return={res}")
            return res
        except Exception as e:
            logging.exception(f"got exception during calling {func.__name__!r}: {e}")
            raise e

    return wrapper


class UnexpectedTokenException(Exception):
    pass


class ZeroDivisionException(Exception):
    pass


class Parser:
    """Arithmetic expression parser using recursive descent.

    Grammar (EBNF):
        expr   → term ( ( "+" | "-" ) term )*
        term   → factor ( ( "*" | "/" ) factor )*
        factor → NUMBER | "(" expr ")"
        NUMBER → <any valid number string from lexer, including signed like "-2">

    Examples:
        expr:   "1 + 2", "3 + 4 - 5", "1"
        term:   "2 * 3", "6 / 2 * 3", "7"
        factor: "42", "(1 + 2)", "-5"
        NUMBER: "123", "3.14", "-2"
    """

    def __init__(self):
        self._tokens: list[Token] | None = None
        self.pos = 0

    def current(self) -> Token | None:
        return (
            self._tokens[self.pos]
            if len(self._tokens) > 0 and self.pos < len(self._tokens)
            else None
        )

    @record
    def parse(self, tokens: list[Token]) -> Decimal | None:
        """ """
        logging.info(f"parsing: {tokens}")
        if len(tokens) == 0:
            return Decimal(0)

        self._tokens = tokens
        self.pos = 0
        return self.expr()

    @record
    def expr(self) -> Decimal | None:
        res = self.term()
        while (
            (current := self.current())
            and current is not None
            and current.type in (Type.Plus, Type.Minus)
        ):
            logging.info(f"got {current.type} in expr() at pos={self.pos}")
            self.pos += 1
            rhr = self.term()
            match current.type:
                case Type.Plus:
                    res += rhr
                case Type.Minus:
                    res -= rhr
                case _:
                    raise UnexpectedTokenException(f"unexpected token {current}")
        return res

    @record
    def term(self) -> Decimal | None:
        res = self.factor()
        while (
            (current := self.current())
            and current is not None
            and current.type in (Type.Mult, Type.Div)
        ):
            logging.info(f"got {current.type} in term() at pos={self.pos}")
            self.pos += 1
            rhr = self.factor()
            match current.type:
                case Type.Mult:
                    res *= rhr
                case Type.Div:
                    if rhr == 0:
                        raise ZeroDivisionException(
                            f"can not divided by 0, res: {res}, rhr: {rhr}, pos: {self.pos}"
                        )
                    res /= rhr
                case _:
                    raise UnexpectedTokenException(f"unexpected token {current}")
        return res

    @record
    def factor(self) -> Decimal | None:
        if (current := self.current()) and current is None:
            raise UnexpectedTokenException(
                f"current token should not be None, pos: {self.pos}"
            )
        elif current.type == Type.Number:
            return self.number()
        elif current.type == Type.LeftParen:
            # (expr)
            logging.info(f"in factor(), got {current} at pos {self.pos}")
            self.pos += 1
            expr = self.expr()
            if (current := self.current()) and current.type is not Type.RightParen:
                raise UnexpectedTokenException(
                    f"current should be {Type.RightParen} at pos {self.pos}, got {current}"
                )
            logging.info(f"in factor(), got {current} at pos {self.pos}")
            self.pos += 1
            return expr
        raise UnexpectedTokenException(f"expect a {Type.RightParen} at pos {self.pos}, but got {self.current()}")

    @record
    def number(self) -> Decimal | None:
        if self.current() is None:
            raise UnexpectedTokenException(
                f"current token should not be None, pos: {self.pos}"
            )
        d = Decimal(self.current().value)
        logging.info(f"in number(), got {d} at pos {self.pos}")
        self.pos += 1
        return d
