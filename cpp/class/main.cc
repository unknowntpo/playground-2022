#include "main.h"
#include <iostream>
#include <string>

using namespace std;

Toy::Toy(string name, int price) : name_(name), price_(price) {}

string Toy::Show() {
  string left = "hello";
  string right = "world";
  return left + right;
}

int main(int argc, char *argv[]) {
  Toy t("car", 3);
  cout << t.Show() << endl;
  return 0;
}
