import { useQuery } from '@tanstack/react-query'
import { taskService } from '../../services/taskService'
import { format } from 'date-fns'
import {
  FiCheckCircle,
  FiEdit,
  FiUser,
  FiCalendar,
  FiAlertCircle,
} from 'react-icons/fi'
import Spinner from '../common/Spinner'

const ACTIVITY_ICONS = {
  TASK_CREATED: FiCheckCircle,
  STATUS_CHANGED: FiEdit,
  TASK_ASSIGNED: FiUser,
  DUE_DATE_CHANGED: FiCalendar,
  PRIORITY_CHANGED: FiAlertCircle,
  DESCRIPTION_UPDATED: FiEdit,
  COMMENT_ADDED: FiCheckCircle,
}

const TaskActivity = ({ taskId }) => {
  const { data: rawActivities, isLoading } = useQuery({
    queryKey: ['task-activities', taskId],
    queryFn: () => taskService.getActivities(taskId),
  })

  const activities = Array.isArray(rawActivities)
    ? rawActivities
    : rawActivities?.content ?? rawActivities?.data?.content ?? []

  if (isLoading) {
    return (
      <div className="flex justify-center py-8">
        <Spinner />
      </div>
    )
  }

  if (activities.length === 0) {
    return (
      <p className="text-center text-gray-500 dark:text-gray-400 py-8">
        No activity yet
      </p>
    )
  }

  return (
    <div className="space-y-4">
      {activities.map((activity, index) => {
        const Icon = ACTIVITY_ICONS[activity.activityType] || FiCheckCircle

        return (
          <div key={activity.id} className="flex gap-3">
            {/* Timeline */}
            <div className="flex flex-col items-center">
              <div className="w-8 h-8 rounded-full bg-primary-100 dark:bg-primary-900/20 flex items-center justify-center">
                <Icon className="text-primary-600 dark:text-primary-400" />
              </div>
              {index < activities.length - 1 && (
                <div className="w-0.5 flex-1 bg-gray-200 dark:bg-gray-700 mt-2" />
              )}
            </div>

            {/* Content */}
            <div className="flex-1 pb-6">
              <div className="flex items-center gap-2 mb-1">
                <span className="font-medium text-sm">
                  {activity.createdBy?.fullName}
                </span>
                <span className="text-xs text-gray-500 dark:text-gray-400">
                  {format(new Date(activity.createdAt), 'MMM dd, yyyy HH:mm')}
                </span>
              </div>
              <p className="text-sm text-gray-700 dark:text-gray-300">
                {activity.description}
              </p>
              {activity.oldValue && activity.newValue && (
                <div className="mt-2 text-xs text-gray-600 dark:text-gray-400">
                  <span className="line-through">{activity.oldValue}</span>
                  {' → '}
                  <span className="font-medium">{activity.newValue}</span>
                </div>
              )}
            </div>
          </div>
        )
      })}
    </div>
  )
}

export default TaskActivity
