package main

import (
	. "github.com/onsi/ginkgo/v2"
	. "github.com/onsi/gomega"
)

// const Right = x => ({
//     chain: f => f(x),
//     map: f => Right(f(x)),
//     fold: (f, g) => g(x),
//     toString: () => `Right(${x})`
// })

type Fn func()

type Result any
type monad interface {
	Chain(f Fn) monad
	Map(f Fn) monad
	Fold(f, g Fn) Result
	ToString(func() string)
}

// const Right = func(addr Address) {

// }

// const Left = x => ({
//     chain: f => Left(x),
//     map: f => Left(x),
//     fold: (f, g) => f(x),
//     toString: () => `Left(${x})`
// })

// const fromNullable = x =>
//     x != null ? Right(x) : Left(null)

// const street = user =>
//     fromNullable(user.address)
//         .map(address => address.street)
//         .fold(() => 'no street', x => x)

// describe("monad", () => {
//     test("local street", () => {
//         expect(
//             street(
//                 // user
//                 {
//                     name: "Bob",
//                     address: {
//                         street: "local street"
//                     },
//                 },
//             ),
//         ).toEqual("local street")
//     })

//     test("no street", () => {
//         expect(
//             street(
//                 // user
//                 {
//                     name: "Alice",
//                 },
//             ),
//         ).toEqual("no street")
//     })
// })

type Address struct {
	Street string
}

type User struct {
	Name string
	Addr Address
}

var _ = Describe("monad", func() {
	Context("local street", func() {
		When("input is local street", func() {
			// expect(
			//             street(
			//                 // user
			//                 {
			//                     name: "Bob",
			//                     address: {
			//                         street: "local street"
			//                     },
			//                 },
			//             ),
			//         ).toEqual("local street")
			It("should return correct result", func() {
				// Write your test case here
				Expect(street(User{Name: "Bob", Addr: Address{Street: "local street"}})).To(Equal("local street"))
			})
		})
	})
})
