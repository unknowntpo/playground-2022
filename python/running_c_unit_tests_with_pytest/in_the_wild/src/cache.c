
#include <stdbool.h>
#include <stdio.h>

#include "cache.h"

#define isvalid(x) ((x) != NULL)

// ----------------------------------------------------------------------------
queue_item_t *
queue_item_new(value_t value, key_dt key)
{
    queue_item_t *item = (queue_item_t *)calloc(1, sizeof(queue_item_t));

    item->value = value;
    item->key = key;

    return item;
}

// ----------------------------------------------------------------------------
void queue_item__destroy(queue_item_t *self, void (*deallocator)(value_t))
{
    if (!isvalid(self))
        return;

    deallocator(self->value);

    free(self);
}

// ----------------------------------------------------------------------------
queue_t *
queue_new(int capacity, void (*deallocator)(value_t))
{
    queue_t *queue = (queue_t *)calloc(1, sizeof(queue_t));

    queue->capacity = capacity;
    queue->deallocator = deallocator;

    return queue;
}

// ----------------------------------------------------------------------------
int queue__is_full(queue_t *queue)
{
    return queue->count == queue->capacity;
}

// ----------------------------------------------------------------------------
int queue__is_empty(queue_t *queue)
{
    return queue->rear == NULL;
}

// ----------------------------------------------------------------------------
value_t
queue__dequeue(queue_t *queue)
{
    if (queue__is_empty(queue))
        return NULL;

    if (queue->front == queue->rear)
        queue->front = NULL;

    queue_item_t *temp = queue->rear;
    queue->rear = queue->rear->prev;

    if (queue->rear)
        queue->rear->next = NULL;

    void *value = temp->value;
    free(temp);

    queue->count--;

    return value;
}

// ----------------------------------------------------------------------------
queue_item_t *
queue__enqueue(queue_t *self, value_t value, key_dt key)
{
    if (queue__is_full(self))
        return NULL;

    queue_item_t *temp = queue_item_new(value, key);
    temp->next = self->front;

    if (queue__is_empty(self))
        self->rear = self->front = temp;
    else
    {
        self->front->prev = temp;
        self->front = temp;
    }

    self->count++;

    return temp;
}

// ----------------------------------------------------------------------------
void queue__destroy(queue_t *self)
{
    if (!isvalid(self))
        return;

    queue_item_t *next = NULL;
    for (queue_item_t *item = self->front; isvalid(item); item = next)
    {
        next = item->next;
        queue_item__destroy(item, self->deallocator);
    }

    free(self);
}