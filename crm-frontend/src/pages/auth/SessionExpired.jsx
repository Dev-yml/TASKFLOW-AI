import { useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { motion } from 'framer-motion'
import { FiClock, FiRefreshCw } from 'react-icons/fi'
import { useDispatch } from 'react-redux'
import { logout } from '../../store/slices/authSlice'

const SessionExpired = () => {
  const navigate = useNavigate()
  const dispatch = useDispatch()

  useEffect(() => {
    // Clear auth state
    dispatch(logout())
  }, [dispatch])

  const handleLogin = () => {
    navigate('/login')
  }

  return (
    <div className="min-h-screen flex items-center justify-center p-4 bg-gradient-to-br from-gray-50 via-blue-50 to-purple-50 dark:from-gray-900 dark:via-blue-900/20 dark:to-purple-900/20">
      <motion.div
        initial={{ opacity: 0, scale: 0.9 }}
        animate={{ opacity: 1, scale: 1 }}
        transition={{ duration: 0.5 }}
        className="max-w-md w-full"
      >
        <div className="bg-white dark:bg-gray-800 rounded-2xl shadow-2xl p-8 border border-gray-200 dark:border-gray-700">
          {/* Icon */}
          <motion.div
            initial={{ scale: 0 }}
            animate={{ scale: 1 }}
            transition={{ delay: 0.2, type: 'spring' }}
            className="w-20 h-20 mx-auto mb-6 bg-orange-100 dark:bg-orange-900/30 rounded-full flex items-center justify-center"
          >
            <FiClock className="text-orange-600 dark:text-orange-400" size={40} />
          </motion.div>

          {/* Content */}
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.3 }}
            className="text-center space-y-4"
          >
            <h2 className="text-2xl font-bold text-gray-900 dark:text-white">
              Session Expired
            </h2>
            <p className="text-gray-600 dark:text-gray-400">
              Your session has expired due to inactivity. Please log in again to continue.
            </p>

            {/* Security Info */}
            <div className="bg-blue-50 dark:bg-blue-900/20 rounded-lg p-4 text-left">
              <p className="text-sm text-blue-900 dark:text-blue-300">
                <strong>Security Notice:</strong> For your protection, we automatically log you out after a period of inactivity.
              </p>
            </div>

            {/* Action Button */}
            <motion.button
              whileHover={{ scale: 1.02 }}
              whileTap={{ scale: 0.98 }}
              onClick={handleLogin}
              className="w-full flex items-center justify-center space-x-2 px-6 py-3 bg-gradient-to-r from-blue-600 to-purple-600 text-white rounded-lg font-semibold shadow-lg hover:shadow-xl transition-all mt-6"
            >
              <FiRefreshCw size={20} />
              <span>Log In Again</span>
            </motion.button>
          </motion.div>
        </div>

        {/* Help Text */}
        <motion.p
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          transition={{ delay: 0.5 }}
          className="text-center text-sm text-gray-600 dark:text-gray-400 mt-6"
        >
          Need help?{' '}
          <a href="#" className="text-blue-600 hover:text-blue-700 dark:text-blue-400 dark:hover:text-blue-300 font-medium">
            Contact Support
          </a>
        </motion.p>
      </motion.div>
    </div>
  )
}

export default SessionExpired
