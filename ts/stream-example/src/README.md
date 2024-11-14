

# Stream Example

## Chapter1: Basic

### Replationship between Stream 

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

- Readable stream
https://blog.dennisokeeffe.com/blog/2024-07-08-readable-streams-in-nodejs

### How to end a stream ?
> By [NodeJS Docs: Event: 'end'](https://nodejs.org/api/stream.html#event-end) The 'end' event is emitted when there is no more data to be consumed from the stream.
> The 'end' event will not be emitted unless the data is completely consumed. This can be accomplished by switching the stream into flowing mode, or by calling `stream.read()` repeatedly until all data has been consumed.

### Two Reading modes
- [NodeJS docs](https://nodejs.org/api/stream.html#two-reading-modes)
#### When to switch ?
##### flowing mode
##### pause mode

- Read from file and write to another file
- Read from Readable string
- stream.on 
- Write to stream
- Use Transfer to filter

## Chapter2: Flow control

### Highwatermark
- `writableLength`
> This property contains the number of bytes (or objects) in the queue ready to be written. The value provides introspection data regarding the status of the highWaterMark.
- `readableLength`
> This property contains the number of bytes (or objects) in the queue ready to be read. The value provides introspection data regarding the status of the highWaterMark.
### Events

#### Drain
- stream.Writable specific event
Ref: [NodeJS Docs: Event: drain](https://nodejs.org/api/stream.html#event-drain)

- When `stream.write()` return `false`, `drain` event will be emitted -> can write more data
- When `stream.write()` return `true` -> writer needs to WAIT!

### Back pressure
- https://nodejs.org/en/learn/modules/backpressuring-in-streams

## Chapter3: 

Great blog about nodejs stream and buffers

https://blog.dennisokeeffe.com/blog/series/node-js-streams

---- 
oFIIXMEJka