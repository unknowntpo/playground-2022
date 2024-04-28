type Combinator = (str: string) => boolean;

export const char = (c: string): Combinator => {
  return (str: string) => {
    return (str[0] === c);
  }
}

export const either = (...combinators: Combinator[]): Combinator => {
  return (s: string) => {
    for (let i = 0; i < combinators.length; i++) {
      if (combinators[i](s)) {
        return true;
      }
    }
    return false;
  }
}

export const digit = either(..."0123456789".split('').map(char));
export const hexDigit = either(digit, ..."abcdefABCDEF".split('').map(char));
