import { useState } from "react";

export default function Counter() {
	const [count, setCount] = useState(0);

	return (
		<div className="flex flex-col items-center justify-center gap-4 p-6 bg-lime-200">
			<div className="text-2xl font-bold">
				Count: {count}
			</div>
			<div className="flex gap-2">
				<button onClick={() => setCount(count+1)} className="px-4 py-2 text-white bg-blue-500 rounded-md hover:bg-blue-600">+</button>
				<button onClick={() => setCount(count-1)} className="px-4 py-2 text-white bg-red-500 rounded-md hover:bg-red-600">-</button>
			</div>
		</div>
	)
}