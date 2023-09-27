class User < ApplicationRecord
  has_many :movies
  # insert movies in the same time
  accepts_nested_attributes_for :movies
end
