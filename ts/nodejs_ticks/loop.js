var EventEmitter = require('events');

var crazy = new EventEmitter();

crazy.on('event1', function () {
	console.log('event1 fired!');
	setImmediate(function () {
		crazy.emit('event2');
	});
});

crazy.on('event2', function () {
	console.log('event2 fired!');
	setImmediate(function () {
		crazy.emit('event3');
	});

});

crazy.on('event3', function () {
	console.log('event3 fired!');
	setImmediate(function () {
		crazy.emit('event1');
	});
});

crazy.emit('event1');