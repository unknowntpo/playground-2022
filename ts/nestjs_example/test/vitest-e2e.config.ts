import { defineConfig } from 'vitest/config';
import { resolve } from 'path';
import swc from 'unplugin-swc';

export default defineConfig({
  plugins: [swc.vite()],
  test: {
    globals: true,
    environment: 'node',
    root: './',
    include: ['test/**/*.e2e-spec.ts'],
    exclude: ['node_modules', 'dist', 'src/**/*.spec.ts'],
    testTimeout: 30000,
    setupFiles: ['./test/setup-e2e.ts'],
  },
  resolve: {
    alias: {
      '@': resolve(__dirname, '../src'),
    },
  },
});