import { bench } from "vitest";
import { countTo } from "./counter";

bench("Multi-thread 100K", () => {
  countTo(100_000);
}, { iterations: 1000 });
