#include <iostream>
#include <list>
#include <unordered_map>

int main(int argc, char *argv[]) {
  std::unordered_map<int, std::list<int>::iterator> um;

  std::list<int> l;

  for (int i = 0; i < 6; i++) {
    l.push_back(i);
    // FIXME: Why l.rbegin().base() is changed ?
    // um[i] = (--l.rbegin().base());
    um[i] = std::prev(l.end());
  }

  for (const auto &n : l) {
    std::cout << n << std::endl;
  }

  std::cout << "in um: " << std::endl;

  for (auto it = um.begin(); it != um.end(); it++) {
    std::cout << "key: " << it->first << " value: " << *it->second
              << " addr of iterator: " << &(it) << std::endl;
  }
  return 0;
}
