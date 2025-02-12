import user_icon from '../assets/person.png'
import password_icon from '../assets/password.png'
import email_icon from '../assets/email.png'
import './LoginSignup.css'


const LoginSignup = () => {
	return <div>
		<div className='container'>
			<div className='header'>
				<div className='text'>Sign up</div>
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
				<div className="submit">Sign up</div>
				<div className="submit">Login</div>
			</div>
		</div>
	</div>
}

export default LoginSignup
