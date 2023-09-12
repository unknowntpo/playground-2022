import json

users = [
    {"name": "Alice", "age": 28, "earn": 50000},
    {"name": "Bob", "age": 35, "earn": 60000},
    {"name": "Charlie", "age": 22, "earn": 45000},
    {"name": "David", "age": 31, "earn": 70000},
    {"name": "Eve", "age": 27, "earn": 55000},
    {"name": "Frank", "age": 40, "earn": 80000},
    {"name": "Grace", "age": 33, "earn": 72000},
    {"name": "Hannah", "age": 29, "earn": 61000},
    {"name": "Joseph", "age": 29, "earn": 59000},
    {"name": "Ian", "age": 24, "earn": 49000},
    {"name": "Jack", "age": 37, "earn": 75000}
]

exceed50k = list(filter(lambda x: x['earn'] >= 50000, users))

print(json.dumps(exceed50k, indent=4))

sortByAgeEarn = list(sorted(exceed50k, key=lambda u: (u['age'], -u['earn'])))

print(json.dumps(sortByAgeEarn, indent=4))
