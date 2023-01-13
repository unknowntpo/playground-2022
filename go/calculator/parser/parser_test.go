package parser_test

import (
	"fmt"
	"testing"

	"github.com/unknowntpo/playground-2022/go/calculator/parser"

	. "github.com/onsi/ginkgo/v2"
	. "github.com/onsi/gomega"
)

func TestParser(t *testing.T) {
	RegisterFailHandler(Fail)
	RunSpecs(t, "parser test suites")
}

var _ = Describe("parser", func() {
	var (
		p   *parser.Parser
		res *parser.Result
	)
	When("parser.Parse is called", func() {
		BeforeEach(func() {
			p = parser.NewParser()
			res = p.Parse()
		})

		It("Should return correct parse tree", func() {
			fmt.Println(res)
			_ = res
			Expect(res.String()).To(Equal(""))

		})
	})
})
