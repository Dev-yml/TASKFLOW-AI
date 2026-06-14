import { useRef, useState } from 'react'
import { motion, useInView, AnimatePresence } from 'framer-motion'
import { Link } from 'react-router-dom'
import { Cpu, Clock, MessageSquare, Zap, TrendingUp, ListChecks, ArrowRight, CheckCircle, AlertCircle, Sparkles } from 'lucide-react'

const AI_FEATURES = [
  {
    id: 'prioritization',
    icon: Cpu,
    label: 'Prioritization',
    title: 'Smart Task Prioritization',
    description: 'AI analyses deadlines, complexity, and team capacity to automatically surface what needs attention first.',
    demo: (
      <div className="space-y-2.5">
        <div className="text-[11px] font-semibold text-zinc-500 dark:text-zinc-600 uppercase tracking-widest mb-3">AI Output</div>
        {[
          { task: 'API Integration', priority: 'URGENT', days: '3 days left', color: 'text-red-600 dark:text-red-400 bg-red-50 dark:bg-red-950/50 border-red-200 dark:border-red-900' },
          { task: 'Auth Flow Refactor', priority: 'HIGH', days: '5 days left', color: 'text-amber-600 dark:text-amber-400 bg-amber-50 dark:bg-amber-950/50 border-amber-200 dark:border-amber-900' },
          { task: 'Design System Update', priority: 'MEDIUM', days: '2 weeks left', color: 'text-blue-600 dark:text-blue-400 bg-blue-50 dark:bg-blue-950/50 border-blue-200 dark:border-blue-900' },
        ].map((item, i) => (
          <motion.div
            key={item.task}
            initial={{ opacity: 0, x: 10 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ delay: i * 0.08 }}
            className="flex items-center justify-between p-3 rounded-lg bg-white dark:bg-zinc-900 border border-gray-200 dark:border-zinc-700/60"
          >
            <div className="flex items-center gap-2.5">
              <AlertCircle size={13} className="text-gray-400 flex-shrink-0" />
              <span className="text-[13px] font-medium text-gray-800 dark:text-zinc-200">{item.task}</span>
            </div>
            <div className="flex items-center gap-2">
              <span className="text-[10px] text-gray-400 dark:text-zinc-500">{item.days}</span>
              <span className={`text-[10px] font-bold px-2 py-0.5 rounded border ${item.color}`}>{item.priority}</span>
            </div>
          </motion.div>
        ))}
        <div className="flex items-center gap-1.5 mt-3 text-[11px] text-[#4F46E5] dark:text-indigo-400 font-medium">
          <Sparkles size={11} /> Accuracy: 94% · Saves 2.5 hrs/week
        </div>
      </div>
    ),
  },
  {
    id: 'deadline',
    icon: Clock,
    label: 'Deadlines',
    title: 'Deadline Prediction',
    description: 'Get accurate deadline estimates based on historical velocity and current team capacity.',
    demo: (
      <div className="space-y-4">
        <div className="text-[11px] font-semibold text-zinc-500 dark:text-zinc-600 uppercase tracking-widest">Prediction</div>
        <div className="p-4 rounded-lg bg-white dark:bg-zinc-900 border border-gray-200 dark:border-zinc-700/60">
          <div className="text-[12px] text-gray-500 dark:text-zinc-400 mb-2">Task: "Database Migration"</div>
          <div className="text-2xl font-bold text-gray-900 dark:text-white mb-1">6 days</div>
          <div className="text-[12px] text-emerald-600 dark:text-emerald-400 font-medium mb-3">Deadline: Jun 18, 2026</div>
          <div className="space-y-2">
            {[
              { label: 'Confidence', value: '85%', w: 'w-[85%]', color: 'bg-emerald-500' },
              { label: 'Risk level', value: 'Medium', w: 'w-[55%]', color: 'bg-amber-400' },
            ].map(b => (
              <div key={b.label}>
                <div className="flex justify-between text-[11px] text-gray-500 dark:text-zinc-500 mb-1">
                  <span>{b.label}</span><span className="font-medium text-gray-700 dark:text-zinc-300">{b.value}</span>
                </div>
                <div className="h-1.5 bg-gray-100 dark:bg-zinc-800 rounded-full overflow-hidden">
                  <motion.div initial={{ width: 0 }} animate={{ width: b.w }} transition={{ duration: 0.8, delay: 0.2 }} className={`h-full ${b.color} rounded-full`} />
                </div>
              </div>
            ))}
          </div>
        </div>
        <div className="flex items-center gap-1.5 text-[11px] text-[#4F46E5] dark:text-indigo-400 font-medium">
          <Sparkles size={11} /> Based on 240 similar tasks
        </div>
      </div>
    ),
  },
  {
    id: 'summarization',
    icon: MessageSquare,
    label: 'Summary',
    title: 'Chat Summarization',
    description: 'Instantly summarise long conversations with key points, decisions, and action items.',
    demo: (
      <div className="space-y-3">
        <div className="text-[11px] font-semibold text-zinc-500 dark:text-zinc-600 uppercase tracking-widest">Summary — #project-alpha (50 messages)</div>
        {[
          { label: 'Key Decision', icon: CheckCircle, color: 'text-emerald-600 dark:text-emerald-400', text: 'JWT auth approved for v1 release.' },
          { label: 'Action Item', icon: AlertCircle, color: 'text-amber-600 dark:text-amber-400', text: 'Jane: prepare API docs by Friday.' },
          { label: 'Action Item', icon: AlertCircle, color: 'text-amber-600 dark:text-amber-400', text: 'Bob: review API design proposal.' },
        ].map((item, i) => {
          const Icon = item.icon
          return (
            <motion.div key={i} initial={{ opacity: 0, x: 10 }} animate={{ opacity: 1, x: 0 }} transition={{ delay: i * 0.1 }}
              className="flex items-start gap-2.5 p-3 rounded-lg bg-white dark:bg-zinc-900 border border-gray-200 dark:border-zinc-700/60">
              <Icon size={13} className={`${item.color} mt-0.5 flex-shrink-0`} />
              <div>
                <div className={`text-[10px] font-bold uppercase tracking-wide mb-0.5 ${item.color}`}>{item.label}</div>
                <div className="text-[12.5px] text-gray-700 dark:text-zinc-300">{item.text}</div>
              </div>
            </motion.div>
          )
        })}
        <div className="flex items-center gap-1.5 text-[11px] text-[#4F46E5] dark:text-indigo-400 font-medium">
          <Sparkles size={11} /> Generated in 1.2s
        </div>
      </div>
    ),
  },
  {
    id: 'replies',
    icon: Zap,
    label: 'Smart Reply',
    title: 'Smart Reply Suggestions',
    description: 'Context-aware reply suggestions help you respond faster and more professionally.',
    demo: (
      <div className="space-y-3">
        <div className="p-3 rounded-lg bg-gray-100 dark:bg-zinc-800 text-[13px] text-gray-700 dark:text-zinc-300">
          <span className="text-[11px] font-semibold text-gray-400 dark:text-zinc-500 block mb-1">Message received</span>
          "Can you review my PR before the standup?"
        </div>
        <div className="text-[11px] font-semibold text-zinc-500 dark:text-zinc-600 uppercase tracking-widest">AI Suggestions</div>
        {[
          "Sure, I'll review it before the standup.",
          "Added to my queue. Will get back by EOD.",
          "Reviewing now — will leave comments shortly.",
        ].map((s, i) => (
          <motion.button key={i} initial={{ opacity: 0, x: 10 }} animate={{ opacity: 1, x: 0 }} transition={{ delay: i * 0.08 }}
            className="w-full text-left p-3 rounded-lg bg-white dark:bg-zinc-900 border border-gray-200 dark:border-zinc-700/60 text-[13px] text-gray-800 dark:text-zinc-200 hover:border-[#4F46E5]/40 hover:bg-[#4F46E5]/4 dark:hover:bg-indigo-950/30 transition-colors">
            {s}
          </motion.button>
        ))}
      </div>
    ),
  },
  {
    id: 'insights',
    icon: TrendingUp,
    label: 'Insights',
    title: 'Productivity Insights',
    description: 'Personalised recommendations to improve your workflow and boost overall team productivity.',
    demo: (
      <div className="space-y-4">
        <div className="text-[11px] font-semibold text-zinc-500 dark:text-zinc-600 uppercase tracking-widest">Your score this week</div>
        <div className="flex items-center gap-4 p-4 rounded-lg bg-white dark:bg-zinc-900 border border-gray-200 dark:border-zinc-700/60">
          <div className="text-4xl font-bold text-gray-900 dark:text-white">82</div>
          <div>
            <div className="text-[12px] text-gray-500 dark:text-zinc-400">Productivity score</div>
            <div className="flex items-center gap-1 text-[11px] text-emerald-600 dark:text-emerald-400 font-medium mt-0.5">
              <TrendingUp size={11} /> +7 from last week
            </div>
          </div>
          <div className="ml-auto">
            <div className="w-12 h-12 rounded-full border-4 border-[#4F46E5]/20 flex items-center justify-center">
              <div className="w-8 h-8 rounded-full bg-[#4F46E5]/10 border-2 border-[#4F46E5] flex items-center justify-center text-[10px] font-bold text-[#4F46E5]">B+</div>
            </div>
          </div>
        </div>
        <div className="space-y-1.5">
          {[
            { label: 'Task completion rate', pct: 85, color: 'bg-emerald-500' },
            { label: 'On-time delivery', pct: 72, color: 'bg-[#4F46E5]' },
          ].map(b => (
            <div key={b.label}>
              <div className="flex justify-between text-[11px] text-gray-500 dark:text-zinc-500 mb-1">
                <span>{b.label}</span><span className="font-medium">{b.pct}%</span>
              </div>
              <div className="h-1.5 bg-gray-100 dark:bg-zinc-800 rounded-full overflow-hidden">
                <motion.div initial={{ width: 0 }} animate={{ width: `${b.pct}%` }} transition={{ duration: 0.8, delay: 0.2 }} className={`h-full ${b.color} rounded-full`} />
              </div>
            </div>
          ))}
        </div>
      </div>
    ),
  },
  {
    id: 'subtasks',
    icon: ListChecks,
    label: 'Subtasks',
    title: 'AI-Generated Subtasks',
    description: 'Break down complex tasks into actionable subtasks automatically with one click.',
    demo: (
      <div className="space-y-3">
        <div className="p-3 rounded-lg bg-gray-100 dark:bg-zinc-800 text-[13px] text-gray-700 dark:text-zinc-300">
          <span className="text-[11px] font-semibold text-gray-400 dark:text-zinc-500 block mb-1">Task</span>
          "Build user authentication system"
        </div>
        <div className="text-[11px] font-semibold text-zinc-500 dark:text-zinc-600 uppercase tracking-widest">Generated subtasks</div>
        {[
          'Design database schema for users',
          'Implement JWT token generation',
          'Create login / register endpoints',
          'Add password hashing with bcrypt',
          'Write unit tests for auth flow',
        ].map((task, i) => (
          <motion.div key={i} initial={{ opacity: 0, x: 10 }} animate={{ opacity: 1, x: 0 }} transition={{ delay: i * 0.07 }}
            className="flex items-center gap-2.5 p-2.5 rounded-lg bg-white dark:bg-zinc-900 border border-gray-200 dark:border-zinc-700/60">
            <div className="w-4 h-4 rounded border border-gray-300 dark:border-zinc-600 flex-shrink-0" />
            <span className="text-[12.5px] text-gray-800 dark:text-zinc-200">{task}</span>
          </motion.div>
        ))}
      </div>
    ),
  },
]

const AIShowcase = () => {
  const ref = useRef(null)
  const isInView = useInView(ref, { once: true, margin: '-80px' })
  const [active, setActive] = useState(AI_FEATURES[0])

  return (
    <section id="ai" className="py-24 px-4 sm:px-6 border-t border-gray-100 dark:border-zinc-800/60">
      <div className="max-w-5xl mx-auto">

        {/* Header */}
        <motion.div
          ref={ref}
          initial={{ opacity: 0, y: 20 }}
          animate={isInView ? { opacity: 1, y: 0 } : {}}
          transition={{ duration: 0.5 }}
          className="mb-14"
        >
          <div className="inline-flex items-center gap-2 border border-[#4F46E5]/25 bg-[#4F46E5]/5 text-[#4F46E5] dark:text-indigo-400 px-3 py-1 rounded-full text-xs font-semibold tracking-wide mb-4">
            <Cpu size={11} />
            AI-Powered Intelligence
          </div>
          <h2 className="text-3xl sm:text-4xl font-bold tracking-tight text-gray-900 dark:text-white leading-tight mb-3">
            Your AI assistant for<br className="hidden sm:block" /> smarter work
          </h2>
          <p className="text-base text-gray-500 dark:text-zinc-400 max-w-xl leading-relaxed">
            Powered by advanced language models, our AI learns your team's patterns and surfaces the right insights at the right time.
          </p>
        </motion.div>

        <div className="grid lg:grid-cols-[260px_1fr] gap-6">

          {/* Feature tab list */}
          <motion.div
            initial={{ opacity: 0, x: -16 }}
            animate={isInView ? { opacity: 1, x: 0 } : {}}
            transition={{ duration: 0.5, delay: 0.1 }}
            className="flex lg:flex-col gap-2 overflow-x-auto pb-2 lg:pb-0 lg:overflow-visible"
          >
            {AI_FEATURES.map((f) => {
              const Icon = f.icon
              const isActive = active.id === f.id
              return (
                <button
                  key={f.id}
                  onClick={() => setActive(f)}
                  className={`flex items-center gap-2.5 px-3.5 py-2.5 rounded-lg text-left transition-all flex-shrink-0 lg:flex-shrink border ${
                    isActive
                      ? 'bg-[#4F46E5] text-white border-[#4F46E5] shadow-md shadow-indigo-500/20'
                      : 'bg-white dark:bg-[#18181B] text-gray-600 dark:text-zinc-400 border-gray-200 dark:border-zinc-800 hover:border-gray-300 dark:hover:border-zinc-700 hover:text-gray-900 dark:hover:text-white'
                  }`}
                >
                  <Icon size={15} className="flex-shrink-0" />
                  <span className="text-[13px] font-medium whitespace-nowrap">{f.label}</span>
                </button>
              )
            })}
          </motion.div>

          {/* Demo panel */}
          <motion.div
            initial={{ opacity: 0, x: 16 }}
            animate={isInView ? { opacity: 1, x: 0 } : {}}
            transition={{ duration: 0.5, delay: 0.15 }}
            className="rounded-xl border border-gray-200 dark:border-zinc-800 bg-white dark:bg-[#18181B] overflow-hidden"
          >
            {/* Panel header */}
            <div className="px-6 py-5 border-b border-gray-100 dark:border-zinc-800">
              <div className="flex items-start gap-3">
                <div className="p-2 rounded-lg bg-[#4F46E5]/10 dark:bg-indigo-950/50 flex-shrink-0">
                  {(() => { const Icon = active.icon; return <Icon size={16} className="text-[#4F46E5] dark:text-indigo-400" /> })()}
                </div>
                <div>
                  <h3 className="text-[15px] font-semibold text-gray-900 dark:text-white">{active.title}</h3>
                  <p className="text-[13px] text-gray-500 dark:text-zinc-400 mt-0.5 leading-relaxed">{active.description}</p>
                </div>
              </div>
            </div>

            {/* Demo content */}
            <div className="p-6">
              <AnimatePresence mode="wait">
                <motion.div
                  key={active.id}
                  initial={{ opacity: 0, y: 10 }}
                  animate={{ opacity: 1, y: 0 }}
                  exit={{ opacity: 0, y: -10 }}
                  transition={{ duration: 0.25 }}
                >
                  {active.demo}
                </motion.div>
              </AnimatePresence>
            </div>
          </motion.div>
        </div>

        {/* CTA */}
        <motion.div
          initial={{ opacity: 0, y: 16 }}
          animate={isInView ? { opacity: 1, y: 0 } : {}}
          transition={{ duration: 0.5, delay: 0.4 }}
          className="mt-12 flex flex-col sm:flex-row items-center justify-between gap-4 p-6 rounded-xl border border-gray-200 dark:border-zinc-800 bg-gray-50/60 dark:bg-zinc-900/40"
        >
          <div>
            <div className="text-[15px] font-semibold text-gray-900 dark:text-white mb-1">Ready to experience AI-powered work?</div>
            <div className="text-[13px] text-gray-500 dark:text-zinc-400">All AI features are included in every plan, including Free.</div>
          </div>
          <Link to="/register" className="flex-shrink-0">
            <motion.button
              whileHover={{ scale: 1.02 }}
              whileTap={{ scale: 0.98 }}
              className="flex items-center gap-2 px-5 py-2.5 bg-[#4F46E5] hover:bg-[#4338CA] text-white text-sm font-medium rounded-lg transition-colors shadow-sm"
            >
              Try AI features free
              <ArrowRight size={14} />
            </motion.button>
          </Link>
        </motion.div>
      </div>
    </section>
  )
}

export default AIShowcase
