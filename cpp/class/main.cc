#include <iostream>
#include <string>

using namespace std;

class Toy {
private:
  string name;
  int price;

public:
  Toy(string name, int price) {
    name = name;
    price = price;
  }
  string Show();
};

string Toy::Show() {
  string a = "hello";
  string b = "world";
  return a + b;
}

int main(int argc, char *argv[]) {
  Toy t("car", 3);
  cout << t.Show() << endl;
  return 0;
}
