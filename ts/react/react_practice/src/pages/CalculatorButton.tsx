// import { useEffect, useState } from 'react';
import './CalculatorButton.css'

export interface CalculatorButtonProps 
{
	keyStroke: string;
	onClick: (value: CalculatorButtonProps["keyStroke"]) => void;
}

export default function CalculatorButton({keyStroke, onClick}: CalculatorButtonProps) {
	return (
		<div className="w-12 h-12 flex items-center justify-center bg-gray-200 rounded-lg cursor-pointer 
	  hover:bg-grey-300
		" onClick={() => onClick(keyStroke)}>
			{keyStroke}
		</div>
	)
} 