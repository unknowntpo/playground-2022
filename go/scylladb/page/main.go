package main

import (
	"encoding/json"
	"fmt"
	"time"

	"github.com/gocql/gocql"
)

// Define the Pet struct to hold the data
type Page struct {
	UserID  int
	ListKey gocql.UUID
	PageKey gocql.UUID
	Time    time.Time
}

func debug(e any) string {
	b, err := json.MarshalIndent(e, "", "\t")
	if err != nil {
		panic(err)
	}
	return string(b)
}

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

	cluster.Keyspace = "page"

	// Create the pets table if it doesn't exist
	if err := sess.Query(`
        CREATE TABLE IF NOT EXISTS page.page (
          user_id int,
          list_key uuid,
          page_key uuid,
          time timestamp,
          PRIMARY KEY ((user_id, list_key), page_key, time)
        ) WITH compaction = {
            'class': 'TimeWindowCompactionStrategy',
            'compaction_window_size': '1',
            'compaction_window_unit': 'DAYS'
        };
    `).Exec(); err != nil {
		panic(err)
	}

	// Insert some dummy data into the page table
	pages := []Page{
		{1, gocql.TimeUUID(), gocql.TimeUUID(), time.Now()},
		{1, gocql.TimeUUID(), gocql.TimeUUID(), time.Now().Add(1 * time.Hour)},
		{1, gocql.TimeUUID(), gocql.TimeUUID(), time.Now().Add(2 * time.Hour)},
		{2, gocql.TimeUUID(), gocql.TimeUUID(), time.Now().Add(-1 * time.Hour)},
		{2, gocql.TimeUUID(), gocql.TimeUUID(), time.Now()},
		{2, gocql.TimeUUID(), gocql.TimeUUID(), time.Now().Add(1 * time.Hour)},
	}

	for _, page := range pages {
		if err := sess.Query(`
            INSERT INTO page.page (user_id, list_key, page_key, time) VALUES (?, ?, ?, ?)
        `, page.UserID, page.ListKey, page.PageKey, page.Time).Exec(); err != nil {
			panic(err)
		}
	}

	// Select the pages that match the provided userID and list_key
	var gotPages []Page
	iter := sess.Query(`
    SELECT user_id, list_key, page_key, time
    FROM page.page
    WHERE user_id = ? AND list_key = ?
    ORDER BY time ASC
`, 1, pages[0].ListKey).Iter()
	for {
		page := Page{}
		if !iter.Scan(&page.UserID, &page.ListKey, &page.PageKey, &page.Time) {
			break
		}
		pages = append(gotPages, page)
	}
	if err := iter.Close(); err != nil {
		panic(err)
	}

	fmt.Println("gotPages", debug(gotPages))

	// // Query the pets table and print the results
	// var id gocql.UUID
	// var name string
	// var age int
	// iter := sess.Query("SELECT id, name, age FROM carepet.pets").Iter()
	//
	//	for iter.Scan(&id, &name, &age) {
	//		fmt.Printf("ID: %v, Name: %s, Age: %d\n", id, name, age)
	//	}
	//
	//	if err := iter.Close(); err != nil {
	//		panic(err)
	//	}
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
  page WITH replication = { 'class': 'NetworkTopologyStrategy', 'replication_factor': '3' }
  AND durable_writes = TRUE;`
