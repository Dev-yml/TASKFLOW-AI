import { createSlice } from '@reduxjs/toolkit'

const initialState = {
  activeLeadId: null,
  selectedWorkspaceId: null,
  showAnalytics: true,
  sortBy: 'createdAt',
  sortDir: 'DESC',
}

const pipelineSlice = createSlice({
  name: 'pipeline',
  initialState,
  reducers: {
    setActiveLeadId: (state, action) => {
      state.activeLeadId = action.payload
    },
    setSelectedWorkspaceId: (state, action) => {
      state.selectedWorkspaceId = action.payload
    },
    setShowAnalytics: (state, action) => {
      state.showAnalytics = action.payload
    },
    setPipelineSort: (state, action) => {
      state.sortBy = action.payload.sortBy
      state.sortDir = action.payload.sortDir
    },
  },
})

export const {
  setActiveLeadId,
  setSelectedWorkspaceId,
  setShowAnalytics,
  setPipelineSort,
} = pipelineSlice.actions

export default pipelineSlice.reducer
