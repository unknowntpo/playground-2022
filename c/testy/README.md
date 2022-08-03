# Build the target

Set up project directory

```
$ project_dir=$(pwd)
```

```
$ mkdir -p ${project_dir}/build
$ cd build
$ cmake ..
```

And inside `${project_dir}/build`, run

```
$ make
```

# Run the tests
Note: you need to build the target first.
```
$ cd ${project_dir}/build
$ ctest
```

You will see something like:

```
Test project /Users/unknowntpo/repo/unknowntpo/playground-2022/c/testy/build
    Start 1: customer
1/2 Test #1: customer .........................   Passed    0.00 sec
    Start 2: widget
2/2 Test #2: widget ...........................   Passed    0.00 sec

100% tests passed, 0 tests failed out of 2
```
