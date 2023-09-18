import pytest
from typing import *


class Solution:
    def shortestPathLength(self, graph: List[List[int]]) -> int:
        # for every path: traverse until end
        paths = []
        path = []

        def dfs(start: int):
            print(f"graph[{start}]: {graph[start]}")
            if len(graph[start]) == 0:
                paths.append(path[:])
                return
            for i, e in enumerate(graph[start]):
                path.append(e)
                del graph[start][i]
                dfs(e)
                path.pop()
                graph[start][i].insert(i, e)

        # then return the shortest path
        paths = []
        for i in range(0, len(graph)):
            dfs(i)
        print("paths", paths)

        paths.sort(key=lambda x: len(x), reverse=True)
        return len(graph[0])


graph = [[1, 2, 3], [0], [0], [0]]
sol = Solution()
sol.shortestPathLength(graph)
