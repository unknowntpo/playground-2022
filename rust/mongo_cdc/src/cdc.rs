use mongodb::{bson::doc, options::ClientOptions, options::FindOptions, Client};
use serde::{Deserialize, Serialize};
use std::fmt;

fn add(a: i32, b: i32) -> i32 {
    a + b
}

#[derive(Debug, Serialize, Deserialize)]
struct Book {
    title: String,
    author: String,
}

impl fmt::Display for Book {
    // This trait requires `fmt` with this exact signature.
    fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
        // Write strictly the first element into the supplied output
        // stream: `f`. Returns `fmt::Result` which indicates whether the
        // operation succeeded or failed. Note that `write!` uses syntax which
        // is very similar to `println!`.
        write!(f, "title: {}, author: {}", self.title, self.author)
    }
}

#[tokio::test]
async fn mongo_test() -> Result<(), String> {
    // Parse a connection string into an options struct.
    let mut client_options = ClientOptions::parse("mongodb://localhost:27017")
        .await
        .map_err(|e| e.to_string())?;

    // Manually set an option.
    client_options.app_name = Some("My App".to_string());

    // Get a handle to the deployment.
    let client = Client::with_options(client_options).unwrap();

    let db = client.database("test");

    // List the names of the databases in that deployment.
    for db_name in client
        .list_database_names(None, None)
        .await
        .map_err(|e| e.to_string())?
    {
        println!("{}", db_name);
    }

    // https://docs.rs/mongodb/latest/mongodb/#inserting-documents-into-a-collection
    let books = vec![
        Book {
            title: "The Grapes of Wrath".to_string(),
            author: "John Steinbeck".to_string(),
        },
        Book {
            title: "To Kill a Mockingbird".to_string(),
            author: "Harper Lee".to_string(),
        },
        Book {
            title: "Blanal".to_string(),
            author: "George Orwell".to_string(),
        },
        Book {
            title: "ajskdlfasdf".to_string(),
            author: "George Orwell".to_string(),
        },
    ];
    let typed_collection = db.collection::<Book>("books");
    typed_collection
        .insert_many(books, None)
        .await
        .map_err(|e| e.to_string())?;

    // Query the books in the collection with a filter and an option.
    let filter = doc! { "author": "George Orwell" };
    let find_options = FindOptions::builder().sort(doc! { "title": 1 }).build();
    let mut cursor = typed_collection
        .find(filter, find_options)
        .await
        .map_err(|e| e.to_string())?;

    // Iterate over the results of the cursor.
    while let Ok(true) = cursor.advance().await.map_err(|e| e.to_string()) {
        match cursor.deserialize_current() {
            Ok(book) => println!("Book: {}", book),
            Err(e) => return Err(e.to_string()),
        }
    }
    Ok(())
}

mod tests {
    use super::*;
    #[test]
    fn test_add() {
        assert_eq!(add(1, 2), 3);
    }

    #[test]
    fn test_mongo() {
        assert!(mongo_test().is_ok())
    }
}
