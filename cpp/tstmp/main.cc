#include <chrono>
#include <iostream>

int main() {
  using namespace std::chrono;

  // Get the current time point
  auto now = system_clock::now();

  // Convert the time point to Unix timestamp with nanosecond precision
  auto timestamp = time_point_cast<nanoseconds>(now).time_since_epoch().count();

  // Print the timestamp
  std::cout << "Unix timestamp (nanoseconds): " << timestamp << std::endl;

  return 0;
}
