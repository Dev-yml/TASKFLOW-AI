import { createSlice } from '@reduxjs/toolkit'

const initialState = {
  notifications: [],
  unreadCount: 0,
  isOpen: false,
}

const notificationSlice = createSlice({
  name: 'notifications',
  initialState,
  reducers: {
    addNotification: (state, action) => {
      state.notifications.unshift(action.payload)
      if (!action.payload.isRead) {
        state.unreadCount += 1
      }
    },
    setNotifications: (state, action) => {
      const list = Array.isArray(action.payload) ? action.payload : []
      state.notifications = list
      state.unreadCount = list.filter(n => !n.isRead).length
    },
    markAsRead: (state, action) => {
      const notification = state.notifications.find(n => n.id === action.payload)
      if (notification && !notification.isRead) {
        notification.isRead = true
        state.unreadCount -= 1
      }
    },
    markAllAsRead: (state) => {
      state.notifications.forEach(n => n.isRead = true)
      state.unreadCount = 0
    },
    togglePanel: (state) => {
      state.isOpen = !state.isOpen
    },
    closePanel: (state) => {
      state.isOpen = false
    },
    clearNotifications: (state) => {
      state.notifications = []
      state.unreadCount = 0
    },
  },
})

export const {
  addNotification,
  setNotifications,
  markAsRead,
  markAllAsRead,
  togglePanel,
  closePanel,
  clearNotifications,
} = notificationSlice.actions

export default notificationSlice.reducer
