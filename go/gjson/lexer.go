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
	TokenError              TokenType = "ERROR"                // error
	TokenEOF                          = "EOF"                  // eof
	TokenString                       = "STRING"               // string
	TokenNumber                       = "NUMBER"               // number
	TokenTrue                         = "TRUE"                 // true
	TokenFalse                        = "FALSE"                // false
	TokenNull                         = "NULL"                 // null
	TokenLeftBracket                  = "LEFT_BRACKET"         // {
	TokenRightBracket                 = "RIGHT_BRACKET"        // }
	TokenLeftSquareBracket            = "LEFT_SQUARE_BRACKET"  // [
	TokenRightSquareBracket           = "RIGHT_SQUARE_BRACKET" // ]
	TokenColon                        = "COLON"                // :
	TokenComma                        = "COMMA"                // ,
)

func NewLexer(input string) *Lexer {
	l := &Lexer{input: input}
	l.TokenChan = make(chan Token)
	return l
}

type stateFn func(l *Lexer) stateFn

func lexJSON(l *Lexer) stateFn {
	var r rune = l.next()
	switch r {
	case EOF:
		return nil
	case '{':
		l.TokenChan <- Token{Type: TokenLeftBracket, Value: "{"}
		return lexJSON
	case '}':
		l.TokenChan <- Token{Type: TokenRightBracket, Value: "}"}
		return lexJSON
	case '\'', '"':
		l.backup()
		return lexString
	case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9':
		l.backup()
		return lexNumber
	case '[':
		l.TokenChan <- Token{Type: TokenLeftSquareBracket, Value: "["}
		return lexJSON
	case ']':
		l.TokenChan <- Token{Type: TokenRightSquareBracket, Value: "]"}
		return lexJSON
	case ':':
		l.TokenChan <- Token{Type: TokenColon, Value: ":"}
		return lexJSON
	case ',':
		l.TokenChan <- Token{Type: TokenComma, Value: ","}
		return lexJSON
	}
	return l.errorf("unreachable")
}

func lexNumber(l *Lexer) stateFn {
	var r rune
	numStr := ""
	for r = l.next(); r == '0' || r == '1' || r == '2' || r == '3' ||
		r == '4' || r == '5' || r == '6' || r == '7' ||
		r == '8' || r == '9'; r = l.next() {
		numStr += string(r)
	}
	fmt.Println("got numStr", numStr)
	l.TokenChan <- Token{Type: TokenNumber, Value: numStr}
	return lexJSON
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
		// print index number
		fmt.Printf("%d\t", pIdx)
		if pIdx%cPerLine == cPerLine-1 || pIdx == len(l.input)-1 {
			// need to print literal
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
