interface Unary<A, B>{
    (a: A): B;
}

// === utils ===

function box<A>(x: A) {
  function pipe<B>(fn: Unary<A,B>) {
    return box(fn(x));
  }
  function unwrap() {
    return x;
  }
  return {pipe, unwrap};
}

function map<A, B>(fn: Unary<A, B>) {
    return function(list: A[]) {
        const result: B[] = [];

        for (const item of list) {
            result.push(fn(item))
        }

        return result;
    }
}

// ======

interface Product {
    name: string
    sku: string
    price: number
}

interface Bill {
    products: Product[];
    total: number;
}

function displayTotal(Bill: Bill) {

}

const display = (fns: Display[]) => (bill:Bill):string => 
  box(
    flatten(
      box(fns)
        .pipe(map(applyTo(bill)))
        .pipe(filter(isNotNull))
        .unwrap()
    )
  )
  .pipe(join("\n"))
  .unwrap();

test("display", () => {
const testcase = {
    products: [
      {
        name: "乖乖(椰子口味)",
        sku: "K0132",
        price: 20,
        tags: [],
      },
      {
        name: "乖乖(椰子口味)",
        sku: "K0132",
        price: 20,
        tags: [],
      },
      {
        name: "乖乖(椰子口味)",
        sku: "K0132",
        price: 20,
        tags: [],
      },
    ],
    total: 60,
  };

  const expected = `
- 乖乖(椰子口味)      $20.00
- 乖乖(椰子口味)      $20.00
- 乖乖(椰子口味)      $20.00
Total: $60.00
`.trim();
expect(
    display([
      // 呈現商品有哪些
      displayProducts,
      // 呈現結算後的金額
      displayTotal,
    ])(testcase)
  ).toBe(expected);
})

function main() {

}

export {};
