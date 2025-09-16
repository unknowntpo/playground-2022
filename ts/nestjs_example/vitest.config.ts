import { defineConfig } from 'vitest/config';
import { resolve } from 'path';
import swc from 'unplugin-swc';

export default defineConfig({
  /**
   * Enable this will cause:
   * TypeError: Unknown file extension ".ts" for /Users/unknowntpo/repo/unknowntpo/playground-2022/ts/nestjs_example/src/todos/entities/todo.entity.ts
   */
  // plugins: [swc.vite()],
  test: {
    globals: true,
    environment: 'node',
    root: './',
    include: ['src/**/*.spec.ts', 'test/**/*.e2e.spec.ts'],
    exclude: ['node_modules', 'dist'],
    testTimeout: 30000,
    coverage: {
      provider: 'v8',
      reporter: ['text', 'json', 'html'],
      exclude: [
        'node_modules/',
        'dist/',
        '**/*.spec.ts',
        '**/*.e2e.spec.ts',
        '**/*.test.ts',
        '**/main.ts',
      ],
    },
    setupFiles: ['reflect-metadata', './test/setup-e2e.ts'],
  },
  resolve: {
    alias: {
      '@': resolve(__dirname, './src'),
    },
  },
});