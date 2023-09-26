FactoryBot.define do
  factory :movie do
    user
    sequence(:title) { |n| "Best book ever (#{n})" }
    year { 1984 }
    genre { 'Thriller' }
  end
end
