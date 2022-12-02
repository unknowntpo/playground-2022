def fn(line):
    sep = "/"
    # trim \n and split line by s
    return line.replace("\n", "").split(sep)

with open('files.txt') as f:
    lines = f.readlines()
    # https://www.pythontutorial.net/python-basics/python-map-list/
    iter = map(fn, lines)
    print(list(iter))
