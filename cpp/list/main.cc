#include <iostream>
#include <list>

using namespace std;

int main(int argc, char *argv[]) {
  list<string> l;

  l.push_back("a");
  l.push_back("b");
  l.push_back("c");

  l.push_front("z");
  l.push_front("x");

  auto front_it = l.begin();

  for (auto it = l.begin(); it != l.end(); it++) {
    cout << "e: " << *it << "\t";
  }

  cout << "\n" << endl;

  for (auto it = l.rbegin(); it != l.rend(); it++) {
    cout << "e: " << *it << "\t";
  }

  return 0;
}
