// import { useState } from 'react'
// import reactLogo from './assets/react.svg'
// import ColorButton from './components/ColorButton'
import Task, { taskData } from './components/Task'

// export default function App() {
//   return (
//     <div className="flex min-h-screen items-center justify-center bg-gray-100">
//       <h1 className="text-4xl font-bold text-blue-600">
//         Hello Tailwind CSS!
//       </h1>
//     </div>
//   )
// }


function App() {
  // const [count, setCount] = useState(10)

	// const decrementCount = () => {
	// 	setCount((count) => count - 1);
	// }

	// const incrementCount= () => {
	// 	setCount((count) => count + 1);
	// }


  return (
    <div className="min-h-screen bg-blue-100 p-8">
      <div className="max-w-4xl mx-auto">
        <h1 className="text-5xl text-red-500 font-bold">
          Task Manager
        </h1>
        <Task 
          subTasks={taskData} 
          level={0}
        />
      </div>
    </div>
  )
}

export default App
