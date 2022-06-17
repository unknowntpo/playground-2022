package api

import (
	"os"
	"testing"

	"github.com/unknowntpo/playground-2002/go/workdir/pkg/model"

	"github.com/stretchr/testify/assert"
)

func TestMain(t *testing.T) {
	wd, err := os.Getwd()
	t.Log("wd in api/api_test.go", wd)

	f, err := os.Open("../.env")
	assert.NoError(t, err)
	t.Log("filename", f.Name())

	assert.NoError(t, model.Setup())
}
