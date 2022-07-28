package cal

import (
	"fmt"

	"github.com/onsi/ginkgo/v2"
	"github.com/onsi/gomega"
)

// func TestNum(t *testing.T) {
// 	gomega.RegisterFailHandler(ginkgo.Fail)
// 	ginkgo.RunSpecs(t, "Num")
// }

var _ = ginkgo.Describe("Num Test", func() {
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
			fmt.Println("abc")
			ginkgo.It("should pring something", func() {
				gomega.Expect(1 + 1).To(gomega.Equal(2))
			})
		})
	})
})
