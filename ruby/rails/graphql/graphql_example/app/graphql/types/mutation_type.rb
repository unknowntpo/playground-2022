module Types
  class MutationType < Types::BaseObject
    field :movie, mutation: Mutations::Movie
    field :user, mutation: Mutations::User
    # TODO: remove me
    field :test_field, String, null: false,
                               description: 'An example field added by the generator'
    field :create_user, mutation: Mutations::CreateUser
  end
end
