#include "string"

using namespace std;

class Toy {
private:
  string name_;
  int price_;

public:
  Toy(string name, int price);
  auto Show() -> string;
};
