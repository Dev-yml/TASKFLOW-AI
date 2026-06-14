import { useState, useRef, useEffect } from 'react'
import { motion, AnimatePresence } from 'framer-motion'
import { FiMoreVertical, FiUsers, FiPhone, FiVideo, FiInfo, FiSearch, FiCpu, FiTrash2, FiLogOut, FiSlash } from 'react-icons/fi'
import { useSelector } from 'react-redux'
import { useNavigate } from 'react-router-dom'
import { useMutation, useQueryClient } from '@tanstack/react-query'
import { chatService } from '../../services/chatService'
import toast from 'react-hot-toast'

const ChatHeader = ({ room, onShowInfo, onShowSearch, onShowAI }) => {
  const { user } = useSelector((state) => state.auth)
  const { onlineUsers } = useSelector((state) => state.presence)
  const [showMenu, setShowMenu] = useState(false)
  const [showDeleteConfirm, setShowDeleteConfirm] = useState(false)
  const menuRef = useRef(null)
  const navigate = useNavigate()
  const queryClient = useQueryClient()

  // Close menu when clicking outside
  useEffect(() => {
    const handler = (e) => {
      if (menuRef.current && !menuRef.current.contains(e.target)) setShowMenu(false)
    }
    document.addEventListener('mousedown', handler)
    return () => document.removeEventListener('mousedown', handler)
  }, [])

  const isCreator = room?.createdById === user?.id

  const deleteMutation = useMutation({
    mutationFn: () => chatService.deleteRoom(room.id),
    onSuccess: () => {
      toast.success('Chat deleted')
      queryClient.invalidateQueries({ queryKey: ['chat-rooms'] })
      navigate('/chat')
    },
    onError: (err) => toast.error(err?.message || 'Failed to delete chat'),
  })

  const leaveMutation = useMutation({
    mutationFn: () => chatService.removeParticipant(room.id, user.id),
    onSuccess: () => {
      toast.success('Left conversation')
      queryClient.invalidateQueries({ queryKey: ['chat-rooms'] })
      navigate('/chat')
    },
    onError: (err) => toast.error(err?.message || 'Failed to leave'),
  })

  if (!room) {
    return (
      <div className="h-16 border-b border-gray-200 dark:border-gray-700 bg-white dark:bg-gray-800 flex items-center justify-center">
        <p className="text-gray-500 dark:text-gray-400">Select a conversation to start chatting</p>
      </div>
    )
  }

  const getOtherUser = () => {
    if (room.type === 'PRIVATE') {
      return room.participants?.find(p => p.userId !== user?.id)
    }
    return null
  }

  const otherUser = getOtherUser()
  const isOnline = room.type === 'PRIVATE' && otherUser && Boolean(onlineUsers[otherUser.userId])

  const getRoomName = () => {
    if (room.type === 'PRIVATE') {
      return otherUser?.userName || 'Unknown User'
    }
    return room.name || 'Unnamed Room'
  }

  const getRoomSubtitle = () => {
    if (room.type === 'PRIVATE') {
      return isOnline ? 'Online' : 'Offline'
    }
    const participantCount = room.participants?.length || 0
    return `${participantCount} ${participantCount === 1 ? 'member' : 'members'}`
  }

  return (
    <div className="h-16 border-b border-gray-200 dark:border-gray-700 bg-white dark:bg-gray-800 px-6 flex items-center justify-between">
      {/* Left: Room Info */}
      <div className="flex items-center space-x-3">
        {/* Avatar */}
        <div className="relative">
          <div className={`w-10 h-10 rounded-full flex items-center justify-center text-white font-semibold ${
            room.type === 'PRIVATE' ? 'bg-gradient-to-br from-blue-600 to-purple-600' : 'bg-gradient-to-br from-green-600 to-teal-600'
          }`}>
            {room.type === 'GROUP' ? <FiUsers size={20} /> : getRoomName().charAt(0).toUpperCase()}
          </div>
          {isOnline && (
            <div className="absolute bottom-0 right-0 w-3 h-3 bg-green-500 border-2 border-white dark:border-gray-800 rounded-full" />
          )}
        </div>

        {/* Name and Status */}
        <div>
          <h2 className="text-lg font-semibold text-gray-900 dark:text-white">
            {getRoomName()}
          </h2>
          <p className={`text-sm ${isOnline ? 'text-green-600 dark:text-green-400' : 'text-gray-500 dark:text-gray-400'}`}>
            {getRoomSubtitle()}
          </p>
        </div>
      </div>

      {/* Right: Actions */}
      <div className="flex items-center space-x-2">
        <motion.button
          whileHover={{ scale: 1.1 }}
          whileTap={{ scale: 0.9 }}
          onClick={onShowAI}
          className="p-2 rounded-lg bg-cyan-50 text-cyan-700 hover:bg-cyan-100 transition-colors dark:bg-cyan-950/40 dark:text-cyan-300"
          title="AI Chat Intelligence"
        >
          <FiCpu size={20} />
        </motion.button>
        <motion.button
          whileHover={{ scale: 1.1 }}
          whileTap={{ scale: 0.9 }}
          onClick={onShowSearch}
          className="p-2 rounded-lg hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors text-gray-600 dark:text-gray-400"
          title="Search Messages"
        >
          <FiSearch size={20} />
        </motion.button>
        <motion.button
          whileHover={{ scale: 1.1 }}
          whileTap={{ scale: 0.9 }}
          className="p-2 rounded-lg hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors text-gray-600 dark:text-gray-400"
          title="Voice Call"
        >
          <FiPhone size={20} />
        </motion.button>
        <motion.button
          whileHover={{ scale: 1.1 }}
          whileTap={{ scale: 0.9 }}
          className="p-2 rounded-lg hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors text-gray-600 dark:text-gray-400"
          title="Video Call"
        >
          <FiVideo size={20} />
        </motion.button>
        <motion.button
          whileHover={{ scale: 1.1 }}
          whileTap={{ scale: 0.9 }}
          onClick={onShowInfo}
          className="p-2 rounded-lg hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors text-gray-600 dark:text-gray-400"
          title="Room Info"
        >
          <FiInfo size={20} />
        </motion.button>

        {/* More actions dropdown */}
        <div className="relative" ref={menuRef}>
          <motion.button
            whileHover={{ scale: 1.1 }}
            whileTap={{ scale: 0.9 }}
            onClick={() => setShowMenu(!showMenu)}
            className="p-2 rounded-lg hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors text-gray-600 dark:text-gray-400"
          >
            <FiMoreVertical size={20} />
          </motion.button>

          <AnimatePresence>
            {showMenu && (
              <motion.div
                initial={{ opacity: 0, scale: 0.95, y: -8 }}
                animate={{ opacity: 1, scale: 1, y: 0 }}
                exit={{ opacity: 0, scale: 0.95, y: -8 }}
                transition={{ duration: 0.12 }}
                className="absolute right-0 mt-2 w-48 bg-white dark:bg-gray-800 rounded-xl shadow-xl border border-gray-200 dark:border-gray-700 py-1 z-50"
              >
                {/* Leave conversation (non-creator or private) */}
                {!isCreator && (
                  <button
                    onClick={() => { setShowMenu(false); leaveMutation.mutate() }}
                    className="w-full flex items-center gap-3 px-4 py-2.5 text-sm text-gray-700 dark:text-gray-300 hover:bg-gray-50 dark:hover:bg-gray-700 transition-colors"
                  >
                    <FiLogOut size={15} className="text-orange-500" />
                    Leave conversation
                  </button>
                )}

                {/* Delete chat (creator only) */}
                {isCreator && !showDeleteConfirm && (
                  <button
                    onClick={() => setShowDeleteConfirm(true)}
                    className="w-full flex items-center gap-3 px-4 py-2.5 text-sm text-red-600 dark:text-red-400 hover:bg-red-50 dark:hover:bg-red-900/20 transition-colors"
                  >
                    <FiTrash2 size={15} />
                    Delete chat
                  </button>
                )}

                {/* Inline delete confirm */}
                {isCreator && showDeleteConfirm && (
                  <div className="px-4 py-3 space-y-2">
                    <p className="text-xs text-gray-600 dark:text-gray-400 font-medium">
                      Delete forever? This cannot be undone.
                    </p>
                    <div className="flex gap-2">
                      <button
                        onClick={() => { setShowMenu(false); setShowDeleteConfirm(false); deleteMutation.mutate() }}
                        className="flex-1 py-1.5 text-xs font-semibold bg-red-600 hover:bg-red-700 text-white rounded-lg transition-colors"
                      >
                        Delete
                      </button>
                      <button
                        onClick={() => setShowDeleteConfirm(false)}
                        className="flex-1 py-1.5 text-xs font-semibold border border-gray-200 dark:border-gray-600 text-gray-700 dark:text-gray-300 rounded-lg hover:bg-gray-50 dark:hover:bg-gray-700 transition-colors"
                      >
                        Cancel
                      </button>
                    </div>
                  </div>
                )}
              </motion.div>
            )}
          </AnimatePresence>
        </div>
      </div>
    </div>
  )
}

export default ChatHeader
