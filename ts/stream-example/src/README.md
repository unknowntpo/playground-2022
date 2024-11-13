

# Stream Example

Chapter1: Basic

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
    
 
 
- mode
  - flowing
  - paused

- Read from file and write to another file
- Read from Readable string
- stream.on 
- Write to stream
- Use Transfer to filter

Chapter2: Flow control

Highwatermark
Drain

Not flushed problem:
https://github.com/nodejs/node/issues/34274

Back pressure
- https://nodejs.org/en/learn/modules/backpressuring-in-streams

Chapter3: 

