// import { useEffect, useState } from 'react';
import './Calculator.css'
import CalculatorButton from './CalculatorButton';

export default function Calculator() {
	const arr: string[] = ["1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "+", "-", "x", "%"];

	return (
		<div className="flex flex-row">
			{arr.map((char) => (
					<CalculatorButton value={char} />
			))}

		</div>
	)
} 