import { Effect } from "effect";

const parse = (input: string) =>
  Effect.try({
    // JSON.parse may throw for bad input
    try: () => JSON.parse(input),
    // remap the error
    catch: (unknown) => new Error(`something went wrong ${unknown}`),
  });

const program = Effect.match(parse("xxx"), {
  onSuccess: (value) => console.log(`got value: ${value}`),
  onFailure: (error) => console.log(`got error: ${(error as Error).message}`),
});

Effect.runPromise(program).then((res) => console.log(res));
