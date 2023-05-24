package gjson

type Lexer struct {
	TokenChan chan Token
	input     string
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
	TokenLeftBrace
	TokenRightBrace
	TokenLeftBracket
	TokenRightBracket
	TokenColon
	TokenComma
)

func NewLexer(input string) *Lexer {
	return &Lexer{input: input}
}

func (l *Lexer) Run() {

}
