import { describe, expect, test, it } from '@jest/globals';
import { char, either, digit, sequence } from '.';

describe('char', () => {
  it('match a single character', () => {
    expect(char('a')('abc')).toEqual({
      success: true,
      value: "abc",
      rest: "bc"
    });
  })
  it('fails matching if first character does not match', () => {
    expect(char('a')('bc')).toEqual({
      success: false,
    });
  })
})

describe("either", () => {
  const aOrB = either(char("a"), char("b"));

  it("returns true if any of the combinators are true", () => {
    expect(aOrB("a")).toEqual({
      success: true,
      value: "a",
      rest: "",
    });
    expect(aOrB("b")).toEqual({
      success: true,
      value: "b",
      rest: ""
    });
  });

  it("returns false none of the combinators are true", () => {
    expect(aOrB("c")).toEqual({
      success: false
    });
  });
});


describe("digit", () => {
  it("return true when input is digit", () => {
    expect(digit("036937530354")).toEqual({
      success: true,
      value: "036937530354",
      rest: "36937530354"
    });
    expect(digit("038438593343")).toEqual({
      success: true,
      value: "038438593343",
      rest: "38438593343"
    });
  });

  it("returns false when input is not digit", () => {
    expect(digit("abcde")).toEqual({
      success: false
    });
  });
});

describe("sequence", () => {
  const aAndB = sequence(char("a"), char("b"));

  it("returns true if all of the combinators are true", () => {
    expect(aAndB("abcdef")).toEqual({
      success: true,
      value: "ab",
      rest: "cdef"
    });
  });

  it("returns false any of the combinators are false", () => {
    expect(aAndB("bcdef")).toEqual({
      success: false
    });
  });
})
