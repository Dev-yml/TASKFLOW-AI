import { FiMessageSquare } from 'react-icons/fi'

const EmptyChat = () => {
  return (
    <div className="flex-1 flex items-center justify-center bg-gray-50 dark:bg-gray-900">
      <div className="text-center">
        <div className="inline-flex items-center justify-center w-20 h-20 bg-gray-200 dark:bg-gray-700 rounded-full mb-4">
          <FiMessageSquare className="text-4xl text-gray-400" />
        </div>
        <h3 className="text-xl font-semibold mb-2">No conversation selected</h3>
        <p className="text-gray-500">
          Choose a conversation from the sidebar to start messaging
        </p>
      </div>
    </div>
  )
}

export default EmptyChat
