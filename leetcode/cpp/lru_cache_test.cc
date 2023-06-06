#include <gtest/gtest.h>
#include <list>
#include <unordered_map>

using namespace std;

class LRUCache {
private:
  int capacity;
  list<pair<int, int>> dl;
  unordered_map<int, list<pair<int, int>>::iterator> um;

public:
  LRUCache(int capacity) { this->capacity = capacity; }

  int get(int key) {
    if (um.find(key) == um.end()) {
      return -1;
    }
    dl.splice(dl.begin(), dl, um[key]);
    return um[key]->second;
  }

  void put(int key, int value) {
    if (um.find(key) != um.end()) {
      dl.erase(um[key]);
    } else if (dl.size() == capacity) {
      int delKey = dl.back().first;
      dl.pop_back();
      um.erase(delKey);
    }
    dl.push_front({key, value});
    um[key] = dl.begin();
  }
};

TEST(LRUCache, cache) {
  LRUCache *cache = new LRUCache(2);
  cache->put(1, 1);            // cache is {1=1}
  cache->put(2, 2);            // cache is {1=1, 2=2}
  EXPECT_EQ(cache->get(1), 1); // return 1
  cache->put(3, 3); // LRU key was 2, evicts key 2, cache is {1=1, 3=3}
  EXPECT_EQ(cache->get(2), -1); // returns -1 (not found)
  cache->put(4, 4); // LRU key was 1, evicts key 1, cache is {4=4, 3=3}
  EXPECT_EQ(cache->get(1), -1); // return -1 (not found)
  EXPECT_EQ(cache->get(3), 3);  // return 3
  EXPECT_EQ(cache->get(4), 4);  // return 4
                                //
  GTEST_LOG_(INFO) << "OK" << endl;
}
