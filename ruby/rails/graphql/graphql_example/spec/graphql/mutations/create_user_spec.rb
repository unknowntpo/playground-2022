require 'rails_helper'

module Mutations
  RSpec.describe Mutations::CreateUser, type: :request do
    describe '.resolve' do
      it 'creates a user' do
        first_name = 'John'
        last_name = 'Doe'
        email = 'johndoe@example.com'

        movies = [
          { title: 'Movie1', year: 2022, genre: 'Action' },
          { title: 'Movie2', year: 2021, genre: 'Drama' }
        ]

        post '/graphql', params: { query: _query(first_name, last_name, email, movies) }
        body = JSON.parse(response.body)
        puts "body: #{body.inspect}"
        data = body['data']['createUser']['user']

        new_user = ::User.last

        expect(data).to include(
          'id' => new_user.id.to_s,
          'firstName' => new_user.first_name,
          'lastName' => new_user.last_name,
          'email' => new_user.email,
          'movies' => a_collection_containing_exactly(
            *movies.map do |movie|
              hash_including('title' => movie[:title], 'year' => movie[:year], 'genre' => movie[:genre])
            end
          )
        )
      end
    end

    def _query(first_name, last_name, email, movies)
      movies_input = movies.map do |movie|
        "{ title: \"#{movie[:title]}\", year: #{movie[:year]}, genre: \"#{movie[:genre]}\" }"
      end.join(', ')

      puts "movies_input: #{movies_input.inspect}"

      <<~GQL
        mutation {
          createUser(input: {
            firstName: "#{first_name}",
            lastName: "#{last_name}",
            email: "#{email}",
            movies: [#{movies_input}]
          }) {
            user {
              id
              firstName
              lastName
              email
              movies {
                title
                year
                genre
              }
            }
          }
        }
      GQL
    end
  end
end
