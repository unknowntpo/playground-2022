#include <iostream>
#include <unordered_map>

using namespace std;

int main() {
  unordered_map<string, int> um = {{"Tom", 1}, {"Jeff", 2}, {"Alice", -1}};
  for (auto &n : um) {
    cout << "n: " << n.first << ": " << n.second << endl;
  }
  return 0;
}
