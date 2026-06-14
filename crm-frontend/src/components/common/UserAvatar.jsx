/**
 * UserAvatar — shared across the entire app.
 * Shows profile photo if available, otherwise coloured initials.
 *
 * Props:
 *   user     — { fullName, displayName, profileImageUrl }
 *   size     — 'xs' | 'sm' | 'md' | 'lg' | 'xl'  (default 'md')
 *   className — extra Tailwind classes
 */

const SIZE = {
  xs: { ring: 'w-6 h-6 text-[9px]',  img: 'w-6 h-6'  },
  sm: { ring: 'w-8 h-8 text-[11px]', img: 'w-8 h-8'  },
  md: { ring: 'w-9 h-9 text-[13px]', img: 'w-9 h-9'  },
  lg: { ring: 'w-12 h-12 text-[16px]',img: 'w-12 h-12'},
  xl: { ring: 'w-20 h-20 text-[28px]',img: 'w-20 h-20'},
}

const PALETTE = [
  'bg-violet-500', 'bg-sky-500',   'bg-emerald-500',
  'bg-rose-500',   'bg-amber-500', 'bg-indigo-500',
  'bg-pink-500',   'bg-teal-500',
]

function colorFor(name = '') {
  const sum = [...name].reduce((a, c) => a + c.charCodeAt(0), 0)
  return PALETTE[sum % PALETTE.length]
}

function initials(name = '') {
  return name
    .split(' ')
    .filter(Boolean)
    .slice(0, 2)
    .map((n) => n[0].toUpperCase())
    .join('') || '?'
}

const UserAvatar = ({ user, size = 'md', className = '' }) => {
  const displayName = user?.displayName || user?.fullName || ''
  const imgUrl = user?.profileImageUrl
  const s = SIZE[size] ?? SIZE.md

  if (imgUrl) {
    return (
      <img
        src={imgUrl}
        alt={displayName || 'avatar'}
        className={`${s.img} rounded-full object-cover flex-shrink-0 ${className}`}
      />
    )
  }

  return (
    <div
      className={`${s.ring} ${colorFor(displayName)} rounded-full flex items-center justify-center
        font-bold text-white flex-shrink-0 select-none ${className}`}
      aria-label={displayName}
    >
      {initials(displayName)}
    </div>
  )
}

export default UserAvatar
