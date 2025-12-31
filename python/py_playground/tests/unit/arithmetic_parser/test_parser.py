from decimal import Decimal

from py_playground.arithmetic_parser.parser import Parser, Token, Type


def test_single_number():
    p = Parser()
    res: Decimal = p.parse([Token(type=Type.Number, value="2")])
    assert res == 2

def test_add_two_numbers():
    p = Parser()
    res: Decimal = p.parse([Token(type=Type.Number, value="2"), Token(type=Type.Plus, value="+"), Token(type=Type.Number, value="3")])
    assert res == 5