import gym
import os
from gym import spaces
import numpy as np
from stable_baselines3 import DQN

class CacheEnv(gym.Env):
    """
    Custom Environment that follows gym interface.
    This is a simple cache where the agent must learn to maximize hit rates.
    """

    def __init__(self, cache_size=10, total_items=50):
        super(CacheEnv, self).__init__()
        self.cache_size = cache_size
        self.total_items = total_items
        self.cache = set()
        self.action_space = spaces.Discrete(total_items)  # Choose an item to cache
        self.observation_space = spaces.Discrete(total_items)  # Current requested item
        self.total_requests = 0
        self.cache_misses = 0

    def step(self, action):
        self.total_requests += 1
        request_item = np.random.randint(0, self.total_items)
        hit = request_item in self.cache
        reward = 1 if hit else -1
        self.cache_misses += 0 if hit else 1

        # Convert action from numpy.ndarray to an integer
        action = int(action.item()) if isinstance(action, np.ndarray) else action

        if len(self.cache) < self.cache_size:
            self.cache.add(action)
        else:
            # Replace an item in the cache
            self.cache.pop()
            self.cache.add(action)

        done = False
        info = {}
        return request_item, reward, done, info

    def reset(self):
        self.cache = set()
        return np.random.randint(0, self.total_items)

    def calculate_miss_rate(self):
        return self.cache_misses / self.total_requests if self.total_requests > 0 else 0

# Create and wrap the environment
env = CacheEnv()

# Simulate cache interactions with random actions before training
env.reset()
for _ in range(1000):  # Run for a certain number of steps
    action = env.action_space.sample()  # Random action
    state, reward, done, info = env.step(action)
    if done:
        env.reset()

# Display cache miss rate before training
print(f"Cache Miss Rate before training: {env.calculate_miss_rate():.2f}")

# Define and train the agent
model = DQN("MlpPolicy", env, verbose=1)
model.learn(total_timesteps=10000)

# Display cache miss rate after training
print(f"Cache Miss Rate after training: {env.calculate_miss_rate():.2f}")

# Test the trained agent
state = env.reset()
env.total_requests = 0
env.cache_misses = 0
for _ in range(100):
    action, _states = model.predict(state)
    state, reward, done, info = env.step(action)
    if done:
      state = env.reset()

# Display cache miss rate after testing
print(f"Cache Miss Rate after testing: {env.calculate_miss_rate():.2f}")

