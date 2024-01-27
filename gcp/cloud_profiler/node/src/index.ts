import * as profiler from '@google-cloud/profiler';

const sleep = (ms: number) => new Promise((r) => setTimeout(r, ms))

const slowFunc = async () => {
  await sleep(100)
  await slowFunc2()
}

const slowFunc2 = async () => {
  await sleep(300)
  await slowFunc3()
}

const slowFunc3 = async () => {
  await sleep(500)
  for (let i = 0; i < 500; i++) {
    console.log("Done")
  }
}



async function main() {
  await profiler.start({
    projectId: 'web-service-design',
    serviceContext: {
      service: 'slowFuncService',
      version: '1.0.0',
    },
  });
  for (let i = 0; i < 500; i++) {
    await slowFunc()
    console.log("Done")
  }
}

main()
