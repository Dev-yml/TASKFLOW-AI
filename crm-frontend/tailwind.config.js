/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  darkMode: 'class',
  theme: {
    extend: {
      fontFamily: {
        sans: ['Inter', 'system-ui', '-apple-system', 'sans-serif'],
      },
      colors: {
        // GitHub/Linear-inspired design system
        gh: {
          // Dark surfaces
          canvas:   '#0D1117',
          surface:  '#161B22',
          overlay:  '#1C2128',
          inset:    '#010409',
          // Borders
          border:   '#30363D',
          borderMuted: '#21262D',
          // Text
          text:     '#E6EDF3',
          textMuted:'#8B949E',
          textSubtle:'#6E7681',
          // Accent
          accent:   '#7C3AED',
          accentHover: '#6D28D9',
          accentMuted: 'rgba(124,58,237,0.15)',
          // Status
          success:  '#238636',
          successText: '#3FB950',
          warning:  '#9E6A03',
          warningText: '#D29922',
          danger:   '#DA3633',
          dangerText: '#F85149',
          // Interactive
          hover:    '#21262D',
          selected: '#1F2937',
        },
        // Keep primary for landing page compat
        primary: {
          50: '#f5f3ff',
          100: '#ede9fe',
          200: '#ddd6fe',
          300: '#c4b5fd',
          400: '#a78bfa',
          500: '#8b5cf6',
          600: '#7c3aed',
          700: '#6d28d9',
          800: '#5b21b6',
          900: '#4c1d95',
        },
      },
      animation: {
        'fade-in': 'fadeIn 0.2s ease-out',
        'slide-up': 'slideUp 0.2s ease-out',
        'pulse': 'pulse 2s cubic-bezier(0.4, 0, 0.6, 1) infinite',
      },
      keyframes: {
        fadeIn: {
          '0%': { opacity: '0' },
          '100%': { opacity: '1' },
        },
        slideUp: {
          '0%': { transform: 'translateY(6px)', opacity: '0' },
          '100%': { transform: 'translateY(0)', opacity: '1' },
        },
        pulse: {
          '0%, 100%': { opacity: '1' },
          '50%': { opacity: '.5' },
        },
      },
      backdropBlur: { xs: '2px' },
      boxShadow: {
        'gh': '0 0 0 1px #30363D',
        'gh-md': '0 3px 6px rgba(0,0,0,0.3), 0 0 0 1px #30363D',
        'gh-lg': '0 8px 24px rgba(0,0,0,0.4)',
        'inset-gh': 'inset 0 1px 0 rgba(255,255,255,0.05)',
      },
    },
  },
  plugins: [],
  safelist: [
    // Landing page dynamic classes
    'text-blue-600','text-purple-600','text-green-600','text-orange-600',
    'text-pink-600','text-yellow-600','text-indigo-600','text-cyan-600',
    'text-emerald-600','text-red-600','dark:text-blue-400','dark:text-purple-400',
    'dark:text-green-400','dark:text-orange-400','dark:text-pink-400',
    'dark:text-yellow-400','dark:text-indigo-400','dark:text-cyan-400',
    'dark:text-emerald-400','dark:text-red-400',
    'bg-blue-100','bg-purple-100','bg-green-100','bg-orange-100','bg-pink-100',
    'bg-yellow-100','bg-indigo-100','bg-cyan-100','bg-emerald-100','bg-red-100',
    'dark:bg-blue-900/30','dark:bg-purple-900/30','dark:bg-green-900/30',
    'dark:bg-orange-900/30','dark:bg-pink-900/30','dark:bg-yellow-900/30',
    'dark:bg-indigo-900/30','dark:bg-cyan-900/30','dark:bg-emerald-900/30',
    'dark:bg-red-900/30',
    'from-blue-500','from-purple-500','from-green-500','from-orange-500',
    'from-pink-500','from-yellow-500','from-indigo-500','from-cyan-500',
    'from-emerald-500','from-red-500',
    'to-cyan-500','to-pink-500','to-emerald-500','to-red-500','to-purple-500',
    'to-orange-500','to-blue-500',
    // gh- color classes used in components
    'bg-gh-canvas','bg-gh-surface','bg-gh-overlay','bg-gh-hover',
    'bg-gh-accentMuted','text-gh-text','text-gh-textMuted','text-gh-accent',
    'border-gh-border','border-gh-borderMuted',
  ],
}
