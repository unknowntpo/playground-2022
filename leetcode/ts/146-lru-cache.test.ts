import { describe, expect, test, it } from '@jest/globals';


class LRUCache<K, V> {
  map: Map<K, V>
  constructor() {
    this.map = new Map<K, V>()
  }

  get(key: K): V | undefined {
    return undefined
  }

  put(key: K, value: V) {

  }
}

describe(`normal`, () => {
  let cache = new LRUCache()
  cache.put(1, 1)
  cache.put(2, 2)
  expect(cache.get(1)).toEqual(1)
  cache.put(3, 3)
  expect(cache.get(2)).toEqual(-1)
  cache.put(4, 4)
  expect(cache.get(1)).toEqual(-1)
  expect(cache.get(3)).toEqual(3)
  expect(cache.get(4)).toEqual(4)
})

/*
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
*/
