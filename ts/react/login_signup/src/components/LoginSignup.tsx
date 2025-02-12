import user_icon from '../assets/person.png'
import password_icon from '../assets/password.png'
import email_icon from '../assets/email.png'
import './LoginSignup.css'
import { useState } from 'react'


const LoginSignup = () => {
	const [action, setAction] = useState("Login");
	return <div>
		<div className='container'>
			<div className='header'>
				<div className='text'>{action}</div>
				<div className='underline'></div>
			</div>
			<div className="inputs">
				<div className="input">
					<img src={user_icon} alt="" />
					<input type="text" placeholder='Username' />
				</div>
				<div className="input">
					<img src={password_icon} alt="" />
					<input type="text" placeholder='Password' />
				</div>
				<div className="input">
					<img src={email_icon} alt="" />
					<input type="text" placeholder='Email' />
				</div>
			</div>
			<div className="forgot-password">Forgot Password? <span>Click Here</span></div>
			<div className="submit-container">
				<div className={ action === "Signup" ? "submit grey" : "submit" } onClick={() => setAction("Signup")}>Sign up</div>
				<div className={ action === "Login" ? "submit grey" : "submit" } onClick={() => setAction("Login")}>Login</div>
			</div>
		</div>
	</div>
}

export default LoginSignup
