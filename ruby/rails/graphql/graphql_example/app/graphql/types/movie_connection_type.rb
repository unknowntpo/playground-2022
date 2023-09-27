# frozen_string_literal: true

module Types
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
