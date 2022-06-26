package cal

import (
	"testing"

	"github.com/onsi/ginkgo"
	"github.com/onsi/ginkgo/extensions/table"
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

	table.DescribeTable(
		"calculate integer",
		func(x, y, expectResult int) {
			gomega.Expect(cal.Add(x, y)).To(gomega.Equal(expectResult))
		},
		table.Entry("1+1 should be 2", 1, 1, 2),
		table.Entry("2+1 should be 3", 1, 1, 3),
	)
})
