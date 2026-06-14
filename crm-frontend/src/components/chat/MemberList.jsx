import OnlineIndicator from './OnlineIndicator'
import { FiMoreVertical } from 'react-icons/fi'

const MemberList = ({ participants }) => {
  if (!participants || participants.length === 0) {
    return (
      <div className="p-4 text-center text-gray-500">
        No members
      </div>
    )
  }

  // Sort: online first, then by name
  const sortedParticipants = [...participants].sort((a, b) => {
    if (a.isOnline !== b.isOnline) {
      return a.isOnline ? -1 : 1
    }
    return a.userName.localeCompare(b.userName)
  })

  return (
    <div className="divide-y divide-gray-200 dark:divide-gray-700">
      {sortedParticipants.map((participant) => (
        <div
          key={participant.id}
          className="p-3 hover:bg-gray-50 dark:hover:bg-gray-700/50 transition-colors"
        >
          <div className="flex items-center gap-3">
            {/* Avatar with online indicator */}
            <div className="relative">
              <div className="w-10 h-10 rounded-full bg-primary-500 flex items-center justify-center text-white font-medium">
                {participant.userName?.charAt(0).toUpperCase()}
              </div>
              <div className="absolute bottom-0 right-0">
                <OnlineIndicator isOnline={participant.isOnline} size="md" />
              </div>
            </div>

            {/* User Info */}
            <div className="flex-1 min-w-0">
              <p className="font-medium truncate">{participant.userName}</p>
              <p className="text-sm text-gray-500 truncate">
                {participant.userEmail}
              </p>
            </div>

            {/* Actions */}
            <button className="p-1 hover:bg-gray-200 dark:hover:bg-gray-600 rounded">
              <FiMoreVertical className="text-gray-500" />
            </button>
          </div>
        </div>
      ))}
    </div>
  )
}

export default MemberList
