from decimal import Decimal

import pytest

from py_playground.arithmetic_parser.parser import (
    Parser,
    Token,
    Type,
    ZeroDivisionException,
)


def test_single_number():
    p = Parser()
    res: Decimal = p.parse([Token(type=Type.Number, value="2")])
    assert res == 2


# fmt:off
@pytest.mark.parametrize("tokens,expected", [
    pytest.param([Token(Type.Number, "2"), Token(Type.Plus, "+"), Token(Type.Number, "3")], 5, id="2+3"),
    pytest.param([Token(Type.Number, "2"), Token(Type.Minus, "-"), Token(Type.Number, "3")], -1, id="2-3"),
    pytest.param([Token(Type.Number, "2"), Token(Type.Minus, "-"), Token(Type.Number, "3"), Token(Type.Plus, "+"), Token(Type.Number, "1")], 0, id="2-3+1"),
    pytest.param([Token(Type.Number, "22"), Token(Type.Minus, "-"), Token(Type.Number, "3")], 19, id="22-3"),
])
# fmt:on
def test_add_and_subtract(tokens, expected):

    p = Parser()
    res: Decimal = p.parse(tokens)
    assert res == expected

# fmt:off
@pytest.mark.parametrize("tokens,expected", [
    pytest.param([Token(Type.Number, "2"), Token(Type.Mult, "*"), Token(Type.Number, "3")], 6, id="2*3"),
    pytest.param([Token(Type.Number, "3"), Token(Type.Div, "/"), Token(Type.Number, "2")], 1.5, id="3/2"),
    pytest.param([Token(Type.Number, "5"), Token(Type.Mult, "*"), Token(Type.Number, "4"), Token(Type.Div, "/"), Token(Type.Number, "2")], 10, id="5*4/2"),
    pytest.param([Token(Type.Number, "22"), Token(Type.Div, "/"), Token(Type.Number, "0")], ZeroDivisionException, id="22/0"),
])
# fmt:on
def test_mult_and_div(tokens, expected):

    p = Parser()
    if isinstance(expected, type) and issubclass(expected, Exception):
        with pytest.raises(expected) as excinfo:
            p.parse(tokens)
        assert excinfo.type is expected
    else:
        res: Decimal = p.parse(tokens)
        assert res == expected

# fmt:off
@pytest.mark.parametrize("tokens,expected", [
    pytest.param([Token(Type.LeftParam, "("), Token(Type.Number, "2"), Token(Type.Plus, "+"), Token(Type.Number, "3"), Token(Type.RightParam, ")"), Token(Type.Mult, "*"), Token(Type.Number, "2")], 10, id="(2+3)*2"),
    pytest.param([Token(Type.LeftParam, "("), Token(Type.Number, "4"), Token(Type.Mult, "*"), Token(Type.LeftParam, "("), Token(Type.Number, "2"), Token(Type.Plus, "+"), Token(Type.Number, "3"), Token(Type.RightParam, ")"), Token(Type.RightParam, ")"), Token(Type.Mult, "*"), Token(Type.Number, "2")], 40, id="(4*(2+3))*2"),
])
# fmt:on
def test_params(tokens, expected):

    p = Parser()
    if isinstance(expected, type) and issubclass(expected, Exception):
        with pytest.raises(expected) as excinfo:
            p.parse(tokens)
        assert excinfo.type is expected
    else:
        res: Decimal = p.parse(tokens)
        assert res == expected
