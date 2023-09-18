# frozen_string_literal: true

require_relative '../leap_year'

RSpec.describe "Leap Year" do
  it "2016 is leap year" do
    result = is_leap_year?(2016)  # 把 2016 傳進去
    expect(result).to eq(true)    # 檢查結果應該要是 true
  end

  it "2017 is common year" do
    result = is_leap_year?(2017)  # 把 2017 傳進去
    expect(result).to eq(false)   # 檢查結果應該要是 false
  end

  it "2100 is common year" do
    result = is_leap_year?(2100)  # 把 2100 傳進去
    expect(result).to eq(false)   # 檢查結果應該要是 false
  end

  it "2400 is leap year" do
    result = is_leap_year?(2400)  # 把 2400 傳進去
    expect(result).to eq(true)    # 檢查結果應該要是 true
  end

end