import { useState, useEffect, useRef } from 'react'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { chatService } from '../../services/chatService'
import { websocketService } from '../../services/websocketService'
import MessageList from './MessageList'
import MessageInput from './MessageInput'
import TypingIndicator from './TypingIndicator'
import Spinner from '../common/Spinner'
import toast from 'react-hot-toast'

const ChatWindow = ({ room, roomDetails, isLoading }) => {
  const queryClient = useQueryClient()
  const [typingUsers, setTypingUsers] = useState([])
  const messagesEndRef = useRef(null)

  // Fetch messages
  const { data: messagesData, isLoading: messagesLoading } = useQuery({
    queryKey: ['chatMessages', room.id],
    queryFn: () => chatService.getMessages(room.id, { page: 0, size: 100 }),
    enabled: !!room.id,
  })

  const messages = messagesData?.content || []

  // Mark messages as read
  const markAsReadMutation = useMutation({
    mutationFn: () => chatService.markAsRead(room.id),
    onSuccess: () => {
      queryClient.invalidateQueries(['chatRooms'])
      queryClient.invalidateQueries(['chatRoom', room.id])
    },
  })

  // Send message
  const sendMessageMutation = useMutation({
    mutationFn: (content) => chatService.sendMessage({ chatRoomId: room.id, content }),
    onError: () => {
      toast.error('Failed to send message')
    }
  })

  const handleSendMessage = (content) => {
    sendMessageMutation.mutate(content)
  }

  const handleTyping = (isTyping) => {
    websocketService.sendTypingIndicator(room.id, isTyping)
  }

  // WebSocket: Subscribe to new messages
  useEffect(() => {
    if (!room.id || !websocketService.connected) return

    const handleNewMessage = (message) => {
      queryClient.setQueryData(['chatMessages', room.id], (old) => {
        if (!old) return { content: [message] }
        // Avoid duplicates
        if (old.content.some((m) => m.id === message.id)) return old
        return {
          ...old,
          content: [...old.content, message],
        }
      })

      // Mark as read
      markAsReadMutation.mutate()

      // Scroll to bottom
      setTimeout(() => {
        messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' })
      }, 100)
    }

    const subscription = websocketService.subscribeToChat(room.id, handleNewMessage)

    return () => {
      websocketService.unsubscribeFromChat(room.id)
    }
  }, [room.id, queryClient])

  // WebSocket: Subscribe to typing indicators
  useEffect(() => {
    if (!room.id || !websocketService.connected) return

    const handleTyping = (data) => {
      if (data.isTyping) {
        setTypingUsers((prev) => {
          if (prev.some((u) => u.userId === data.userId)) return prev
          return [...prev, data]
        })

        // Remove after 10 seconds
        setTimeout(() => {
          setTypingUsers((prev) => prev.filter((u) => u.userId !== data.userId))
        }, 10000)
      } else {
        setTypingUsers((prev) => prev.filter((u) => u.userId !== data.userId))
      }
    }

    const subscription = websocketService.subscribeToTyping(room.id, handleTyping)

    return () => {
      websocketService.unsubscribeFromTyping(room.id)
    }
  }, [room.id])

  // Mark as read when room changes
  useEffect(() => {
    if (room.id && messages.length > 0) {
      markAsReadMutation.mutate()
    }
  }, [room.id])

  // Scroll to bottom on new messages
  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' })
  }, [messages.length])

  if (isLoading || messagesLoading) {
    return (
      <div className="flex-1 flex items-center justify-center">
        <Spinner size="lg" />
      </div>
    )
  }

  return (
    <div className="flex-1 flex flex-col overflow-hidden">
      {/* Messages */}
      <div className="flex-1 overflow-y-auto p-4 space-y-4">
        <MessageList messages={messages} roomDetails={roomDetails} />
        <div ref={messagesEndRef} />
      </div>

      {/* Typing Indicator */}
      {typingUsers.length > 0 && (
        <div className="px-4 pb-2">
          <TypingIndicator users={typingUsers} />
        </div>
      )}

      {/* Message Input */}
      <MessageInput 
        onSendMessage={handleSendMessage} 
        onTyping={handleTyping} 
        disabled={sendMessageMutation.isPending} 
      />
    </div>
  )
}

export default ChatWindow
