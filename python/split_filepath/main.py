import json

def fn(line):
    sep = "/"
    # trim \n and split line by s
    return line.replace("\n", "").split(sep)

# filename = "files.txt"

filename = "files_less.txt"

with open(filename) as f:
    lines = f.readlines()
    # https://www.pythontutorial.net/python-basics/python-map-list/
    iter = map(fn, lines)
    # print(list(iter))
    str = json.dumps(list(iter))
    print(str)
