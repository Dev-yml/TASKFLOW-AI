import { motion } from 'framer-motion'
import ReactMarkdown from 'react-markdown'
import { FiCpu, FiRefreshCw, FiZap } from 'react-icons/fi'
import { useStreamingText } from '../../hooks/useStreamingText'

const AIResponseCard = ({
  title = 'AI response',
  subtitle,
  markdown,
  loading = false,
  error,
  confidence,
  onRetry,
  actions,
}) => {
  const streamed = useStreamingText(markdown || '', 8)

  return (
    <motion.div
      initial={{ opacity: 0, y: 12 }}
      animate={{ opacity: 1, y: 0 }}
      className="relative overflow-hidden rounded-3xl border border-cyan-300/40 bg-white/85 p-5 shadow-lg shadow-cyan-500/10 backdrop-blur-xl dark:border-cyan-500/20 dark:bg-gray-900/85"
    >
      <div className="absolute -right-16 -top-16 h-40 w-40 rounded-full bg-cyan-400/20 blur-3xl" />
      <div className="relative">
        <div className="flex items-start justify-between gap-4">
          <div className="flex items-center gap-3">
            <div className="flex h-11 w-11 items-center justify-center rounded-2xl bg-gradient-to-br from-cyan-500 to-violet-600 text-white shadow-lg">
              <FiCpu size={22} />
            </div>
            <div>
              <h3 className="font-black text-gray-950 dark:text-white">{title}</h3>
              {subtitle && <p className="text-sm text-gray-500 dark:text-gray-400">{subtitle}</p>}
            </div>
          </div>
          {confidence !== undefined && confidence !== null && (
            <div className="rounded-full bg-cyan-50 px-3 py-1 text-xs font-bold text-cyan-700 dark:bg-cyan-950 dark:text-cyan-300">
              {Math.round(Number(confidence) * (Number(confidence) <= 1 ? 100 : 1))}% confidence
            </div>
          )}
        </div>

        {loading && (
          <div className="mt-5 flex items-center gap-3 rounded-2xl bg-cyan-50 p-4 text-cyan-700 dark:bg-cyan-950/50 dark:text-cyan-300">
            <FiZap className="animate-pulse" />
            <div>
              <p className="text-sm font-bold">Generating with Gemini...</p>
              <div className="mt-2 flex gap-1">
                {[0, 1, 2].map((dot) => (
                  <motion.span
                    key={dot}
                    animate={{ y: [0, -5, 0] }}
                    transition={{ duration: 0.7, repeat: Infinity, delay: dot * 0.15 }}
                    className="h-2 w-2 rounded-full bg-cyan-500"
                  />
                ))}
              </div>
            </div>
          </div>
        )}

        {error && (
          <div className="mt-5 rounded-2xl bg-red-50 p-4 text-sm text-red-700 dark:bg-red-950/30 dark:text-red-300">
            {error}
          </div>
        )}

        {markdown && !loading && !error && (
          <div className="prose prose-sm mt-5 max-w-none text-gray-700 prose-strong:text-gray-950 prose-li:my-1 dark:text-gray-300 dark:prose-strong:text-white">
            <ReactMarkdown>{streamed}</ReactMarkdown>
          </div>
        )}

        {(onRetry || actions) && (
          <div className="mt-5 flex flex-wrap items-center gap-2">
            {onRetry && (
              <button type="button" onClick={onRetry} className="btn-outline inline-flex items-center gap-2">
                <FiRefreshCw />
                Regenerate
              </button>
            )}
            {actions}
          </div>
        )}
      </div>
    </motion.div>
  )
}

export default AIResponseCard
