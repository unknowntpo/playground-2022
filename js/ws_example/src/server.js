// @ts-nocheck
import express from 'express';
import { createServer } from 'http';
import { Server } from 'socket.io';
import { messages } from './messages.js'

const app = express();
const server = createServer(app);
const io = new Server(server);

const eventName = 'lawDiff'

io.on('connection', (socket) => {
	console.log('Server A: Client connected');

	socket.on(eventName, (data) => {
		console.log(`Received event: ${eventName}, data: ${data}`);
		socket.emit(eventName, messages[data])
	});

	socket.on('disconnect', () => {
		console.log('Server A: Client disconnected');
	});
	// TODO: 需要一個 isEnd 的 topic
});

server.listen(3000, () => {
	console.log('Server A is running on port 3000');
});
