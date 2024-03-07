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

export const digit = (s: string): Combinator => {
  for (let i = 0; i < s.length;i++) {
    const code = s.charCodeAt(i);
    if (!(code >= 48 && code <= 57)) return false;
  }
  return true;
}

