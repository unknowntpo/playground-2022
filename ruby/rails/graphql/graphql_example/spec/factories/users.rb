FactoryBot.define do
  factory :user do
    # now = Time.find_zone('Wellington').local(2011, 1, 1)

    sequence(:first_name) { |n| "John (#{n})" }
    sequence(:last_name) { |n| "Smith (#{n})" }
    # created_at { now }
    # updated_at { now }
    # movies_count { 0 }
    movies { [] }
  end
end
