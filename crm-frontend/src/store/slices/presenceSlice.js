import { createSlice } from '@reduxjs/toolkit'

const initialState = {
  // { [userId]: { online: true, userName: 'Alice', lastSeen: '2026-06-12T...' } }
  onlineUsers: {},
  lastUpdatedAt: null,
}

const presenceSlice = createSlice({
  name: 'presence',
  initialState,
  reducers: {
    // Replaces the whole map — used when we receive a presence snapshot
    setOnlineUsers: (state, action) => {
      const userIds = action.payload // number[]
      const next = {}
      userIds.forEach((id) => {
        // Preserve existing userName/lastSeen if we already know this user
        next[id] = state.onlineUsers[id] ?? { online: true }
        next[id].online = true
      })
      state.onlineUsers = next
      state.lastUpdatedAt = new Date().toISOString()
    },

    // Individual user came online — payload: { userId, userName?, lastSeen? }
    setUserOnline: (state, action) => {
      const { userId, userName, lastSeen } = action.payload
      state.onlineUsers[userId] = {
        ...(state.onlineUsers[userId] ?? {}),
        online: true,
        userName,
        lastSeen,
      }
      state.lastUpdatedAt = new Date().toISOString()
    },

    // Individual user went offline — payload: { userId, lastSeen? }
    setUserOffline: (state, action) => {
      const { userId, lastSeen } = action.payload
      if (state.onlineUsers[userId]) {
        state.onlineUsers[userId] = {
          ...state.onlineUsers[userId],
          online: false,
          lastSeen,
        }
      }
      state.lastUpdatedAt = new Date().toISOString()
    },

    clearPresence: (state) => {
      state.onlineUsers = {}
      state.lastUpdatedAt = null
    },
  },
})

export const {
  setOnlineUsers,
  setUserOnline,
  setUserOffline,
  clearPresence,
} = presenceSlice.actions

// ─── Selectors ──────────────────────────────────────────────────────────────

/** Returns true if the given userId is currently online */
export const selectIsUserOnline = (state, userId) =>
  Boolean(state.presence.onlineUsers[userId]?.online)

/** Returns the last-seen string for a user (ISO timestamp or null) */
export const selectLastSeen = (state, userId) =>
  state.presence.onlineUsers[userId]?.lastSeen ?? null

/** Returns array of all online user IDs */
export const selectOnlineUserIds = (state) =>
  Object.entries(state.presence.onlineUsers)
    .filter(([, v]) => v.online)
    .map(([id]) => Number(id))

export default presenceSlice.reducer
