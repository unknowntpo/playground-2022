interface SuccessResult {
  success: true;
  value: string;
  rest: string;
}

interface FailureResult {
  success: false;
}

type CombinatorResult = SuccessResult | FailureResult;
type Combinator = (str: string) => CombinatorResult;

export const char = (c: string): Combinator => {
  return (str: string) => {
    if (str[0] === c) {
      return { success: true, value: str, rest: str.substring(1) }
    } else {
      return { success: false }
    }
  }
}

export const either = (...combinators: Combinator[]): Combinator => {
  return (s: string) => {
    for (let i = 0; i < combinators.length; i++) {
      const res = combinators[i](s);
      if (res.success) {
        return res;
      }
    }
    return { success: false };
  }
}

export function sequence(...combinators: Combinator[]): Combinator {
  return (str: string) => {
    let rest = str;
    let value = "";

    for (let i = 0; i < combinators.length; i++) {
      const result = combinators[i](rest);
      if (result.success) {
        rest = result.rest;
        value += result.value;
      } else {
        return { success: false };
      }
    }

    return {
      success: true,
      value,
      rest
    };
  };
}

export const digit = either(..."0123456789".split('').map(char));
export const hexDigit = either(digit, ..."abcdefABCDEF".split('').map(char));
