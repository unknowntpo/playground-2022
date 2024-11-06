// @ts-nocheck
import { io } from 'socket.io-client';
import { messages } from './messages.js'

const socket = io('http://localhost:3000');
const topicName = 'lawDiff'

socket.on('connect', () => {
	console.log('Connected to Server A');

	// Send messageA
	socket.emit('messageA', 'Hello from ClientA - MessageA');

	for (const [fieldName, value] of Object.entries(messages)) {
		value;
		console.log(`sending eventName: ${topicName}, ${fieldName}`)
		// please give me fieldA, fieldB
		socket.emit(topicName, fieldName);
	}
});


// Handle responseA
// FIXME: 需針對 fieldName 做切分
socket.on(topicName, (data) => {
	console.log('Received responseA:', data);
});

socket.on('disconnect', () => {
	console.log('Disconnected from Server A');
});
