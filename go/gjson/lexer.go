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
	var r rune
	switch r = l.next(); {
	case r == EOF:
		return nil
	case r == '{':
		l.TokenChan <- Token{Type: TokenLeftBracket, Value: "{"}
		return lexJSON
	case r == '}':
		l.TokenChan <- Token{Type: TokenRightBracket, Value: "}"}
		return lexJSON
	case string(r) == `'` || string(r) == `"`:
		l.backup()
		return lexString
	case string(r) == `:`:
		l.TokenChan <- Token{Type: TokenColon, Value: ":"}
		return lexJSON
	}
	return l.errorf("unreachable")
}

func lexString(l *Lexer) stateFn {
	r := l.next()
	t := string(r)
	if t != `"` && t != `'` {
		return l.errorf(`unexpected token, got %v, want " or '`, l.pos)
	}
	cur := l.pos

	// find another pair
	for rune(l.input[cur]) != r {
		cur++
	}
	l.TokenChan <- Token{TokenString, string(l.input[l.pos:cur])}
	// +1 because we need to skip trailing " or '
	l.pos = cur + 1
	return lexJSON
}

const EOF = rune(0)

// l.Lexer rewind l.pos for 1 step.
func (l *Lexer) backup() {
	if !l.atEOF && l.pos > 0 {
		_, w := utf8.DecodeLastRuneInString(l.input[:l.pos])
		l.pos -= w
	}
}

// 1 2 3 4
// x y 1 y

// l.Lexer rewind l.pos for 1 step.
func (l *Lexer) dump() {
	fmt.Println("=======DUMP========")
	pChar := 0
	cPerLine := 6

	for pIdx := range l.input {
		fmt.Printf("%d\t", pIdx)
		if pIdx%cPerLine == cPerLine-1 {
			fmt.Printf("\n")
			for ; pChar <= pIdx; pChar++ {
				fmt.Printf("%s\t", string(l.input[pChar]))
			}
			fmt.Printf("\n")
		}
	}
	fmt.Println("=======DUMP========")
}

func (l *Lexer) skipWhiteSpace() {
	for l.pos < len(l.input) && l.input[l.pos] == ' ' {
		l.pos++
	}
}

func (l *Lexer) next() rune {
	l.skipWhiteSpace()
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
	close(l.TokenChan)
}

func (l *Lexer) errorf(format string, args ...interface{}) stateFn {
	l.TokenChan <- Token{TokenError, fmt.Sprintf(format, args...)}
	return nil
}
