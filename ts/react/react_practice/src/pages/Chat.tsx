import { useEffect, useState } from 'react';
import './Chat.css'

export default function Chat() {
	  const [healthStatus, setHealthStatus] = useState<string>(''); 
		// {"database":"Connected","foo":"bar","externalApi":"Responsive","status":"ok"}⏎                                                                                                                                                     

		useEffect(()=> {
			const interval = setInterval(async () => {
				try {
					const response = await fetch('http://localhost:8080/healthz');
					const data = await response.text();
					setHealthStatus(data);
				}catch(error) {
					console.error('Health check failed:', error)
					setHealthStatus('Error: Health check failed')
				}
			}, 1000);

			return () => clearInterval(interval);
		})
    return (
        <div className="chat-container">
					<div className="messages">
						A: Hello
						B: World
						{ healthStatus }
					</div>
					<div className="input-field"></div>
					<div className="submit">Submit</div>
				</div>
    )
} 