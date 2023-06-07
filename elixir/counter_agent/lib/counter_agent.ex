defmodule CounterAgent do
  @moduledoc """
  Documentation for `CounterAgent`.
  """

  @doc """
  Hello world.

  ## Examples

      iex> CounterAgent.hello()
      :world

  """
  def hello do
    :world
  end

  def start_link do
    Agent.start_link(fn -> 0 end, name: __MODULE__)
  end

  def increment(counter) do
    Agent.update(counter, fn cnt -> cnt + 1 end)
  end

  def decrement(counter) do
    Agent.update(counter, fn cnt -> cnt - 1 end)
  end

  def get_count(counter) do
    Agent.get(counter, fn cnt -> cnt end)
  end
end
