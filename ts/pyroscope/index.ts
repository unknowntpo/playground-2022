import * as Pyroscope from '@pyroscope/nodejs';

const slowFunc = async () => {
  await sleep(100)
  slowFunc2()
}

const slowFunc2 = async () => {
  await sleep(300)
  slowFunc3()
}

const slowFunc3 = async () => {
  await sleep(500)
  for (let i = 0; i < 500; i++) {
    console.log("Done")
  }
}


const sleep = (ms: number) => new Promise((r) => setTimeout(r, ms))

async function main() {
  Pyroscope.init({
    serverAddress: 'http://localhost:4040',
    appName: 'myNodeService'
  });

  Pyroscope.start()

  for (let i = 0; i < 500; i++) {
    await slowFunc()
    console.log("Done")
  }
}

main()
