const express = require("express");
const expressGraphQL = require("express-graphql").graphqlHTTP;
const {
  GraphQLSchema,
  GraphQLObjectType,
  GraphQLString,
  GraphQLInt,
  GraphQLList,
  GraphQLNonNull,
  graphql,
} = require("graphql");

const app = express();

const authors = [
  { id: 1, name: "J. K. Rowling" },
  { id: 2, name: "J. R. R. Tolkien" },
  { id: 3, name: "Brent Weeks" },
];

const books = [
  { id: 1, name: "Harry Potter and the Chamber of Secretes", authorId: 1 },
  { id: 2, name: "Harry Potter and the Prisoner of Azkaban", authorId: 1 },
  { id: 3, name: "Harry Potter and the Goblet of Fire", authorId: 1 },
  { id: 4, name: "The Fellowship of the Ring", authorId: 2 },
  { id: 5, name: "The Two Towers", authorId: 2 },
  { id: 6, name: "The Return of the King", authorId: 2 },
  { id: 7, name: "The Way of Shadows", authorId: 3 },
  { id: 8, name: "Beyond the Shadows", authorId: 3 },
];

const AuthorType = new GraphQLObjectType({
  name: "Author",
  description: "This represents an author who wrote the book.",
  fields: () => ({
    id: { type: GraphQLNonNull(GraphQLInt) },
    name: { type: GraphQLNonNull(GraphQLString) },
  }),
});

const BookType = new GraphQLObjectType({
  name: "Book",
  description: "This represents a book written by an author",
  fields: () => ({
    id: { type: GraphQLNonNull(GraphQLInt) },
    name: { type: GraphQLNonNull(GraphQLString) },
    author: {
      type: AuthorType,
      resolve: (book) => {
        console.log("resolver of author query type is called!");
        return authors.find((author) => author.id === book.authorId);
      },
    },
  }),
});

const RootQueryType = new GraphQLObjectType({
  name: "Query",
  description: "Root Query",
  fields: () => ({
    book: {
      type: BookType,
      description: "A single Book",
      args: {
        id: {
          type: GraphQLInt,
          description: "Id of the Book you want",
        },
        name: {
          type: GraphQLString,
          description: "Name of the book you want",
        },
      },
      resolve: (parent, args) => {
        console.log(`args is ${args} and parent is ${parent}`);
        console.log("resolver of query a single book is called!");
        return books.find((book) => {
          return book.id === args.id || book.name.includes(`${args.name}`);
        });
      },
    },
    books: {
      type: new GraphQLList(BookType),
      description: "List of All Books",
      resolve: () => {
        console.log("resolver of root query type is called!");
        return books;
      },
    },
    author: {
      type: AuthorType,
      description: "Get Single Author",
      args: {
        id: {
          type: GraphQLInt,
          description: "ID of author",
        },
      },
      resolve: (parent, args) => {
        console.log(`parent: ${parent}, args: ${args.id}`);
        console.log(JSON.stringify(args, null, 4));
        return authors.find((author) => author.id === args.id);
      },
    },
  }),
});

const schema = new GraphQLSchema({
  query: RootQueryType,
});

app.use(
  "/graphql",
  expressGraphQL({
    schema: schema,
    graphiql: true,
  })
);
app.listen(5000, () => console.log("Server is Running"));
