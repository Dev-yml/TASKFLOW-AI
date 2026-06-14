import { forwardRef, useState } from 'react'
import { Eye, EyeOff, Lock } from 'lucide-react'

const PasswordInput = forwardRef(({ label, error, showStrength = false, strength, className = '', ...props }, ref) => {
  const [show, setShow] = useState(false)

  return (
    <div className="space-y-1.5">
      {label && (
        <label className="block text-[13px] font-medium text-gray-700 dark:text-[#E6EDF3]">
          {label}
        </label>
      )}
      <div className="relative">
        <div className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400 dark:text-[#6E7681] pointer-events-none">
          <Lock size={15} />
        </div>
        <input
          ref={ref}
          type={show ? 'text' : 'password'}
          className={`
            w-full pl-9 pr-9 py-2 text-sm
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
        <button
          type="button"
          onClick={() => setShow(s => !s)}
          className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 dark:text-[#6E7681] hover:text-gray-600 dark:hover:text-[#8B949E] transition-colors"
        >
          {show ? <EyeOff size={15} /> : <Eye size={15} />}
        </button>
      </div>

      {/* Strength meter */}
      {showStrength && strength && (
        <div className="space-y-1.5 pt-0.5">
          <div className="flex items-center justify-between text-[11px]">
            <span className="text-gray-500 dark:text-[#6E7681]">Password strength</span>
            <span className={
              strength.color === 'red'    ? 'text-red-500' :
              strength.color === 'orange' ? 'text-amber-500' :
              strength.color === 'yellow' ? 'text-yellow-500' :
              'text-emerald-500'
            }>
              {strength.label}
            </span>
          </div>
          <div className="h-1 bg-gray-200 dark:bg-[#21262D] rounded-full overflow-hidden">
            <div
              style={{ width: `${strength.score}%`, transition: 'width 0.3s ease' }}
              className={`h-full rounded-full ${
                strength.color === 'red'    ? 'bg-red-500' :
                strength.color === 'orange' ? 'bg-amber-500' :
                strength.color === 'yellow' ? 'bg-yellow-500' :
                'bg-emerald-500'
              }`}
            />
          </div>
          <div className="grid grid-cols-2 gap-x-4 gap-y-0.5">
            {[
              { ok: strength.checks?.length,    label: '8+ characters' },
              { ok: strength.checks?.uppercase,  label: 'Uppercase letter' },
              { ok: strength.checks?.lowercase,  label: 'Lowercase letter' },
              { ok: strength.checks?.number,     label: 'Number' },
            ].map(({ ok, label }) => (
              <div key={label} className="flex items-center gap-1.5 text-[11px] text-gray-500 dark:text-[#6E7681]">
                <div className={`w-1.5 h-1.5 rounded-full flex-shrink-0 ${ok ? 'bg-emerald-500' : 'bg-gray-300 dark:bg-[#30363D]'}`} />
                {label}
              </div>
            ))}
          </div>
        </div>
      )}

      {error && (
        <p className="text-[12px] text-red-600 dark:text-[#F85149]">{error}</p>
      )}
    </div>
  )
})

PasswordInput.displayName = 'PasswordInput'
export default PasswordInput
