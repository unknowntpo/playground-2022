defmodule CounterAgentTest do
  use ExUnit.Case, async: true
  doctest CounterAgent

  setup do
    {:ok, counter} = CounterAgent.start_link()
    %{counter: counter}
  end

  test "increment the counter", %{counter: counter} do
    CounterAgent.increment(counter)
    CounterAgent.increment(counter)
    CounterAgent.increment(counter)

    assert CounterAgent.get_count(counter) == 3
  end
end
