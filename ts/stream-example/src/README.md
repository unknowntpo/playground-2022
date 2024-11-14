

# Stream Example

## Chapter1: Basic

Stream:
- Relationship

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

How to end a stream ?
    
 
 - mode
  - flowing
  - paused

- Read from file and write to another file
- Read from Readable string
- stream.on 
- Write to stream
- Use Transfer to filter

## Chapter2: Flow control

### Highwatermark
- writableLength
> This property contains the number of bytes (or objects) in the queue ready to be written. The value provides introspection data regarding the status of the highWaterMark.
- readableLength
> This property contains the number of bytes (or objects) in the queue ready to be read. The value provides introspection data regarding the status of the highWaterMark.
### Writable: Drain Event

#### When?
write stream resumed.

### Not flushed problem:
https://github.com/nodejs/node/issues/34274

### Back pressure
- https://nodejs.org/en/learn/modules/backpressuring-in-streams

## Chapter3: 

Great blog about nodejs stream and buffers

https://blog.dennisokeeffe.com/blog/series/node-js-streams