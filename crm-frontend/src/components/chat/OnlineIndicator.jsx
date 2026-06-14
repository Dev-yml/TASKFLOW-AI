import clsx from 'clsx'

const OnlineIndicator = ({ isOnline, size = 'sm' }) => {
  const sizeClasses = {
    sm: 'w-2 h-2',
    md: 'w-3 h-3',
    lg: 'w-4 h-4',
  }

  return (
    <span
      className={clsx(
        'rounded-full border-2 border-white dark:border-gray-800',
        sizeClasses[size],
        isOnline ? 'bg-green-500' : 'bg-gray-400'
      )}
      title={isOnline ? 'Online' : 'Offline'}
    />
  )
}

export default OnlineIndicator
