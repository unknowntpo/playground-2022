# frozen_string_literal: true

module Types
  class MovieType < Types::BaseObject
    field :id, ID, null: false
    field :user_id, Integer, null: false
    field :title, String
    field :year, Integer
    field :genre, String
    field :created_at, GraphQL::Types::ISO8601DateTime, null: false
    field :updated_at, GraphQL::Types::ISO8601DateTime, null: false
  end

  class MovieEdge < BaseEdge
    node_type MovieType
  end

  class MoviesConnection < BaseConnection
    edge_type MovieEdge,
              edges_nullable: true,
              edge_nullable: true,
              node_nullable: true,
              nodes_field: true
  end
end
