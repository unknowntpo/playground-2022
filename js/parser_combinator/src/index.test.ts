import { describe, expect, test, it } from '@jest/globals';
import { char, either } from '.';

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
