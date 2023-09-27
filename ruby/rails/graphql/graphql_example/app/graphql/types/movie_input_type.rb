# frozen_string_literal: true

module Types
  class MovieInputType < Types::BaseInputObject
    argument :title, String, required: true
    argument :year, Int, required: true
    argument :genre, String, required: true
  end
end
