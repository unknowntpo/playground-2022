import { defineConfig } from 'vitest/config';
import { resolve } from 'path';

/**
 * Vitest configuration for NestJS application
 *
 * This configuration resolves ESM/CommonJS module conflicts that occur when:
 * - TypeORM tries to dynamically load entity files
 * - Dependencies have both ESM and CommonJS exports
 * - Node.js attempts to require() ESM modules
 *
 * Key solutions applied:
 * 1. Exclude node_modules from SSR optimization to prevent transformation conflicts
 * 2. Use explicit entity imports instead of dynamic loading patterns
 * 3. Ensure CommonJS module type in package.json
 * 4. Remove resolvePackageJsonExports: false from tsconfig.json
 */
export default defineConfig({
  test: {
    globals: true,
    environment: 'node',
    include: ['src/**/*.spec.ts', 'test/**/*.e2e.spec.ts'],
    exclude: ['node_modules', 'dist'],
    testTimeout: 30000,

    /**
     * Dependencies configuration to prevent ESM/CommonJS conflicts
     *
     * deps.optimizer.ssr.exclude: Excludes node_modules from SSR optimization
     * This prevents vitest from trying to transform dependencies that may have
     * mixed module formats, which was causing "Unexpected token 'export'" errors
     */
    deps: {
      optimizer: {
        ssr: {
          exclude: [/node_modules/]
        }
      }
    },
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
    pool: 'forks',
  },
  resolve: {
    alias: {
      '@': resolve(__dirname, './src'),
    },
  },
});
