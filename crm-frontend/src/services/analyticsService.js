import api from './api'

const unwrap = (response) => response?.data ?? response

export const analyticsService = {
  getDashboard: async () => {
    const response = await api.get('/dashboard/overview')
    return unwrap(response)
  },

  getTaskAnalytics: async (params) => {
    const response = await api.get('/analytics/tasks', { params })
    return unwrap(response)
  },

  getTeamPerformance: async (params) => {
    const response = await api.get('/analytics/team', { params })
    return unwrap(response)
  },

  getActivityAnalytics: async (params) => {
    const response = await api.get('/analytics/activity', { params })
    return unwrap(response)
  },

  getDailyReport: async (date) => {
    const response = await api.get('/reports/daily', { params: { date } })
    return unwrap(response)
  },

  getWeeklyReport: async (startDate) => {
    const response = await api.get('/reports/weekly', { params: { startDate } })
    return unwrap(response)
  },

  getMonthlyReport: async (year, month) => {
    const response = await api.get('/reports/monthly', { params: { year, month } })
    return unwrap(response)
  },
}
