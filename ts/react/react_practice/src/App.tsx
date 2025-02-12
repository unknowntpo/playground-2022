import Task, { taskData } from './components/Task'

function App() {
  return (
    <div className="min-h-screen bg-blue-100 p-8">
      <div className="max-w-4xl mx-auto hidden">
        <h1 className="text-5xl text-red-500 font-bold">
          Task Manager
        </h1>
        <Task 
          subTasks={taskData} 
          level={0}
        />
      </div>
			<div>
					<ol className="list-desc">
						<li className="hover:bg-slate-10">First item</li>
						<li>Second item</li>
						<li>Third item</li>
						<li>Fourth item</li>
						<li>Fifth item</li>
					</ol>
			</div>
    </div>
  )
}

export default App
