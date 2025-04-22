import { defineConfig } from "vitest/config";

export default defineConfig({
  test: {
    poolOptions: {
      threads: {
        // For single-thread comparison
        singleThread: process.env.THREADS === "single",
      },
    },
  },
});
