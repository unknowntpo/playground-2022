package main

type Parser struct {
}

func NewParser() *Parser {
	return &Parser{}
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
