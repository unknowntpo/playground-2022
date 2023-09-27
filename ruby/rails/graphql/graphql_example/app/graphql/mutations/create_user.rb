module Mutations
  class CreateUser < Mutations::BaseMutation
    argument :first_name, String, required: true
    argument :last_name, String, required: true
    argument :email, String, required: true
    argument :movies, [Types::MovieInputType], required: true

    field :user, Types::UserType, null: false
    field :errors, [String], null: false

    def resolve(first_name:, last_name:, email:, movies:)
      user = ::User.new(first_name:, last_name:, email:)
      return { user: nil, errors: user.errors.full_messages } unless user.valid?

      puts "movies: #{movies.inspect}"

      if user.save
        puts "#{user.inspect} issaved"
        { user:, errors: [] }
      else
        logger.debug 'has some error'

        { user: nil, errors: user.errors.full_messages }
      end
    end
  end
end
