package parser

import "encoding/json"

type Parser struct {
}

func NewParser() *Parser {
	return &Parser{}
}

type Result struct {
	Root *Node
}

func (r *Result) String() string {
	b, _ := json.MarshalIndent(r, "", "\t")
	return string(b)
}

func forEach[T any](collections []T, iterFn func(e T, idx int)) {
	for i, e := range collections {
		iterFn(e, i)
	}
}

func (p *Parser) Parse(tokens []Token) *Result {
	// Do some parse things
	// tree := &Result{}
	forEach(tokens, func(e Token, idx int) {
		//	e.BuildTree()
	})
	return &Result{
		Root: &Node{
			Token: Token{
				Type:    ADD,
				Literal: "+",
			},
			Left: &Node{
				Token: Token{
					Type:    NUM,
					Literal: "1",
				},
			},
			Right: &Node{
				Token: Token{
					Type:    NUM,
					Literal: "2",
				},
			},
		},
	}
}

type Node struct {
	Token Token
	Left  *Node
	Right *Node
}

func (n *Node) String() string {
	var (
		leftStr, rightStr string
	)
	if n.Left == nil && n.Right == nil {
		// s, _ := json.MarshalIndent(n)
		// return string(s)
	}
	if n.Left != nil {
		leftStr = n.Left.String()
	}
	if n.Right != nil {
		rightStr = n.Right.String()
	}
	return leftStr + rightStr
}

type Token struct {
	Type    TokenType
	Literal string
}

type TokenType string

const (
	ADD TokenType = "+"
	SUB TokenType = "-"
	MUL TokenType = "*"
	DIV TokenType = "/"
	NUM TokenType = "NUM"
)
