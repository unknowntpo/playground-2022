import gym
import warnings

env = gym.make("Taxi-v3", render_mode="ansi").env

env.reset()

# state = env.encode(3, 1, 2, 0) 
# env.s = state
# out = env.render()
# print(out)

# print("Action Space {}".format(env.action_space))
# print("State Space {}".format(env.observation_space))

# List of movements: (0: south, 1: north, 2: east, 3: west, 4: pickup, 5: dropoff)
actions = [2, 2, 2, 3, 3, 3, 1, 1, 1, 1, 4, 5]

# Simulate the movements and print the output for each movement
for action in actions:
    state, reward, done, truncated, info = env.step(action)
    print("Action:", action)
    print(env.render())
    print("State:", state, "Reward:", reward, "Done:", done)
    if done:
        break

# Close the environment
env.close()
