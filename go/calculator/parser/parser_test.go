package parser

import (
	"fmt"
	"testing"

	. "github.com/onsi/ginkgo/v2"
	. "github.com/onsi/gomega"
)

func TestParser(t *testing.T) {
	RegisterFailHandler(Fail)
	RunSpecs(t, "parser test suites")
}

var _ = Describe("parser", func() {
	var (
		p   *Parser
		res *Result
	)
	When("parser.Parse is called", func() {
		BeforeEach(func() {
			p = NewParser()
			res = p.Parse()
		})

		It("Should return correct parse tree", func() {
			fmt.Println(res)

			want := &Result{
				Root: &Node{
					Token: Token{
						Type:    ADD,
						Literal: "+",
					},
					Left: &Node{
						Token: Token{
							Type:    NUM,
							Literal: "1",
						},
					},
					Right: &Node{
						Token: Token{
							Type:    NUM,
							Literal: "2",
						},
					},
				},
			}

			Expect(res).To(Equal(want))
		})
	})
})
