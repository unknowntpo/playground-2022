package main

import (
	"testing"

	"github.com/onsi/ginkgo"
	"github.com/onsi/gomega"
	"github.com/onsi/gomega/gstruct"
)

type Post struct {
	ID      string
	Title   string
	Content string
}

var _ = ginkgo.Describe("test slice matching", func() {
	var (
		// want []Post
		got []Post
	)
	ginkgo.BeforeEach(func() {
		got = []Post{
			{"TitleA", "ContentA"},
			{"TitleB", "ContentB"},
			{"TitleC", "ContentC"},
			{"TitleA", "ContentD"},
		}
		// want = []Post{
		// 	{"TitleA", "ContentA"},
		// 	{"TitleB", "ContentC"},
		// }

	})
	ginkgo.When("nothing", func() {
		id := func(element interface{}) string {
			return string(element.(string)[0])
		}
		ginkgo.It("match", func() {
			// gomega.Expect(got).To(
			// 	gomega.ContainElement(gstruct.MatchFields(gstruct.IgnoreExtras, gstruct.Fields{
			// 		"Title": gstruct.Match,
			// 	})))
			gomega.Expect(got).To(
				gstruct.MatchAllElements(id,
					gstruct.Elements{
						"TitleA": gstruct.MatchFields(
							gstruct.IgnoreExtras, gstruct.Fields{
								"Title": gomega.MatchRegexp("^Bitle.*"),
							},
						)},
				))
		})
	})
})

func TestSlice(t *testing.T) {
	gomega.RegisterFailHandler(ginkgo.Fail)
}
