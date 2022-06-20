package main

import (
	"strings"
	"testing"

	"github.com/go-playground/assert/v2"
)

func TestMap(t *testing.T) {
	t.Run("[]string", func(t *testing.T) {
		strs := []string{"a", "b", "c", "d"}
		got := Map(strs, strings.ToUpper)
		want := []string{"A", "B", "C", "D"}

		assert.Equal(t, got, want)
	})
	t.Run("[]int", func(t *testing.T) {
		ints := []int{1, 2, 3, 4}
		got := Map(ints, func(i int) int {
			return i * 2
		})
		want := []int{2, 4, 6, 8}

		assert.Equal(t, got, want)
	})
	t.Run("[]interface{}", func(t *testing.T) {
		type data struct {
			ID        int
			IsDeleted bool
		}
		datas := []interface{}{
			data{ID: 1},
			data{ID: 2},
			data{ID: 3},
			data{ID: 4},
		}
		got := Map(datas, func(dataInt interface{}) interface{} {
			d := dataInt.(data)
			if d.ID%2 == 0 {
				d.IsDeleted = true
			}
			return d
		})
		// want := []int{2, 4, 6, 8}
		t.Log(got)

		want := []interface{}{
			data{ID: 1, IsDeleted: false},
			data{ID: 2, IsDeleted: true},
			data{ID: 3, IsDeleted: false},
			data{ID: 4, IsDeleted: true},
		}

		assert.Equal(t, got, want)
	})
}
