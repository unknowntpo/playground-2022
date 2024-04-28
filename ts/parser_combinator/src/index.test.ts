import { describe, expect, test, it } from '@jest/globals';
import { char, either, digit } from '.';

describe('char', () => {
  it('match a single character', () => {
    expect(char('a')('abc')).toBeTruthy();
  })
  it('fails matching if first character does not match', () => {
    expect(char('a')('bc')).toBeFalsy();
  })
})

describe("either", () => {
  const aOrB = either(char("a"), char("b"));

  it("returns true if any of the combinators are true", () => {
    expect(aOrB("a")).toBeTruthy();
    expect(aOrB("b")).toBeTruthy();
  });

  it("returns false none of the combinators are true", () => {
    expect(aOrB("c")).toBeFalsy();
  });
});


describe("digit", () => {
  it("return true when input is digit", () => {
    expect(digit("036937530354")).toBeTruthy();
    expect(digit("038438593343")).toBeTruthy();
  });

  it("returns false when input is not digit", () => {
    expect(digit("abcde")).toBeFalsy();
  });
});
