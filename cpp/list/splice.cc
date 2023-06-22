/******************************************************************************

Welcome to GDB Online.
  GDB online is an online compiler and debugger tool for C, C++, Python, PHP,
Ruby, C#, OCaml, VB, Perl, Swift, Prolog, Javascript, Pascal, COBOL, HTML, CSS,
JS Code, Compile, Run and Debug online from anywhere in world.

*******************************************************************************/
#include <iostream>
#include <list>
#include <stdio.h>

using namespace std;

int main() {
  printf("Hello World");
  list<int> list1 = {1, 2, 3};
  list<int> list2 = {4, 5, 6};

  for (const int &e : list1) {
    cout << "e: " << e << endl;
  }

  // move 1 from list1 to the begining of list2
  list2.splice(list2.begin(), list1, list1.begin(), ++list1.begin());

  cout << "list1" << endl;
  for (const int &e : list1) {
    cout << "e: " << e << " ";
  }

  cout << endl;

  cout << "list2" << endl;
  for (const int &e : list2) {
    cout << "e: " << e << " ";
  }

  cout << endl;
  return 0;
}
