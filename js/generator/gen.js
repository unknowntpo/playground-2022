const gen = function* () {
  let id = 1;
  while (true) {
    yield id;
    id++;
  }
};

console.log(gen().next());
console.log(gen().next());

let limit = 10;
for (id of gen()) {
  if (id > limit) break;
  console.log(id);
}
