import { useState } from 'react';

export default function Chat() {
		const [messages, setMessages] = useState<string[]>([]); 
		const [inputValue, setInputValue] = useState<string>(''); 

		const handleSubmit = (e: React.FormEvent) => {
			e.preventDefault();
			messages.push(inputValue);
			setMessages(messages);
			setInputValue('');
		};
    return (
        <div className="chat-container">
					<div className="messages">
						{messages.map((msg, index) => (
							<div key={index} className="message">{msg}</div>
						))}
					</div>
					<form onSubmit={handleSubmit} className="flex">
						<div className="input-field bg-green flex-grow">
							<input
								value={inputValue}
								onChange={e => setInputValue(e.target.value)}
								type="text"
								placeholder="Type your message"
								className="w-full bg-amber-50"
								/>
						</div>
						<button type="submit" className="submit border-2">Submit</button>
					</form>
				</div>
    )
} 