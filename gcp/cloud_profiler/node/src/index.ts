import * as profiler from '@google-cloud/profiler';
import { Hono } from 'hono';
import { serve } from '@hono/node-server'
import { logger } from 'hono/logger';

const sleep = (ms: number) => new Promise((r) => setTimeout(r, ms))

const slowFunc = (n: number) => slowFunc2(n);

const slowFunc2 = (n: number) => slowFunc3(n);

const slowFunc3 = (n: number) => fib(n)

const fib = (n: number): number => {
  if (n <= 0) return 0;
  if (n <= 1) return n;
  return fib(n - 1) + fib(n - 2);
}

async function main() {
  console.log(`test: ${process.env.TEST}`)
  console.log(`process.env.GCP_CLIENT_EMAIL: ${process.env.GCP_CLIENT_EMAIL}`)
  console.log(`process.env.GCP_PRIVATE_KEY: ${process.env.GCP_PRIVATE_KEY}`)
  await profiler.start({
    projectId: process.env.PROJECT_ID,
    serviceContext: {
      service: 'package-json-image',
      version: '1.0.0',
    },
    credentials: {
      client_email: process.env.GCP_CLIENT_ID,
      private_key: process.env.GCP_CLIENT_SECRET
    },
    logLevel: 4,
  });
  const app = new Hono();
  app.use('*', logger());
  app.get('/', (c) => c.text('Hono🔥'));
  app.get('/slow/:n', async (c) => {
    const n = Number(c.req.param('n'));
    const res = slowFunc(n);
    return c.text(`fib(${n}) = ${res}`);
  });

  serve({ fetch: app.fetch, port: 4444 })
}

main()
