package cart_test

import (
	"testing"

	"github.com/onsi/ginkgo"
	"github.com/onsi/gomega"
)

func TestGinkgo(t *testing.T) {
	var _ = ginkgo.Describe("Calculator", func() {
		ginkgo.BeforeEach(func() {
			return
		})

		ginkgo.Context("1 + 1", func() {
			ginkgo.It("should be 2", func() {
				gomega.Expect(1 + 1).To(gomega.Equal(2))
			})
		})
	})
	gomega.RegisterFailHandler(ginkgo.Fail)
	ginkgo.RunSpecs(t, "Shopping Cart Suite")
}
