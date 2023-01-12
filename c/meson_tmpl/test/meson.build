test_sources = []

foreach source_path : module_sources
  test_sources += '../src/' + source_path
endforeach

test_module = executable('cdemo_test_module', [test_sources, 'test/add/test-add.c'],
    dependencies : shared_dependencies,
    include_directories: [includes_dir]
)

test('CDemo Test Valid', test_module)
