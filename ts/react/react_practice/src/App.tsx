import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import ColorButton from './components/ColorButton'

function App() {
  const [count, setCount] = useState(10)

	const decrementCount = () => {
		setCount((count) => count - 1);
	}

	const incrementCount= () => {
		setCount((count) => count + 1);
	}


  return (
    <>
      <div>
        <a href="https://vite.dev" target="_blank">
          <img src={viteLogo} className="logo" alt="Vite logo" />
        </a>
        <a href="https://react.dev" target="_blank">
          <img src={reactLogo} className="logo react" alt="React logo" />
        </a>
      </div>
      <h1>Vite + React</h1>
      <div className="card">
				<button onClick={incrementCount}>
					+
        </button>
				<ColorButton />
        <p className="count">
					Count is {count}
        </p>
        <button onClick={decrementCount}>
					-
        </button>
				<p>
          Edit <code>src/App.tsx</code> and save to test HMR
        </p>
      </div>
      <p className="read-the-docs">
        Click on the Vite and React logos to learn more
      </p>
    </>
  )
}

export default App
