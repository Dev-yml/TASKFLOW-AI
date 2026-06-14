import { createSlice } from '@reduxjs/toolkit'

const initialState = {
  isOpen: false,
  context: {
    page: null,
    entityId: null,
    title: null,
  },
  messages: [],
}

const copilotSlice = createSlice({
  name: 'copilot',
  initialState,
  reducers: {
    openCopilot: (state, action) => {
      state.isOpen = true
      if (action.payload) state.context = { ...state.context, ...action.payload }
    },
    closeCopilot: (state) => {
      state.isOpen = false
    },
    toggleCopilot: (state) => {
      state.isOpen = !state.isOpen
    },
    setCopilotContext: (state, action) => {
      state.context = { ...state.context, ...action.payload }
    },
    addCopilotMessage: (state, action) => {
      state.messages.push(action.payload)
    },
    clearCopilotMessages: (state) => {
      state.messages = []
    },
  },
})

export const {
  openCopilot,
  closeCopilot,
  toggleCopilot,
  setCopilotContext,
  addCopilotMessage,
  clearCopilotMessages,
} = copilotSlice.actions

export default copilotSlice.reducer
