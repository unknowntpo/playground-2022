module Mutations
  class Mutations::CreateUser < Mutations::BaseMutation
    argument :first_name, String, required: true
    argument :last_name, String, required: true
    argument :email, String, required: true

    field :user, Types::UserType, null: false
    field :errors, [String], null: false

    def resolve(first_name:, last_name:, email:)
      user = ::User.new(first_name:, last_name:, email:)
      return { user: nil, errors: user.errors.full_messages } unless user.valid?

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
