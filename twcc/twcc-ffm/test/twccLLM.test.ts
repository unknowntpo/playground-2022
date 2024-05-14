import { describe, test, expect } from 'vitest'

import * as twcc from 'src/twccLLM';

describe("project tests", () => {
  test("moduleA is correct", async () => {
    expect(twcc.sum(1, 2)).toEqual(3);
  });
});
