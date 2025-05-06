// import { useEffect, useState } from 'react';
import { useState } from 'react';
import './Calculator.css'
import CalculatorButton, { CalculatorButtonProps } from './CalculatorButton';

export default function Calculator() {
	const initExpression = '0';
	 
	const [expression, setExpression] = useState(initExpression);
	const rows: string[][] = [
		["AC", "+/-", "%", "÷"],
		["7", "8", "9", "x"],
		["4", "5", "6", "-"],
		["1", "2", "3", "+"],
		["", "0", ".", "="]
	];

	const handleClick = (char: CalculatorButtonProps["keyStroke"]) => {
		console.log(`${char} is clicked`)
		setExpression(prevExpression => prevExpression === initExpression ? char : prevExpression + char);
	}

	return (
		<div className="flex flex-col gap-2"> {/* Changed to column layout with gap */}
		<div className="bg-green-50">{expression}</div>
		{rows.map((row, rowIndex) => (
				<div key={rowIndex} className="flex flex-row gap-2"> {/* Row container with gap */}
						{row.map((char) => (
								<CalculatorButton key={char} keyStroke={char} onClick={handleClick}/>
						))}
				</div>
		))}
	</div>
	)
} 