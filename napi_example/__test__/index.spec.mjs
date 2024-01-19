import test from 'ava'

import { sum } from '../index.js'

import { fibonacci } from '../index.js'


test('sum from native', (t) => {
  t.is(sum(1, 2), 3)
})

test('fibonacci from native', (t) => {
  t.is(fibonacci(3), 2)
})
