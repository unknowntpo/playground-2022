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

type Fn func(x any) any

type monad interface {
	Chain(f Fn) any
	Map(f Fn) monad
	Fold(f, g Fn) any
	ToString(func() string) string
}

type Right struct{ X any }

func (r Right) Chain(f Fn) any {
	return f(r.X)
}

func (r Right) Map(f Fn) monad {
	return Right{X: f(r.X)}
}

func (r Right) Fold(f, g Fn) any {
	return g(r.X)
}

func (r Right) ToString(f func() string) string {
	return f()
}

// const Left = x => ({
//     chain: f => Left(x),
//     map: f => Left(x),
//     fold: (f, g) => f(x),
//     toString: () => `Left(${x})`
// })

type Left struct{ X any }

func (l Left) Chain(f Fn) any {
	return Left{l.X}
}

func (l Left) Map(f Fn) monad {
	return Left{X: f(l.X)}
}

func (l Left) Fold(f, g Fn) any {
	return f(l.X)
}

func (l Left) ToString(f func() string) string {
	return f()
}

var fromNullable = func(x any) monad {
	if x != nil {
		return Right{x}
	}
	return Left{x}
}

var street = func() {

}

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
