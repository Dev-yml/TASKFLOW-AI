import { forwardRef } from 'react'

const FormInput = forwardRef(({ label, error, icon: Icon, className = '', ...props }, ref) => (
  <div className="space-y-1.5">
    {label && (
      <label className="block text-[13px] font-medium text-gray-700 dark:text-[#E6EDF3]">
        {label}
      </label>
    )}
    <div className="relative">
      {Icon && (
        <div className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400 dark:text-[#6E7681] pointer-events-none">
          <Icon size={15} />
        </div>
      )}
      <input
        ref={ref}
        className={`
          w-full ${Icon ? 'pl-9' : 'px-3'} pr-3 py-2 text-sm
          bg-white dark:bg-[#0D1117]
          border rounded-md
          text-gray-900 dark:text-[#E6EDF3]
          placeholder:text-gray-400 dark:placeholder:text-[#6E7681]
          transition-colors duration-150
          focus:outline-none focus:border-violet-500 focus:ring-2 focus:ring-violet-500/20
          disabled:opacity-50 disabled:cursor-not-allowed
          ${error
            ? 'border-red-400 dark:border-[#F85149]/70 focus:border-red-400 focus:ring-red-400/20'
            : 'border-gray-300 dark:border-[#30363D]'}
          ${className}
        `}
        {...props}
      />
    </div>
    {error && (
      <p className="text-[12px] text-red-600 dark:text-[#F85149]">{error}</p>
    )}
  </div>
))

FormInput.displayName = 'FormInput'
export default FormInput
