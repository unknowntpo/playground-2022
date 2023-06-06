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

set(TESTS
  hello
  replacing_space
  reverse_linked_list
  lru_cache
)

# Create an empty list for the cropped file names
set(TEST_FILES "")
include(GoogleTest)

foreach(T ${TESTS})
  message(STATUS "test: ${T}")
  set(TEST_FILE ${T}_test.cc)
  add_executable(${T} ${TEST_FILE})
  target_link_libraries(
    ${T}
    GTest::gtest_main
  ) 
  message(STATUS "TEST_FILE", ${TEST_FILE})
  gtest_discover_tests(${T})
  list(APPEND TEST_FILES ${TEST_FILE})
endforeach()

message(STATUS " test files: ${TEST_FILES}")
