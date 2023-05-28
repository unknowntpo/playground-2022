package gjson

import (
	"fmt"
	"unicode/utf8"
)

type Lexer struct {
	TokenChan chan Token
	input     string
	pos       int
	atEOF     bool
}

type Token struct {
	Type  TokenType
	Value string
}

type TokenType string

const (
	TokenError        TokenType = "ERROR"
	TokenEOF                    = "EOF"
	TokenString                 = "STRING"
	TokenNumber                 = "STRING"
	TokenTrue                   = "TRUE"
	TokenFalse                  = "FALSE"
	TokenNull                   = "NULL"
	TokenLeftBracket            = "LEFT_BRACKET"
	TokenRightBracket           = "RIGHT_BRACKET"
	TokenColon                  = "COLON"
	TokenComma                  = "COMMA"
)

func NewLexer(input string) *Lexer {
	l := &Lexer{input: input}
	l.TokenChan = make(chan Token)
	return l
}

type stateFn func(l *Lexer) stateFn

func lexJSON(l *Lexer) stateFn {
	switch r := l.next(); {
	case r == '{':
		l.TokenChan <- Token{Type: TokenLeftBracket, Value: "{"}
		return lexJSON
	case string(r) == `'` || string(r) == `"`:
		return lexString
	case string(r) == `:`:
		l.TokenChan <- Token{Type: TokenColon, Value: ":"}
		return lexJSON
	}
	return l.errorf("unreachable")
}

func lexString(l *Lexer) stateFn {
	l.backup()
	cur := l.pos
	for string(l.input[cur]) != `"` && string(l.input[cur]) != `'` {
		cur++
	}
	l.TokenChan <- Token{TokenString, string(l.input[l.pos:cur])}
	l.pos = cur
	return lexJSON
}

const EOF = rune(0)

// backup rewind lexer by 1 position
func (l *Lexer) backup() {
	if int(l.pos) == 0 {
		return
	}
	l.pos = l.pos - 1
}

func (l *Lexer) next() rune {
	if int(l.pos) >= len(l.input) {
		l.atEOF = true
		return EOF
	}
	r, w := utf8.DecodeRuneInString(l.input[l.pos:])
	l.pos += w
	return r
}

func (l *Lexer) Run() {
	state := lexJSON
	for state != nil {
		state = state(l)
	}
}

func (l *Lexer) errorf(format string, args ...interface{}) stateFn {
	l.TokenChan <- Token{TokenError, fmt.Sprintf(format, args...)}
	return nil
}
