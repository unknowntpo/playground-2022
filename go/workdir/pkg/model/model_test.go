package model

import (
	"os"
	"testing"

	"github.com/stretchr/testify/assert"
)

func TestMain(t *testing.T) {
	wd, err := os.Getwd()
	assert.NoError(t, err)
	t.Log("wd in pkg/model/models_test.go", wd)
}

func MainTest(t *testing.T) {
	f, err := os.Open("../.env")
	assert.NoError(t, err)
	t.Log(f.Name())
}
