package cal

import (
	"fmt"
	"testing"

	"github.com/onsi/ginkgo/v2"
	"github.com/onsi/gomega"
)

func TestCalculator(t *testing.T) {
	gomega.RegisterFailHandler(ginkgo.Fail)
	ginkgo.RunSpecs(t, "Calculator")
}

var _ = ginkgo.Describe("Calculator", func() {
	var cal Calculator

	ginkgo.BeforeEach(func() {
		cal = NewCal()
	})

	ginkgo.DescribeTable(
		"calculate integer",
		func(x, y, expectResult int) {
			gomega.Expect(cal.Add(x, y)).To(gomega.Equal(expectResult))
		},
		ginkgo.Entry("1+1 should be 2", 1, 1, 2),
		ginkgo.Entry("2+1 should be 3", 2, 1, 3),
	)
})

var _ = ginkgo.Describe("LALA", func() {
	ginkgo.BeforeEach(func() {
		fmt.Println("before each1")
	})
	ginkgo.Context("context outer", func() {
		ginkgo.BeforeEach(func() {
			fmt.Println("before each2")
		})

		ginkgo.Context("context inner", func() {
			ginkgo.It("should print something inside context inner", func() {
				gomega.Expect(1 + 1).To(gomega.Equal(2))
			})
		})
	})

	ginkgo.Describe("inner describe", func() {
		ginkgo.BeforeEach(func() {
			fmt.Println("before each3")
		})

		ginkgo.Context("context inside inner describe", func() {
			ginkgo.It("should pring something", func() {
				gomega.Expect(1 + 1).To(gomega.Equal(2))
			})
		})
	})
})
