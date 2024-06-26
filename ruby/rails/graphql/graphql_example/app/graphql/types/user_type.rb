# frozen_string_literal: true

module Types
  class UserType < Types::BaseObject
    field :id, ID, null: false
    field :email, String
    field :first_name, String
    field :last_name, String
    field :created_at, GraphQL::Types::ISO8601DateTime, null: false
    field :updated_at, GraphQL::Types::ISO8601DateTime, null: false

    field :movies_count, Integer, null: true
    field :movies, [Types::MovieType], null: true

    def movies_count
      object.movies.size
    end

    def movies
      object.movies
    end
  end
end

# module Types
#   class UserType < BaseNode
#     field :created_at, DateTimeType, null: false
#     field :name, String, null: false
#     field :email, String, null: false
#     field :votes, [VoteType], null: false
#     field :links, [LinkType], null: false
#   end
# e
