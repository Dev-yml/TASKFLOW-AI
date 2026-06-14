import api from './api'

const unwrap = (response) => response?.data ?? response

export const userService = {
  // Get current user profile
  getProfile: async () => {
    const response = await api.get('/users/me')
    return response.data
  },

  // Update user profile
  updateProfile: async (userData) => {
    const response = await api.put('/users/me', userData)
    return response.data
  },

  // Search users
  searchUsers: async (query = '') => {
    const response = await api.get('/users/search', {
      params: { query, page: 0, size: 30 },
    })
    return unwrap(response)
  },

  // Get user by ID
  getUser: async (userId) => {
    const response = await api.get(`/users/${userId}`)
    return response.data
  },

  // Get workspace members
  getWorkspaceMembers: async (workspaceId) => {
    const response = await api.get(`/workspaces/${workspaceId}/members`)
    return response.data
  },
}
