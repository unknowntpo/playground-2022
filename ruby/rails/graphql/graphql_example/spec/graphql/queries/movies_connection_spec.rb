require 'rails_helper'

module Queries
  RSpec.describe MoviesConnection, type: :request do
    describe '.resolve' do
      it 'returns all movies with pagination' do
        user = create(:user)
        FactoryBot.create(:movie, user:, title: 'Hero', year: 1984, genre: 'Horror')
        FactoryBot.create(:movie, user:, title: 'Gifted', year: 1988, genre: 'Thriller')
        FactoryBot.create(:movie, user:, title: 'It', year: 1986, genre: 'Horror')

        post '/graphql', params: { query: }

        json = JSON.parse(response.body)
        puts "json: #{json.inspect}"
        page_info = json['data']['moviesConnection']['pageInfo']
        edges = json['data']['moviesConnection']['edges']

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
              'author' => { 'id' => author.id.to_s }
            )
          },
          {
            'cursor' => 'Mg',
            'node' => hash_including(
              'title' => 'Gifted',
              'author' => { 'id' => author.id.to_s }
            )
          }
        ]
      end
    end

    def query
      <<~GQL
        query {
          moviesConnection(first: 2) {
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
                user {
                  id
                }
              }
            }
          }
        }
      GQL
    end
  end
end
