// import { useEffect, useState } from 'react';
import './CalculatorButton.css'

interface CalculatorButtonProps 
{
	value: string;
}

export default function CalculatorButton({value}: CalculatorButtonProps) {
	return (
		<div>
			{value}
		</div>
	)
} 