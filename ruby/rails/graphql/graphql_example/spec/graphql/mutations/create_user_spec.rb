require 'rails_helper'

module Mutations
  RSpec.describe Mutations::CreateUser, type: :request do
    describe '.resolve' do
      it 'creates a user' do
        user = FactoryBot.create(:user)

        expect do
          post '/graphql', params: { query: _query }
        end.to change { ::User.count }.by(1)
      end

      it 'returns a book' do
        user = FactoryBot.create(:user)

        post '/graphql', params: { query: _query }
        json = JSON.parse(response.body)
        data = json['data']['createUser']

        expect(data).to include(
          'id' => be_present,
          'user_id' => user.id,
          'first_name' => 'Russ',
          'last_name' => 'Cox',
          'date_of_birth' => 1999
        )
      end
    end

    def _query
      <<~GQL
        mutation {
          createUser(
            first_name: "Russ"
            last_name: Cox
            date_of_birth: 1999
          ) {
            id
            first_name
            last_name
            date_of_birth
      GQL
    end
  end
end
