async function getData() {
  const response = await fetch('https://jsonplaceholder.typicode.com/todos/1');
  const data = await response.json();
  return data;
}

function doWorkPromise() {
  return new Promise((resolve) => setTimeout(() => {
    resolve("OK")
  }, 1000))
}

doWorkPromise().then(msg => console.log(msg))

getData().then(data => console.log(data));
