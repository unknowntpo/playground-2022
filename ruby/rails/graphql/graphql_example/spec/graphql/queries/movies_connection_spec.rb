require 'rails_helper'

module Queries
  RSpec.describe 'users', type: :request do
    describe '.resolve' do
      it 'returns all users with pagination' do
        first_name = 'John'
        last_name = 'Doe'
        email = 'johndoe@example.com'

        user = FactoryBot.create(:user, first_name:, last_name:, email:)
        FactoryBot.create(:movie, user:, title: 'Hero', year: 1984, genre: 'Horror')
        FactoryBot.create(:movie, user:, title: 'Gifted', year: 1988, genre: 'Thriller')
        FactoryBot.create(:movie, user:, title: 'It', year: 1986, genre: 'Horror')

        post '/graphql', params: { query: }

        json = JSON.parse(response.body)
        puts "json: #{JSON.generate(json)}"
        page_info = json['data']['movies']['pageInfo']
        edges = json['data']['movies']['edges']

        expect(page_info).to eq(
          'startCursor' => 'MQ',
          'endCursor' => 'Mg',
          'hasPreviousPage' => false,
          'hasNextPage' => true
        )
        expect(edges).to match_array [
          {
            'cursor' => 'MQ',
            'node' => hash_including(
              'title' => 'Hero',
              'userId' => user.id,
              'year' => 1984,
              'genre' => 'Horror'
            )
          },
          {
            'cursor' => 'Mg',
            'node' => hash_including(
              'title' => 'Gifted',
              'userId' => user.id,
              'year' => 1988,
              'genre' => 'Thriller'
            )
          }
        ]
      end
    end

    def query
      <<~GQL
        query {
          movies(first: 2) {
            pageInfo {
              endCursor
              startCursor
              hasPreviousPage
              hasNextPage
            }
            edges {
              cursor
              node {
                title
                userId
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
