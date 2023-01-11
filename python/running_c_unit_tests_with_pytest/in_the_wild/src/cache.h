#ifndef CACHE_H
#define CACHE_H

#include <stdint.h>
#include <stdlib.h>

typedef uintptr_t key_dt;
typedef void *value_t;

typedef struct queue_item_t
{
  struct queue_item_t *prev, *next;
  key_dt key;
  value_t value; // Takes ownership of a free-able object
} queue_item_t;

typedef struct queue_t
{
  unsigned count;
  unsigned capacity;
  queue_item_t *front, *rear;
  void (*deallocator)(value_t);
} queue_t;

queue_item_t *queue_item_new(value_t, key_dt);

void queue_item__destroy(queue_item_t *, void (*)(value_t));

queue_t *queue_new(int, void (*)(value_t));

int queue__is_full(queue_t *);

int queue__is_empty(queue_t *);

value_t queue__dequeue(queue_t *);

queue_item_t *queue__enqueue(queue_t *, value_t, key_dt);

void queue__destroy(queue_t *);

#endif /* CACHE_H */