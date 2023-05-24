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
	value string
}

type TokenType int

const (
	TokenError TokenType = iota
	TokenEOF
	TokenString
	TokenNumber
	TokenTrue
	TokenFalse
	TokenNull
	TokenLeftBracket
	TokenRightBracket
	TokenColon
	TokenComma
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
		l.TokenChan <- Token{Type: TokenLeftBracket}
		return lexJSON
	case string(r) == `'` || string(r) == `"`:
		return lexString
	}
	return l.errorf("unreachable")
}

func lexString(l *Lexer) stateFn {
	return l.errorf("lexString unimplemented")
}

const EOF = rune(0)

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
