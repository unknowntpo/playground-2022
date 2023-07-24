#include "unordered_map"
#include "iostream"
#include "string"

using namespace std;

class MapNode {
private:
  string key_;
  string value_;

public:
  explicit MapNode(string key, string value);
  string Show();
};

MapNode::MapNode(string key, string value): key_(key), value_(value){} 

string MapNode::Show(){
  size_t size = key_.size() + value_.size() + 1;
  char buffer[size];
  snprintf(buffer,size,  "%s%s", key_.c_str(), value_.c_str());
  return buffer;
}

int main(int argc, char *argv[]) {
  unordered_map<string, MapNode> um;
  string key = "hello";
  string value = "world";
  um[key] = MapNode(key, value);
  //um.emplace(key, MapNode(key, value));

  for (auto &n: um) {
    cout << n.second.Show()<< endl;
  }

  return 0;
}
