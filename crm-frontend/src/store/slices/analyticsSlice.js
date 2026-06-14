import { createSlice } from '@reduxjs/toolkit'
import { subDays, format } from 'date-fns'

const initialState = {
  filters: {
    startDate: format(subDays(new Date(), 30), 'yyyy-MM-dd'),
    endDate: format(new Date(), 'yyyy-MM-dd'),
    workspaceId: null,
    projectId: null,
    userId: null,
    crmStage: '',
  },
  compactCharts: false,
}

const analyticsSlice = createSlice({
  name: 'analytics',
  initialState,
  reducers: {
    setAnalyticsFilters: (state, action) => {
      state.filters = { ...state.filters, ...action.payload }
    },
    resetAnalyticsFilters: (state) => {
      state.filters = initialState.filters
    },
    setCompactCharts: (state, action) => {
      state.compactCharts = action.payload
    },
  },
})

export const {
  setAnalyticsFilters,
  resetAnalyticsFilters,
  setCompactCharts,
} = analyticsSlice.actions

export default analyticsSlice.reducer
