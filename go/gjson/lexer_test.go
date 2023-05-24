package gjson

import (
	"testing"

	"github.com/stretchr/testify/assert"
)

func TestLexer(t *testing.T) {
	type TestCase struct {
		name  string
		input string
		want  []Token
	}
	testCases := []TestCase{
		{
			name:  "basic case",
			input: `{"hello": "world"}`,
			want: []Token{
				{TokenLeftBracket, "{"},
				{TokenString, "hello"},
				{TokenColon, ":"},
				{TokenString, "world"},
				{TokenRightBracket, "}"},
			},
		},
	}
	for _, tt := range testCases {
		t.Run(tt.name, func(t *testing.T) {
			l := NewLexer(tt.input)
			go l.Run()

			got := []Token{}
			for token := range l.TokenChan {
				got = append(got, token)
				t.Log("got token", token)
			}
			assert.Equal(t, got, tt.want, "got should be equal to want")
		})
	}
}
