import api from './api'

export const authService = {
  login: async (credentials) => {
    const response = await api.post('/auth/login', credentials)
    return response.data
  },

  register: async (userData) => {
    const response = await api.post('/auth/register', userData)
    return response.data
  },

  logout: async () => true,

  getCurrentUser: async () => {
    const response = await api.get('/users/me')
    return response.data
  },

  updateProfile: async (userData) => {
    const response = await api.put('/users/profile', userData)
    return response.data
  },

  updateAvatar: async (avatarUrl) => {
    const response = await api.post('/users/profile/avatar', { avatarUrl })
    return response.data
  },

  removeAvatar: async () => {
    const response = await api.delete('/users/profile/avatar')
    return response.data
  },

  changePassword: async (passwordData) => {
    const response = await api.put('/users/change-password', passwordData)
    return response.data
  },

  /**
   * Convert an image file to a Base64 data URL.
   * Stored directly as profileImageUrl — no external service needed.
   * Max 2 MB to keep DB rows reasonable.
   */
  uploadImageToCloudinary: async (file) => {
    if (file.size > 2 * 1024 * 1024) {
      throw new Error('Max file size is 2 MB')
    }

    return new Promise((resolve, reject) => {
      const reader = new FileReader()
      reader.onload = () => resolve(reader.result)   // data:image/jpeg;base64,...
      reader.onerror = () => reject(new Error('Failed to read file'))
      reader.readAsDataURL(file)
    })
  },
}
