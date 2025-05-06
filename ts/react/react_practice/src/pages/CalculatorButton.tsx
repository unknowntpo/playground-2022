// import { useEffect, useState } from 'react';
import './CalculatorButton.css'

export interface CalculatorButtonProps 
{
	value: string;
	onClick: (value: CalculatorButtonProps["value"]) => void;
}

export default function CalculatorButton({value, onClick}: CalculatorButtonProps) {
	return (
		<div className="w-12 h-12 flex items-center justify-center bg-gray-200 rounded-lg cursor-pointer 
	  hover:bg-grey-300
		" onClick={() => onClick(value)}>
			{value}
		</div>
	)
} 