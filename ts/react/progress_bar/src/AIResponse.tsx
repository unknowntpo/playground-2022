import { useState } from 'react';
import ProgressBar from './ProgressBar';

interface Step {
  id: string;
  title: string;
  status: 'pending' | 'active' | 'completed';
  steps?: Step[];  // recursive - a step can contain more steps
  level?: number;  // to track nesting level
}

export default function AIResponse() {
  const [steps, setSteps] = useState<Step[]>([
    {
      id: '1',
      title: 'Pro Search',
      status: 'pending',
      steps: [
        {
          id: '1-1',
          title: 'Initializing search...',
          status: 'pending'
        }
      ]
    },
    {
      id: '2',
      title: 'Researching',
      status: 'pending',
      steps: [
        {
          id: '2-1',
          title: 'Analyzing query parameters',
          status: 'pending'
        },
        {
          id: '2-2',
          title: 'Fetching relevant data',
          status: 'pending'
        }
      ]
    },
    {
      id: '3',
      title: 'Reasoning with R1',
      status: 'pending'
    }
  ]);

  const [currentIndex, setCurrentIndex] = useState(0);

  const sequence = [
    { id: '1', status: 'active' as const },
    // { id: '1-1', status: 'active' as const },
    // { id: '1-1', status: 'completed' as const },
    { id: '1', status: 'completed' as const },
    { id: '2', status: 'active' as const },
    // { id: '2-1', status: 'active' as const },
    // { id: '2-1', status: 'completed' as const },
    // { id: '2-2', status: 'active' as const },
    // { id: '2-2', status: 'completed' as const },
    { id: '2', status: 'completed' as const },
    { id: '3', status: 'active' as const },
    { id: '3', status: 'completed' as const },
  ];

  const handleUpdate = () => {
    if (currentIndex < sequence.length) {
			console.log(`currentIndex: ${currentIndex}`, sequence[currentIndex]);
      const { id, status } = sequence[currentIndex];
      // setSteps(prev => updateNestedStepStatus(prev, id, status));
			setSteps(prev => updateSequenceStepStatus(prev, id, status));

      setCurrentIndex(prev => prev + 1);
    }
  };

  return (
    <div className="max-w-2xl mx-auto bg-white rounded-lg shadow-sm p-6">
      <ProgressBar steps={steps} />
      <button 
        onClick={handleUpdate}
        className="mt-6 px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 transition-colors"
      >
        Update Progress
      </button>
    </div>
  );
}

// eslint-disable-next-line @typescript-eslint/no-unused-vars
const updateNestedStepStatus = (
  steps: Step[], 
  targetId: string, 
  newStatus: Step['status']
): Step[] => {
  return steps.map(step => {
    console.log(`step: ${step.id}`, step.status);
    if (step.id === targetId) {
			console.log(`newStatus: ${newStatus}`);
      return { ...step, status: newStatus };
    }
    if (step.steps) {
      return {
        ...step,
        steps: updateNestedStepStatus(step.steps, targetId, newStatus)
      };
    }
    return step;
  });
};

const updateSequenceStepStatus = (
  steps: Step[], 
  targetId: string, 
  newStatus: Step['status']
): Step[] => {
  return steps.map(step => {
    console.log(`step: ${step.id}`, step.status);
    if (step.id === targetId) {
			console.log(`newStatus: ${newStatus}`);
      return { ...step, status: newStatus };
    }
    return step;
  });
}