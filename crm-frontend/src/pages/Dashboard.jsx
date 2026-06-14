import { useQuery } from '@tanstack/react-query'
import { analyticsService } from '../services/analyticsService'
import Spinner from '../components/common/Spinner'
import { FiCheckCircle, FiClock, FiAlertCircle, FiTrendingUp } from 'react-icons/fi'

const Dashboard = () => {
  const { data: dashboardData, isLoading } = useQuery({
    queryKey: ['dashboard'],
    queryFn: analyticsService.getDashboard,
  })

  if (isLoading) {
    return (
      <div className="flex items-center justify-center h-full">
        <Spinner size="lg" />
      </div>
    )
  }

  const stats = [
    {
      label: 'Total Tasks',
      value: dashboardData?.taskStatistics?.totalTasks || 0,
      icon: FiCheckCircle,
      color: 'text-blue-600',
      bgColor: 'bg-blue-100 dark:bg-blue-900/20',
    },
    {
      label: 'Completed',
      value: dashboardData?.taskStatistics?.completedTasks || 0,
      icon: FiCheckCircle,
      color: 'text-green-600',
      bgColor: 'bg-green-100 dark:bg-green-900/20',
    },
    {
      label: 'In Progress',
      value: dashboardData?.taskStatistics?.inProgressTasks || 0,
      icon: FiClock,
      color: 'text-yellow-600',
      bgColor: 'bg-yellow-100 dark:bg-yellow-900/20',
    },
    {
      label: 'Overdue',
      value: dashboardData?.taskStatistics?.overdueTasks || 0,
      icon: FiAlertCircle,
      color: 'text-red-600',
      bgColor: 'bg-red-100 dark:bg-red-900/20',
    },
  ]

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold mb-2">Dashboard</h1>
        <p className="text-gray-600 dark:text-gray-400">
          Welcome back! Here's what's happening today.
        </p>
      </div>

      {/* Stats Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        {stats.map((stat, index) => (
          <div key={index} className="card">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-gray-600 dark:text-gray-400 mb-1">
                  {stat.label}
                </p>
                <p className="text-3xl font-bold">{stat.value}</p>
              </div>
              <div className={`p-3 rounded-lg ${stat.bgColor}`}>
                <stat.icon className={`text-2xl ${stat.color}`} />
              </div>
            </div>
          </div>
        ))}
      </div>

      {/* Recent Activity */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div className="card">
          <h2 className="text-lg font-semibold mb-4">Recent Activity</h2>
          <div className="space-y-4">
            <p className="text-gray-500 dark:text-gray-400 text-center py-8">
              No recent activity
            </p>
          </div>
        </div>

        <div className="card">
          <h2 className="text-lg font-semibold mb-4">Productivity Score</h2>
          <div className="flex items-center justify-center py-8">
            <div className="text-center">
              <div className="text-5xl font-bold text-primary-600 mb-2">
                {dashboardData?.userProductivity?.activityScore?.toFixed(0) || 0}
              </div>
              <p className="text-gray-600 dark:text-gray-400">Activity Score</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default Dashboard
