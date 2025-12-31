from decimal import Decimal

from py_playground.arithmetic_parser.parser import Parser, Token, Type


def test_add():
    # (1 + 2) / 3
    # 1 + 1
    p = Parser()
    res: Decimal = p.parse([Token(type=Type.Number, value="2"), Token(type=Type.Number, value="1")])
    assert res == 3