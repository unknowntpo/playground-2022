require 'rails_helper'

module Mutations
  RSpec.describe Mutations::CreateUser, type: :request do
    describe '.resolve' do
      it 'creates a user' do
        allow(Rails.logger).to receive(:debug)

        first_name = 'John'
        last_name = 'Doe'
        email = 'johndoe@example.com'

        post '/graphql', params: { query: _query(first_name, last_name, email) }
        body = JSON.parse(response.body)
        data = body['data']['createUser']['user']

        new_user = ::User.last

        expect(data).to include(
          'id' => new_user.id.to_s,
          'firstName' => new_user.first_name,
          'lastName' => new_user.last_name,
          'email' => new_user.email
        )
      end
    end

    def _query(first_name, last_name, email)
      <<~GQL
        mutation {
          createUser(input: {
            firstName: "#{first_name}",
            lastName: "#{last_name}",
            email: "#{email}"
          }) {
              user {
                id
                firstName
                lastName
                email
              }
          }
        }
      GQL
    end
  end
end
