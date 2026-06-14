import { useMemo, useState } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { useMutation } from '@tanstack/react-query'
import { AnimatePresence, motion } from 'framer-motion'
import ReactMarkdown from 'react-markdown'
import { FiCpu, FiMaximize2, FiMessageCircle, FiSend, FiTrash2, FiX, FiZap } from 'react-icons/fi'
import { aiService } from '../../services/aiService'
import { addCopilotMessage, clearCopilotMessages, closeCopilot, toggleCopilot } from '../../store/slices/copilotSlice'
import { useStreamingText } from '../../hooks/useStreamingText'

const QUICK_COMMANDS = [
  'Summarize my productivity and risks',
  'What tasks or deals need attention?',
  'Generate a practical sprint plan',
  'Who may be overloaded?',
]

const GlobalCopilot = () => {
  const dispatch = useDispatch()
  const { isOpen, context, messages } = useSelector((state) => state.copilot)
  const { user } = useSelector((state) => state.auth)
  const [prompt, setPrompt] = useState('')
  const latestAssistant = [...messages].reverse().find((message) => message.role === 'assistant')?.content || ''
  const streamedLatest = useStreamingText(latestAssistant, 7)

  const history = useMemo(() => messages.map((message) => `${message.role}: ${message.content}`).join('\n'), [messages])

  const mutation = useMutation({
    mutationFn: (message) => aiService.getSmartReplies({
      message,
      context: `You are the AI copilot for ${user?.fullName || 'this user'} inside the Task Manager + CRM platform. Current context: ${JSON.stringify(context)}. Answer with concise markdown. Use only the product data context provided by the user and existing analytics-style reasoning.`,
      conversationHistory: history,
      suggestionCount: 4,
    }),
    onSuccess: (data) => {
      const content = [
        ...(data.suggestions || []),
        ...(data.actionRecommendations || []).map((item) => `Action: ${item}`),
        ...(data.followUpQuestions || []).map((item) => `Question: ${item}`),
      ].map((item) => `- ${item}`).join('\n')
      dispatch(addCopilotMessage({ role: 'assistant', content, createdAt: new Date().toISOString() }))
    },
    onError: (error) => {
      dispatch(addCopilotMessage({
        role: 'assistant',
        content: `AI request failed: ${error.message || 'Please try again.'}`,
        createdAt: new Date().toISOString(),
      }))
    },
  })

  const submitPrompt = (value = prompt) => {
    const clean = value.trim()
    if (!clean || mutation.isPending) return
    dispatch(addCopilotMessage({ role: 'user', content: clean, createdAt: new Date().toISOString() }))
    setPrompt('')
    mutation.mutate(clean)
  }

  return (
    <>
      <motion.button
        type="button"
        whileHover={{ scale: 1.05 }}
        whileTap={{ scale: 0.95 }}
        onClick={() => dispatch(toggleCopilot())}
        className="fixed bottom-6 right-6 z-40 flex h-14 w-14 items-center justify-center rounded-2xl bg-gradient-to-br from-cyan-500 via-blue-600 to-violet-600 text-white shadow-2xl shadow-cyan-500/30 ring-4 ring-white/60 dark:ring-gray-900/60"
      >
        <FiCpu size={24} />
      </motion.button>

      <AnimatePresence>
        {isOpen && (
          <motion.aside
            initial={{ opacity: 0, x: 60, scale: 0.96 }}
            animate={{ opacity: 1, x: 0, scale: 1 }}
            exit={{ opacity: 0, x: 60, scale: 0.96 }}
            className="fixed bottom-24 right-4 z-50 flex h-[75vh] w-[calc(100vw-2rem)] max-w-lg flex-col overflow-hidden rounded-[2rem] border border-cyan-300/40 bg-white/95 shadow-2xl shadow-cyan-500/20 backdrop-blur-xl dark:border-cyan-500/20 dark:bg-gray-900/95 sm:right-6"
          >
            <header className="flex items-center justify-between border-b border-gray-200 p-4 dark:border-gray-800">
              <div className="flex items-center gap-3">
                <div className="rounded-2xl bg-gradient-to-br from-cyan-500 to-violet-600 p-3 text-white">
                  <FiMessageCircle />
                </div>
                <div>
                  <h2 className="font-black text-gray-950 dark:text-white">AI Copilot</h2>
                  <p className="text-xs text-gray-500">Context-aware xAI (Grok) assistant</p>
                </div>
              </div>
              <div className="flex items-center gap-1">
                <button className="p-2 text-gray-500 hover:text-gray-900 dark:hover:text-white" onClick={() => dispatch(clearCopilotMessages())}>
                  <FiTrash2 />
                </button>
                <button className="p-2 text-gray-500 hover:text-gray-900 dark:hover:text-white" onClick={() => dispatch(closeCopilot())}>
                  <FiX />
                </button>
              </div>
            </header>

            <div className="flex-1 space-y-4 overflow-y-auto p-4">
              {messages.length === 0 && (
                <div className="rounded-3xl bg-gradient-to-br from-cyan-50 to-violet-50 p-4 dark:from-cyan-950/30 dark:to-violet-950/30">
                  <div className="mb-3 flex items-center gap-2 text-sm font-bold text-cyan-700 dark:text-cyan-300">
                    <FiZap />
                    Quick commands
                  </div>
                  <div className="grid gap-2">
                    {QUICK_COMMANDS.map((command) => (
                      <button
                        type="button"
                        key={command}
                        className="rounded-2xl bg-white/80 px-3 py-2 text-left text-sm font-semibold text-gray-700 shadow-sm transition hover:bg-white dark:bg-gray-800/80 dark:text-gray-200"
                        onClick={() => submitPrompt(command)}
                      >
                        {command}
                      </button>
                    ))}
                  </div>
                </div>
              )}

              {messages.map((message, index) => {
                const isLatestAssistant = message.role === 'assistant' && message.content === latestAssistant
                return (
                  <div
                    key={`${message.createdAt}-${index}`}
                    className={`rounded-3xl p-4 ${message.role === 'user' ? 'ml-10 bg-blue-600 text-white' : 'mr-10 bg-gray-100 text-gray-800 dark:bg-gray-800 dark:text-gray-200'}`}
                  >
                    <ReactMarkdown>{isLatestAssistant ? streamedLatest : message.content}</ReactMarkdown>
                  </div>
                )
              })}

              {mutation.isPending && (
                <div className="mr-10 rounded-3xl bg-gray-100 p-4 text-sm font-semibold text-gray-600 dark:bg-gray-800 dark:text-gray-300">
                  Grok is thinking...
                </div>
              )}
            </div>

            <form
              onSubmit={(event) => {
                event.preventDefault()
                submitPrompt()
              }}
              className="border-t border-gray-200 p-4 dark:border-gray-800"
            >
              <div className="flex items-end gap-2 rounded-3xl bg-gray-100 p-2 dark:bg-gray-800">
                <textarea
                  value={prompt}
                  onChange={(event) => setPrompt(event.target.value)}
                  placeholder="Ask about tasks, deals, workload, or sprint planning..."
                  rows={2}
                  className="min-h-[48px] flex-1 resize-none bg-transparent px-3 py-2 text-sm outline-none"
                />
                <button type="submit" disabled={mutation.isPending || !prompt.trim()} className="rounded-2xl bg-blue-600 p-3 text-white disabled:opacity-40">
                  <FiSend />
                </button>
              </div>
              <div className="mt-2 flex items-center gap-2 text-xs text-gray-500">
                <FiMaximize2 />
                {context.page ? `Using context: ${context.page}` : 'No page context set'}
              </div>
            </form>
          </motion.aside>
        )}
      </AnimatePresence>
    </>
  )
}

export default GlobalCopilot
