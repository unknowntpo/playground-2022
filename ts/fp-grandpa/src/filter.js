"use strict";
exports.__esModule = true;
function display(Bill) {
}
test("display", function () {
    var testcase = {
        products: [
            {
                name: "乖乖(椰子口味)",
                sku: "K0132",
                price: 20,
                tags: []
            },
            {
                name: "乖乖(椰子口味)",
                sku: "K0132",
                price: 20,
                tags: []
            },
            {
                name: "乖乖(椰子口味)",
                sku: "K0132",
                price: 20,
                tags: []
            },
        ],
        total: 60
    };
    var expected = "\n- \u4E56\u4E56(\u6930\u5B50\u53E3\u5473)      $20.00\n- \u4E56\u4E56(\u6930\u5B50\u53E3\u5473)      $20.00\n- \u4E56\u4E56(\u6930\u5B50\u53E3\u5473)      $20.00\nTotal: $60.00\n".trim();
});
function main() {
}
