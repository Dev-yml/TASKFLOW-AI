import { useSortable } from '@dnd-kit/sortable'
import { CSS } from '@dnd-kit/utilities'
import { format, isPast, isToday } from 'date-fns'
import clsx from 'clsx'
import {
  FiCalendar,
  FiMessageSquare,
  FiPaperclip,
  FiAlertCircle,
  FiZap,
} from 'react-icons/fi'

const PRIORITY_CONFIG = {
  LOW: { color: 'bg-gray-500', label: 'Low' },
  MEDIUM: { color: 'bg-blue-500', label: 'Medium' },
  HIGH: { color: 'bg-orange-500', label: 'High' },
  URGENT: { color: 'bg-red-500', label: 'Urgent' },
}

const TaskCard = ({ task, onClick, isDragging = false }) => {
  const {
    attributes,
    listeners,
    setNodeRef,
    transform,
    transition,
    isDragging: isSortableDragging,
  } = useSortable({ id: task.id })

  const style = {
    transform: CSS.Transform.toString(transform),
    transition,
  }

  const isOverdue = task.dueDate && isPast(new Date(task.dueDate)) && task.status !== 'DONE'
  const isDueToday = task.dueDate && isToday(new Date(task.dueDate))

  return (
    <div
      ref={setNodeRef}
      style={style}
      {...attributes}
      {...listeners}
      onClick={onClick}
      className={clsx(
        'bg-white dark:bg-gray-800 rounded-lg p-4 shadow-sm border border-gray-200 dark:border-gray-700',
        'hover:shadow-md transition-shadow cursor-pointer',
        (isDragging || isSortableDragging) && 'opacity-50',
        isOverdue && 'border-l-4 border-l-red-500'
      )}
    >
      {/* Priority Badge */}
      <div className="flex items-start justify-between mb-2">
        <span
          className={clsx(
            'text-xs px-2 py-1 rounded-full text-white font-medium',
            PRIORITY_CONFIG[task.priority]?.color || 'bg-gray-500'
          )}
        >
          {PRIORITY_CONFIG[task.priority]?.label || task.priority}
        </span>
        {task.aiRiskScore > 70 && (
          <div className="flex items-center gap-1 text-xs text-orange-600 dark:text-orange-400">
            <FiZap className="text-sm" />
            <span>High Risk</span>
          </div>
        )}
      </div>

      {/* Task Title */}
      <h4 className="font-medium text-gray-900 dark:text-gray-100 mb-2 line-clamp-2">
        {task.title}
      </h4>

      {/* Task Description */}
      {task.description && (
        <p className="text-sm text-gray-600 dark:text-gray-400 mb-3 line-clamp-2">
          {task.description}
        </p>
      )}

      {/* Due Date */}
      {task.dueDate && (
        <div
          className={clsx(
            'flex items-center gap-1 text-xs mb-3',
            isOverdue
              ? 'text-red-600 dark:text-red-400'
              : isDueToday
              ? 'text-orange-600 dark:text-orange-400'
              : 'text-gray-600 dark:text-gray-400'
          )}
        >
          <FiCalendar />
          <span>{format(new Date(task.dueDate), 'MMM dd, yyyy')}</span>
          {isOverdue && <FiAlertCircle className="ml-1" />}
        </div>
      )}

      {/* Footer */}
      <div className="flex items-center justify-between pt-3 border-t border-gray-200 dark:border-gray-700">
        {/* Assignee */}
        <div className="flex items-center -space-x-2">
          {task.assignedTo ? (
            <div
              className="w-6 h-6 rounded-full bg-primary-500 flex items-center justify-center text-white text-xs font-medium border-2 border-white dark:border-gray-800"
              title={task.assignedTo.fullName}
            >
              {task.assignedTo.fullName?.charAt(0).toUpperCase()}
            </div>
          ) : (
            <div className="w-6 h-6 rounded-full bg-gray-300 dark:bg-gray-600 border-2 border-white dark:border-gray-800" />
          )}
        </div>

        {/* Metadata */}
        <div className="flex items-center gap-3 text-gray-500 dark:text-gray-400">
          {task.commentCount > 0 && (
            <div className="flex items-center gap-1 text-xs">
              <FiMessageSquare />
              <span>{task.commentCount}</span>
            </div>
          )}
          {task.attachmentCount > 0 && (
            <div className="flex items-center gap-1 text-xs">
              <FiPaperclip />
              <span>{task.attachmentCount}</span>
            </div>
          )}
        </div>
      </div>
    </div>
  )
}

export default TaskCard
