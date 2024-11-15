

# Stream Example

## Chapter1: Basic

### Replationship between Streams

- fs.Readable
  - extends
    - stream.Readable
- fs.Writable
  - extends:
    - stream.Writable

- stream.Duplex
  - extends:
    - stream.ReadableBase
	- stream.WritableBase

- NodeJS.WritableStream
  - extends:
    - EventEmitter

- NodeJS.ReadableStream
  - extends:
    - EventEmitter
  - isA:
    - asyncIterator

### Basic Operations
#### How to end a stream ?
> By [NodeJS Docs: Event: 'end'](https://nodejs.org/api/stream.html#event-end) The 'end' event is emitted when there is no more data to be consumed from the stream.
> The 'end' event will not be emitted unless the data is completely consumed. This can be accomplished by switching the stream into flowing mode, or by calling `stream.read()` repeatedly until all data has been consumed.

#### `finish` v.s. `end` event
[Event: 'finish'](https://nodejs.org/api/stream.html#event-finish)

#### `finished` helper function at `node:stream/promises`
Wait for streams to be finished.
Ref: [NodeJS Docs: stream.finished](https://nodejs.org/api/stream.html#streamfinishedstream-options)

#### pipeline in node:stream/promises 

- Works well with Async / Await
- Auto cleanup
- Best practice
- [Stream Pipelines in Node.js](https://blog.dennisokeeffe.com/blog/2024-07-13-stream-pipelines-in-nodejs)

#### Stream: Two Reading modes
- [NodeJS docs](https://nodejs.org/api/stream.html#two-reading-modes)
- flowing mode
- pause mode

## Chapter2: Flow control

### Concepts

#### Back pressure
- https://nodejs.org/en/learn/modules/backpressuring-in-streams


#### Highwatermark
Related Variables:
- [`writableLength`](https://nodejs.org/api/stream.html#writablewritablelength)
> This property contains the number of bytes (or objects) in the queue ready to be written. The value provides introspection data regarding the status of the highWaterMark.
- [`readableLength`](https://nodejs.org/api/stream.html#readablereadablelength)
> This property contains the number of bytes (or objects) in the queue ready to be read. The value provides introspection data regarding the status of the highWaterMark.

NOTE:
> Writable and Readable has different behavior on `highWaterMark`
- [Readable](https://nodejs.org/api/stream.html#readableread0)
- [Writable](https://nodejs.org/api/stream.html#writablewritechunk-encoding-callback)
#### Some related Events

##### Drain
- stream.Writable specific event
Ref: [NodeJS Docs: Event: drain](https://nodejs.org/api/stream.html#event-drain)

- When `stream.write()` return `false`, writer needs to wait for `drain` event will be emitted -> can write more data
- When `stream.write()` return `true` -> can write

## Chapter3: 

Great blog about nodejs stream and buffers

https://blog.dennisokeeffe.com/blog/series/node-js-streams

---- 
oFIIXMEJka