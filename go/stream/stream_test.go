package stream

import (
	"testing"

	"github.com/stretchr/testify/assert"
)

func TestStream(t *testing.T) {
	stream := NewStream[int]()
	result := Map(
		stream.FromSlice([]int{1, 2, 3}),
		func(e int) int { return e * 2 },
	).
		Reduce(func(e int, acc int) int { return acc + e }, 0)
	assert.Equal(t, 12, result)
}
