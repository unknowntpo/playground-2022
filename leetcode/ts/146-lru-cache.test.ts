import { describe, expect, test, it } from '@jest/globals';

interface KeyValue<K, V> {
  key: K
  value: V
}

class LRUCache<K, V> {
  // map from key to index
  map: Map<K, number>
  list: Array<KeyValue<K, V>>
  cap: number
  constructor(cap: number) {
    // map from key to index of this key in list
    this.map = new Map<K, number>()
    this.list = []
    this.cap = cap
  }

  get(key: K): V | undefined {
    if (this.map.has(key)) {
      let idx: number = this.map.get(key)!
      this.moveToBack(idx)
      // update its index to 0
      this.map.set(key, 0)
      return this.list[this.list.length - 1].value
    }
    return undefined
  }

  /*
* [{1, 1}, {2, 2}]
*
* */

  moveToBack(idx: number) {
    let kv = this.list.splice(idx, 1)[0]
    this.list.push(kv)
    this.map.set(kv.key, this.list.length - 1)
  }

  put(key: K, value: V) {
    if (this.map.has(key)) {
      // find the index of the key in list, move it to back of list  
      let idx: number = this.map.get(key)!
      this.moveToBack(idx)
    } else {
      // try to append to list
      if (this.list.length == this.cap) {
        // got head
        // evict head first
        let head = this.list.shift()
        this.map.delete(head!.key)
      }
      this.list.push({ key: key, value: value })
      this.map.set(key, this.list.length - 1)
    }
  }
}

describe(`normal`, () => {
  let cache = new LRUCache(2)
  cache.put(1, 1)
  cache.put(2, 2)
  expect(cache.get(1)).toEqual(1)
  cache.put(3, 3)
  expect(cache.get(2)).toEqual(undefined)
  cache.put(4, 4)
  expect(cache.get(1)).toEqual(undefined)
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
