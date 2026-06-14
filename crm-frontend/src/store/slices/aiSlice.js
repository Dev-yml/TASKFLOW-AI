import { createSlice } from '@reduxjs/toolkit'

const initialState = {
  activeSurface: null,
  lastRunAt: null,
  usage: {
    requests: 0,
    failures: 0,
  },
}

const aiSlice = createSlice({
  name: 'ai',
  initialState,
  reducers: {
    setActiveAISurface: (state, action) => {
      state.activeSurface = action.payload
    },
    recordAIRequest: (state) => {
      state.usage.requests += 1
      state.lastRunAt = new Date().toISOString()
    },
    recordAIFailure: (state) => {
      state.usage.failures += 1
      state.lastRunAt = new Date().toISOString()
    },
  },
})

export const {
  setActiveAISurface,
  recordAIRequest,
  recordAIFailure,
} = aiSlice.actions

export default aiSlice.reducer
