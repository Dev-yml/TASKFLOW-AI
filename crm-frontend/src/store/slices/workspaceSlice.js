import { createSlice } from '@reduxjs/toolkit'

const initialState = {
  // Do NOT restore currentWorkspace from localStorage on init.
  // The app always fetches workspaces fresh from the API.
  // Restoring stale IDs caused 403 errors when switching users.
  currentWorkspace: null,
  currentProject: null,
}

const workspaceSlice = createSlice({
  name: 'workspace',
  initialState,
  reducers: {
    setCurrentWorkspace: (state, action) => {
      state.currentWorkspace = action.payload
      localStorage.setItem('currentWorkspace', JSON.stringify(action.payload))
    },
    setCurrentProject: (state, action) => {
      state.currentProject = action.payload
      localStorage.setItem('currentProject', JSON.stringify(action.payload))
    },
    clearWorkspace: (state) => {
      state.currentWorkspace = null
      state.currentProject = null
      localStorage.removeItem('currentWorkspace')
      localStorage.removeItem('currentProject')
    },
  },
})

export const {
  setCurrentWorkspace,
  setCurrentProject,
  clearWorkspace,
} = workspaceSlice.actions

export default workspaceSlice.reducer
