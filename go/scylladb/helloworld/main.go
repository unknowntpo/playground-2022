package main

import (
	"fmt"
	"github.com/gocql/gocql"
)

func main() {
	// Connect to the ScyllaDB cluster
	cluster := gocql.NewCluster("localhost:9042", "localhost:9043", "localhost:9044")

	sess, err := cluster.CreateSession()
	if err != nil {
		panic(err)
	}
	defer sess.Close()

	if err := createKeyspace(sess); err != nil {
		panic(fmt.Errorf("failed on createKeyspace: %v", err))
	}

	cluster.Keyspace = "carepet"

	// Create the pets table if it doesn't exist
	if err := sess.Query(`
        CREATE TABLE IF NOT EXISTS carepet.pets (
            id   UUID PRIMARY KEY,
            name text,
            age  int
        )
    `).Exec(); err != nil {
		panic(err)
	}

	// Insert some dummy data into the pets table
	pets := []Pet{
		{gocql.TimeUUID(), "Fluffy", 3},
		{gocql.TimeUUID(), "Mittens", 5},
		{gocql.TimeUUID(), "Fido", 2},
	}
	for _, pet := range pets {
		if err := sess.Query(`
            INSERT INTO carepet.pets (id, name, age) VALUES (?, ?, ?)
        `, pet.ID, pet.Name, pet.Age).Exec(); err != nil {
			panic(err)
		}
	}

	// Query the pets table and print the results
	var id gocql.UUID
	var name string
	var age int
	iter := sess.Query("SELECT id, name, age FROM carepet.pets").Iter()
	for iter.Scan(&id, &name, &age) {
		fmt.Printf("ID: %v, Name: %s, Age: %d\n", id, name, age)
	}
	if err := iter.Close(); err != nil {
		panic(err)
	}
}

type Pet struct {
	ID   gocql.UUID
	Name string
	Age  int
}

func createKeyspace(sess *gocql.Session) error {
	if err := sess.Query(KeySpaceCQL).Exec(); err != nil {
		return fmt.Errorf("failed on sess.Query: %v", err)
	}
	return nil
}

const KeySpaceCQL = `CREATE KEYSPACE IF NOT EXISTS
  carepet WITH replication = { 'class': 'NetworkTopologyStrategy', 'replication_factor': '3' }
  AND durable_writes = TRUE;`
