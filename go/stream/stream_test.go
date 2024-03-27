package stream

import (
	"testing"

	"github.com/stretchr/testify/assert"
)

func TestStream(t *testing.T) {
	t.Run("test map-reduce", func(t *testing.T) {
		slice := []int{1, 2, 3}
		stream := NewStream[int]()
		result := Map(
			stream.FromSlice(slice),
			func(e int) int { return e * 2 },
		).
			Reduce(func(e int, acc int) int { return acc + e }, 0)
		assert.Equal(t, 12, result)
	})
}
