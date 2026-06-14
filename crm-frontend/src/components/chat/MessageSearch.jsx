import { useState } from 'react'
import { motion, AnimatePresence } from 'framer-motion'
import { FiSearch, FiX, FiChevronUp, FiChevronDown } from 'react-icons/fi'
import { format } from 'date-fns'

const MessageSearch = ({ messages, onSelectMessage, onClose }) => {
  const [query, setQuery] = useState('')
  const [currentIndex, setCurrentIndex] = useState(0)

  const filteredMessages = messages.filter((msg) =>
    msg.content?.toLowerCase().includes(query.toLowerCase())
  )

  const handleNext = () => {
    if (currentIndex < filteredMessages.length - 1) {
      const newIndex = currentIndex + 1
      setCurrentIndex(newIndex)
      onSelectMessage(filteredMessages[newIndex])
    }
  }

  const handlePrevious = () => {
    if (currentIndex > 0) {
      const newIndex = currentIndex - 1
      setCurrentIndex(newIndex)
      onSelectMessage(filteredMessages[newIndex])
    }
  }

  const handleSelectResult = (message, index) => {
    setCurrentIndex(index)
    onSelectMessage(message)
  }

  const highlightText = (text, highlight) => {
    if (!highlight.trim()) return text
    
    const escapedHighlight = highlight.replace(/[.*+?^${}()|[\]\\]/g, '\\$&')
    const regex = new RegExp(`(${escapedHighlight})`, 'gi')
    const parts = text.split(regex)
    
    return parts.map((part, index) =>
      regex.test(part) ? (
        <mark key={index} className="bg-yellow-200 dark:bg-yellow-600 text-gray-900 dark:text-white">
          {part}
        </mark>
      ) : (
        part
      )
    )
  }

  return (
    <AnimatePresence>
      <motion.div
        initial={{ opacity: 0, x: '100%' }}
        animate={{ opacity: 1, x: 0 }}
        exit={{ opacity: 0, x: '100%' }}
        transition={{ type: 'spring', damping: 25, stiffness: 200 }}
        className="fixed right-0 top-0 h-full w-96 bg-white dark:bg-gray-800 border-l border-gray-200 dark:border-gray-700 shadow-2xl z-50 flex flex-col"
      >
        {/* Header */}
        <div className="p-4 border-b border-gray-200 dark:border-gray-700">
          <div className="flex items-center justify-between mb-4">
            <h3 className="text-lg font-bold text-gray-900 dark:text-white">
              Search Messages
            </h3>
            <motion.button
              whileHover={{ scale: 1.1 }}
              whileTap={{ scale: 0.9 }}
              onClick={onClose}
              className="p-2 rounded-lg hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors"
            >
              <FiX size={20} className="text-gray-600 dark:text-gray-400" />
            </motion.button>
          </div>

          {/* Search Input */}
          <div className="relative">
            <FiSearch className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400" size={18} />
            <input
              type="text"
              value={query}
              onChange={(e) => {
                setQuery(e.target.value)
                setCurrentIndex(0)
              }}
              placeholder="Search in conversation..."
              autoFocus
              className="w-full pl-10 pr-4 py-2 bg-gray-100 dark:bg-gray-700 border-0 rounded-lg text-sm focus:ring-2 focus:ring-blue-500 transition-all"
            />
          </div>

          {/* Results Counter and Navigation */}
          {query && (
            <div className="flex items-center justify-between mt-3">
              <span className="text-sm text-gray-600 dark:text-gray-400">
                {filteredMessages.length > 0
                  ? `${currentIndex + 1} of ${filteredMessages.length}`
                  : 'No results'}
              </span>
              <div className="flex items-center space-x-1">
                <motion.button
                  whileHover={{ scale: 1.1 }}
                  whileTap={{ scale: 0.9 }}
                  onClick={handlePrevious}
                  disabled={currentIndex === 0 || filteredMessages.length === 0}
                  className="p-1.5 rounded hover:bg-gray-100 dark:hover:bg-gray-700 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
                >
                  <FiChevronUp size={16} className="text-gray-600 dark:text-gray-400" />
                </motion.button>
                <motion.button
                  whileHover={{ scale: 1.1 }}
                  whileTap={{ scale: 0.9 }}
                  onClick={handleNext}
                  disabled={currentIndex === filteredMessages.length - 1 || filteredMessages.length === 0}
                  className="p-1.5 rounded hover:bg-gray-100 dark:hover:bg-gray-700 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
                >
                  <FiChevronDown size={16} className="text-gray-600 dark:text-gray-400" />
                </motion.button>
              </div>
            </div>
          )}
        </div>

        {/* Results List */}
        <div className="flex-1 overflow-y-auto">
          {query ? (
            filteredMessages.length > 0 ? (
              <div className="divide-y divide-gray-200 dark:divide-gray-700">
                {filteredMessages.map((message, index) => (
                  <motion.div
                    key={message.id}
                    whileHover={{ backgroundColor: 'rgba(59, 130, 246, 0.05)' }}
                    onClick={() => handleSelectResult(message, index)}
                    className={`p-4 cursor-pointer transition-colors ${
                      index === currentIndex ? 'bg-blue-50 dark:bg-blue-900/20' : ''
                    }`}
                  >
                    <div className="flex items-start space-x-3">
                      <div className="w-8 h-8 rounded-full bg-gradient-to-br from-blue-600 to-purple-600 flex items-center justify-center text-white text-sm font-semibold flex-shrink-0">
                        {message.senderName?.charAt(0).toUpperCase()}
                      </div>
                      <div className="flex-1 min-w-0">
                        <div className="flex items-center justify-between mb-1">
                          <span className="text-sm font-semibold text-gray-900 dark:text-white">
                            {message.senderName}
                          </span>
                          <span className="text-xs text-gray-500 dark:text-gray-400">
                            {format(new Date(message.createdAt), 'MMM d, h:mm a')}
                          </span>
                        </div>
                        <p className="text-sm text-gray-700 dark:text-gray-300 line-clamp-2">
                          {highlightText(message.content, query)}
                        </p>
                      </div>
                    </div>
                  </motion.div>
                ))}
              </div>
            ) : (
              <div className="flex flex-col items-center justify-center h-full p-8 text-center">
                <FiSearch size={48} className="text-gray-400 mb-4" />
                <h3 className="text-lg font-semibold text-gray-900 dark:text-white mb-2">
                  No messages found
                </h3>
                <p className="text-sm text-gray-600 dark:text-gray-400">
                  Try a different search term
                </p>
              </div>
            )
          ) : (
            <div className="flex flex-col items-center justify-center h-full p-8 text-center">
              <FiSearch size={48} className="text-gray-400 mb-4" />
              <h3 className="text-lg font-semibold text-gray-900 dark:text-white mb-2">
                Search Messages
              </h3>
              <p className="text-sm text-gray-600 dark:text-gray-400">
                Enter a search term to find messages in this conversation
              </p>
            </div>
          )}
        </div>
      </motion.div>
    </AnimatePresence>
  )
}

export default MessageSearch
