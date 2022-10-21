package main

import (
	"testing"

	"github.com/stretchr/testify/assert"
)

func TestPosts(t *testing.T) {
	e := setupEngine()
	res := [][]string{}
	err := e.SQL("select * from post").Find(&res)
	assert.NoError(t, err)
	t.Log(res)
}
