require 'rspec'

RSpec.describe 'Calculator' do
  before(:each) { test = 1 }
  describe 'when calculating addition' do
    it '1 + 1 should return correct result' do
      expect(1 + 1).to eq(2)
    end
  end

  context 'in one context' do
    it 'does one thing' do
      expect(1).to eq(1)
    end
  end

  context 'in another context' do
    it 'does another thing' do
      expect(1).to eq(1)
    end
  end
end

# https://blog.niclin.tw/2019/03/30/rspec-%E4%B8%AD-let-/-let%E9%A9%9A%E5%98%86%E8%99%9F-/-instance-variables-/-subject-%E7%9A%84%E7%94%A8%E6%B3%95%E8%88%87%E5%B7%AE%E7%95%B0/
$count = 0
RSpec.describe 'let' do
  let(:count) { $count += 1 }

  it 'memoizes the value' do
    expect(count).to eq(1)
    expect(count).to eq(1)
    puts("value of count:#{count}")
  end

  it 'is not cached across examples' do
    expect(count).to eq(2)
  end
end
