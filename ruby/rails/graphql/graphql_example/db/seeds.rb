# This file should contain all the record creation needed to seed the database with its default values.
# The data can then be loaded with the bin/rails db:seed command (or created alongside the database with db:setup).
#
# Examples:
#
#   movies = Movie.create([{ name: "Star Wars" }, { name: "Lord of the Rings" }])
#   Character.create(name: "Luke", movie: movies.first)
#
#
require 'faker'

10.times do
  user = User.create(
    email: Faker::Internet.email,
    first_name: Faker::Name.first_name,
    last_name: Faker::Name.last_name
  )

  Movie.create(
    user:,
    title: Faker::Movie.title,
    year: Faker::Date.between(from: '2000-01-01', to: '2021-01-01').year,
    genre: Faker::Book.genre
  )
end

# 10.times do
#   Movie.create(:user_id: "#{Faker::Name.name}@gmail.com", first_name: Faker::Name.first_name,
#                last_name: Faker::Name.last_name)
#   t.references :user, null: false, foreign_key: true
#   t.string :title
#   t.integer :year
#   t.string :genre
# end
