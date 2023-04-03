package main

import (
	"log"

	"github.com/gocql/gocql"
	"github.com/scylladb/gocqlx/qb"
	"github.com/scylladb/gocqlx/v2"
)

func main() {
	// Create gocql cluster.
	cluster := gocql.NewCluster("localhost:9042")
	// Wrap session on creation, gocqlx session embeds gocql.Session pointer.
	session, err := gocqlx.WrapSession(cluster.CreateSession())
	if err != nil {
		log.Fatal("failed on gocqlx.WrapSession", err)
	}

	cluster.ProtoVersion = 4 // Set the protocol version to 4

	cluster.Keyspace = "example"

	if err := session.ExecStmt(`CREATE KEYSPACE IF NOT EXISTS example
  WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 2};
`); err != nil {
		log.Fatal("failed to create keyspace: ", err)
	}

	// if err := session.ExecStmt(`USE example_keyspace`); err != nil {
	// 	log.Fatal("failed to use keyspace: ", err)
	// }

	if err := session.ExecStmt(
		`CREATE TABLE IF NOT EXISTS example.person (
  first_name text,
  last_name text,
  email text,
  PRIMARY KEY ((first_name), last_name)
);`); err != nil {
		log.Fatal("failed to create keyspace: ", err)
	}

	// Person represents a row in person table.
	// Field names are converted to camel case by default, no need to add special tags.
	// A field will not be persisted by adding the `db:"-"` tag or making it unexported.
	type Person struct {
		FirstName string
		LastName  string
		Email     string
		HairColor string `db:"-"` // exported and skipped
		eyeColor  string // unexported also skipped
	}

	p := Person{
		"Michał",
		"Matczuk",
		"michal@scylladb.com",
		"red",   // not persisted
		"hazel", // not persisted
	}

	insertQuery, names := qb.Insert("example.person").Columns("first_name", "last_name", "email").ToCql()
	if err := session.Query(insertQuery, names).BindStruct(p).ExecRelease(); err != nil {
		log.Fatal(err)
	}

	// Retrieve the data and print it to the console
	stmt, name := qb.Select("examples.person").
		Columns("*").
		ToCql()
	tmp := session.Query(stmt, name)

	var people []Person

	tmp.Select(&people)

	log.Println(people)

}
