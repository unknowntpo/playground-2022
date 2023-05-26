cmake_minimum_required(VERSION 3.14)
project(my_project)

set(CMAKE_CXX_STANDARD 14)

include(FetchContent)
FetchContent_Declare(
  googletest
  URL https://github.com/google/googletest/archive/03597a01ee50ed33e9dfd640b249b4be3799d395.zip
)

# For Windows: Prevent overriding the parent project's compiler/linker settings
set(gtest_force_shared_crt ON CACHE BOOL "" FORCE)
FetchContent_MakeAvailable(googletest)

enable_testing()

# TODO: Use foreach stuff to avoid do this everytime

# Hello
add_executable(
  hello_test
  hello_test.cc
)
target_link_libraries(
  hello_test
  GTest::gtest_main
)

# replacing_space
add_executable(
  replacing_space_test
  replacing_space_test.cc
)
target_link_libraries(
  replacing_space_test
  GTest::gtest_main
)

# reverse_linked_list
add_executable(
  reverse_linked_list
  reverse_linked_list_test.cc
)
target_link_libraries(
  reverse_linked_list
  GTest::gtest_main
)

include(GoogleTest)
gtest_discover_tests(hello_test)
gtest_discover_tests(replacing_space_test)
gtest_discover_tests(reverse_linked_list)


set(TESTS
  a
  b
  c
)

# Create an empty list for the cropped file names
set(TEST_FILES "")
foreach(T ${TESTS})
  message(STATUS "test: ${T}")
  set(TEST_FILE ${T}_test.cc)
  list(APPEND TEST_FILES ${TEST_FILE})
endforeach()

message(STATUS " test files: ${TEST_FILES}")
