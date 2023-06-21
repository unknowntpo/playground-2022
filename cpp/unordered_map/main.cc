#include <iostream>
#include <unordered_map>

using namespace std;

int main() {
  unordered_map<string, int> um = {{"Tom", 1}, {"Jeff", 2}, {"Alice", -1}};
  for (auto &n : um) {
    cout << "n: " << n.first << ": " << n.second << endl;
  }

  cout << um.find("Tom")->first << endl;

  // Use it == um.end() to check if this key exists or not
  auto it = um.find("Kevin");
  cout << (it == um.end()) << endl;

  um.erase("Tom");

  for (auto &n : um) {
    cout << "n: " << n.first << ": " << n.second << endl;
  }

  //  cout << um.find("Tom").first << end;
  return 0;
}
