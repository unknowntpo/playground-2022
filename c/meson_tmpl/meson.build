project('cdemo', 'c')

# Demo module sources
module_sources = [
    'module/add.c'
]

# Dependencies
shared_dependencies = [
    dependency('glib-2.0')
]

includes_dir = include_directories([
    'include',
    'include/add'
])

subdir('src')
subdir('test')
