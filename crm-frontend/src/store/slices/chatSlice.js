import { createSlice } from '@reduxjs/toolkit'

const initialState = {
  rooms: [],
  currentRoom: null,
  messages: {},
  typingUsers: {},
  unreadCounts: {},
  isConnected: false,
  isLoading: false,
}

const chatSlice = createSlice({
  name: 'chat',
  initialState,
  reducers: {
    setRooms: (state, action) => {
      state.rooms = action.payload
    },
    addRoom: (state, action) => {
      const exists = state.rooms.find(r => r.id === action.payload.id)
      if (!exists) {
        state.rooms.unshift(action.payload)
      }
    },
    updateRoom: (state, action) => {
      const index = state.rooms.findIndex(r => r.id === action.payload.id)
      if (index !== -1) {
        state.rooms[index] = { ...state.rooms[index], ...action.payload }
      }
    },
    setCurrentRoom: (state, action) => {
      state.currentRoom = action.payload
    },
    setMessages: (state, action) => {
      const { roomId, messages } = action.payload
      state.messages[roomId] = messages
    },
    addMessage: (state, action) => {
      const { roomId, message } = action.payload
      if (!state.messages[roomId]) {
        state.messages[roomId] = []
      }
      if (message.clientId) {
        const optimisticIndex = state.messages[roomId].findIndex(m => m.clientId === message.clientId)
        if (optimisticIndex !== -1) {
          state.messages[roomId][optimisticIndex] = message
          return
        }
      }
      // Avoid duplicates
      const exists = state.messages[roomId].find(m => m.id === message.id)
      if (!exists) {
        state.messages[roomId].push(message)
      }
      // Update last message in room
      const roomIndex = state.rooms.findIndex(r => r.id === roomId)
      if (roomIndex !== -1) {
        state.rooms[roomIndex].lastMessage = message
      }
    },
    prependMessages: (state, action) => {
      const { roomId, messages } = action.payload
      const existing = state.messages[roomId] || []
      const existingIds = new Set(existing.map((message) => message.id))
      const olderMessages = messages.filter((message) => !existingIds.has(message.id))
      state.messages[roomId] = [...olderMessages, ...existing]
    },
    removeMessage: (state, action) => {
      const { roomId, messageId } = action.payload
      if (state.messages[roomId]) {
        state.messages[roomId] = state.messages[roomId].filter((message) => message.id !== messageId)
      }
    },
    updateMessage: (state, action) => {
      const { roomId, messageId, updates } = action.payload
      if (state.messages[roomId]) {
        const index = state.messages[roomId].findIndex(m => m.id === messageId)
        if (index !== -1) {
          state.messages[roomId][index] = { ...state.messages[roomId][index], ...updates }
        }
      }
    },
    deleteMessage: (state, action) => {
      const { roomId, messageId } = action.payload
      if (state.messages[roomId]) {
        const index = state.messages[roomId].findIndex(m => m.id === messageId)
        if (index !== -1) {
          state.messages[roomId][index].isDeleted = true
          state.messages[roomId][index].content = 'This message was deleted'
        }
      }
    },
    setTypingUsers: (state, action) => {
      const { roomId, users } = action.payload
      state.typingUsers[roomId] = users
    },
    addTypingUser: (state, action) => {
      const { roomId, user } = action.payload
      if (!state.typingUsers[roomId]) {
        state.typingUsers[roomId] = []
      }
      const exists = state.typingUsers[roomId].find(u => u.userId === user.userId)
      if (!exists) {
        state.typingUsers[roomId].push(user)
      }
    },
    removeTypingUser: (state, action) => {
      const { roomId, userId } = action.payload
      if (state.typingUsers[roomId]) {
        state.typingUsers[roomId] = state.typingUsers[roomId].filter(u => u.userId !== userId)
      }
    },
    setUnreadCount: (state, action) => {
      const { roomId, count } = action.payload
      state.unreadCounts[roomId] = count
    },
    incrementUnreadCount: (state, action) => {
      const { roomId } = action.payload
      state.unreadCounts[roomId] = (state.unreadCounts[roomId] || 0) + 1
    },
    clearUnreadCount: (state, action) => {
      const { roomId } = action.payload
      state.unreadCounts[roomId] = 0
    },
    setConnected: (state, action) => {
      state.isConnected = action.payload
    },
    setLoading: (state, action) => {
      state.isLoading = action.payload
    },
    clearChat: (state) => {
      state.rooms = []
      state.currentRoom = null
      state.messages = {}
      state.typingUsers = {}
      state.unreadCounts = {}
      state.isConnected = false
    },
  },
})

export const {
  setRooms,
  addRoom,
  updateRoom,
  setCurrentRoom,
  setMessages,
  addMessage,
  prependMessages,
  removeMessage,
  updateMessage,
  deleteMessage,
  setTypingUsers,
  addTypingUser,
  removeTypingUser,
  setUnreadCount,
  incrementUnreadCount,
  clearUnreadCount,
  setConnected,
  setLoading,
  clearChat,
} = chatSlice.actions

export default chatSlice.reducer
