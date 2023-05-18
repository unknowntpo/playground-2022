
interface Unary<A, B> {
  (a: A): B
}

test('Map', () => {
  interface TestCase<A, B> {
    fn: Unary<A, B>;
    input: A[];
    expected: B[];
  }

  const tCases: TestCase<number, string>[] = [
    {
      fn: (num) => String(num),
      input: [1, 2, 3],
      expected: ['1', '2', '3'],
    },
    {
      fn: (str) => str.toUpperCase(),
      input: ['hello', 'world'],
      expected: ['HELLO', 'WORLD'],
    },
    {
      fn: (num) => num * 2,
      input: [1, 2, 3],
      expected: [2, 4, 6],
    },
  ];

  tCases.forEach((testCase) => {
    const { fn, input, expected } = testCase;
    expect(map(fn, input)).toEqual(expected);
  });
});

function map<A, B>(fn: Unary<A, B>, list: A[]): B[] {
  const [item, ...rest] = list;
  if (!item) return [];
  return [fn(item), ...map(fn, rest)];
}
