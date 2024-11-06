// @ts-nocheck
import { io } from 'socket.io-client';
import { messages } from './messages.js'

const socket = io('http://localhost:3000');
const eventName = 'lawDiff'

socket.on('connect', () => {
	console.log('Connected to Server A');

	// Send messageA
	socket.emit('messageA', 'Hello from ClientA - MessageA');

	for (const [fieldName, value] of Object.entries(messages)) {
		value;
		console.log(`sending eventName: ${eventName}, ${fieldName}`)
		socket.emit(eventName, fieldName);
	}
});


// Handle responseA
socket.on(eventName, (data) => {
	console.log('Received responseA:', data);
});

socket.on('disconnect', () => {
	console.log('Disconnected from Server A');
});
