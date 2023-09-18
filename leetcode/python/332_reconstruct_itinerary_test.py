import pytest
from typing import *
import heapq
import collections
import json


class Solution:
    def findItinerary(self, tickets: List[List[str]]) -> List[str]:
        def dfs(curr: str):
            print("dfs: curr: ", curr)
            while vec[curr]:
                tmp = heapq.heappop(vec[curr])
                print("tmp: ", tmp)
                print("vec[curr]", vec[curr])
                dfs(tmp)
            stack.append(curr)

        vec = collections.defaultdict(list)
        print("vec", vec)
        for depart, arrive in tickets:
            vec[depart].append(arrive)
        print("vec after append", json.dumps(vec, indent=4))
#  {'JFK': ['SFO', 'ATL'], 'SFO': ['ATL'], 'ATL': ['JFK', 'SFO']})

        for key in vec:
            print("key", key)
            heapq.heapify(vec[key])

        print("heapq", heapq)

        stack = list()
        dfs("JFK")
        return stack[::-1]

# tickets = [["MUC", "LHR"], ["JFK", "MUC"], ["SFO", "SJC"], ["LHR", "SFO"]]


tickets = [["JFK", "SFO"], ["JFK", "ATL"], [
    "SFO", "ATL"], ["ATL", "JFK"], ["ATL", "SFO"]]
print("input: ", tickets)

sol = Solution()
ans = sol.findItinerary(tickets)

print("ans", ans)
