import { CheckIcon } from '@heroicons/react/24/solid';

interface Step {
  id: string;
  title: string;
  status: 'pending' | 'active' | 'completed';
  steps?: Step[];  // recursive - a step can contain more steps
  level?: number;  // to track nesting level
}

const StepItem = ({ step, level = 0 }: { step: Step; level?: number }) => {
  return (
    <div className="flex flex-col gap-2 py-1">
      <div className={`flex items-center gap-3 ${level > 0 ? 'ml-6' : ''}`}>
        <div className={`
          ${level === 0 ? 'w-5 h-5' : 'w-3 h-3'} 
          rounded-full flex items-center justify-center
          transition-all duration-300 ease-in-out
          ${step.status === 'completed' ? 'bg-green-500 scale-110' : 
            step.status === 'active' ? 'bg-blue-500 animate-pulse' : 'bg-gray-200'}
        `}>
          {level === 0 && step.status === 'completed' && (
            <CheckIcon className="w-3 h-3 text-white animate-scale-in" />
          )}
        </div>
        <span className={`
          ${level === 0 ? 'font-medium' : 'text-sm'} 
          text-gray-700
          transition-colors duration-300
          ${step.status === 'active' ? 'text-blue-600' : ''}
          ${step.status === 'completed' ? 'text-green-600' : ''}
        `}>
          {step.title}
        </span>
      </div>

      {/* Nested steps */}
      {step.steps && (
        <div className="ml-6 border-l-2 border-gray-100 pl-4 space-y-2">
          {step.steps.map((subStep) => (
            <StepItem key={subStep.id} step={subStep} level={level + 1} />
          ))}
        </div>
      )}
    </div>
  );
};

const ProgressBar = ({ steps }: { steps: Step[] }) => {
  return (
    <div className="flex flex-col gap-2 p-4 rounded-lg bg-gray-50 transition-all duration-300">
      {steps.map((step) => (
        <StepItem key={step.id} step={step} />
      ))}
    </div>
  );
};

export default ProgressBar;