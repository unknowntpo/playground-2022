#include <iostream>
#include <list>

using namespace std;

template <typename T> void print_list(list<T> *pList) {
  list<T> l = *pList;
  for (auto it = l.begin(); it != l.end(); it++) {
    cout << "e: " << *it << "\t";
  }
  cout << endl;
}

int main(int argc, char *argv[]) {
  list<string> l;

  l.push_back("a");
  l.push_back("b");
  l.push_back("c");

  l.push_front("z");
  l.push_front("x");

  string target = "a";
  auto a_it = l.begin();

  for (auto it = l.begin(); it != l.end(); it++) {
    // find target
    if (*it == target) {
      a_it = it;
    }
    cout << "e: " << *it << "\t";
  }

  cout << "\n" << endl;

  for (auto it = l.rbegin(); it != l.rend(); it++) {
    cout << "e: " << *it << "\t";
  }

  // find 'a', move it to front
  //
  l.splice(l.begin(), l, a_it);

  cout << "\nafter moving target " << target << " to front: " << endl;

  print_list(&l);

  return 0;
}
