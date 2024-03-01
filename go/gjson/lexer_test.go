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
			name:  "key-value pair",
			input: `{"hello": "world", arr: ["hello", 123, null]}`,
			want: []Token{
				{TokenLeftBracket, "{"},
				// string: string
				{TokenString, "hello"},
				{TokenColon, ":"},
				{TokenString, "world"},
				{TokenComma, ","},
				// array of object
				{TokenString, "arr"},
				{TokenLeftSquareBracket, "["},
				{TokenString, "hello"},
				{TokenNumber, "123"},
				{TokenNull, "null"},
				{TokenRightSquareBracket, "]"},
				{TokenRightBracket, "}"},
			},
		},
	}
	for _, tt := range testCases {
		t.Run(tt.name, func(t *testing.T) {
			l := NewLexer(tt.input)
			go l.Run()

			l.dump()

			got := []Token{}
			for token := range l.TokenChan {
				got = append(got, token)
				t.Log("got token", token)
			}
			assert.Equal(t, got, tt.want, "got should be equal to want")
		})
	}
}
