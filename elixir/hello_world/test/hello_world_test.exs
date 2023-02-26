defmodule HelloWorldTest do
  use ExUnit.Case
  doctest HelloWorld

  test "greets the world" do
    assert HelloWorld.hello() == :world
  end
end

# defmodule MapUtilsTest do
#   use ExUnit.Case

#   test "converts keys to uppercase" do
#     map = %{"a" => 1, "b" => 2, "c" => 3}
#     expected = %{"A" => 1, "B" => 2, "C" => 3}
#     assert MapUtils.keys_to_uppercase(map) == expected
#   end
# end

defmodule MapUtilsTest do
  use ExUnit.Case
  import Utils.MapUtils

  test "converts keys to uppercase" do
    map = %{"a" => 1, "b" => 2, "c" => 3}
    expected = %{"A" => 1, "B" => 2, "C" => 3}
    assert MapUtils.keys_to_uppercase(map) == expected
  end
end
