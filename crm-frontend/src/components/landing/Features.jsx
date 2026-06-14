import { useRef } from 'react'
import { motion, useInView } from 'framer-motion'
import { Link } from 'react-router-dom'
import { Cpu, MessageSquare, KanbanSquare, BarChart3, Bell, Sparkles, ArrowRight } from 'lucide-react'

const FEATURES = [
  {
    icon: Cpu,
    title: 'AI Task Prioritization',
    description: 'Let AI analyze your workload and automatically prioritize tasks based on deadlines, complexity, and team capacity.',
    accent: 'text-[#4F46E5] bg-[#4F46E5]/8 dark:bg-indigo-950/50',
  },
  {
    icon: MessageSquare,
    title: 'Real-Time Chat',
    description: 'Collaborate instantly with your team through direct messages, group chats, and project-specific channels.',
    accent: 'text-sky-600 dark:text-sky-400 bg-sky-50 dark:bg-sky-950/50',
  },
  {
    icon: KanbanSquare,
    title: 'CRM Pipeline',
    description: 'Manage leads and deals with intuitive drag-and-drop boards. Track progress from prospect to closed deal.',
    accent: 'text-emerald-600 dark:text-emerald-400 bg-emerald-50 dark:bg-emerald-950/50',
  },
  {
    icon: BarChart3,
    title: 'Analytics Dashboard',
    description: 'Get real-time insights into team performance, project progress, and productivity metrics with beautiful charts.',
    accent: 'text-amber-600 dark:text-amber-400 bg-amber-50 dark:bg-amber-950/50',
  },
  {
    icon: Bell,
    title: 'Smart Notifications',
    description: 'Stay informed with intelligent notifications that learn your preferences and only alert you when it matters.',
    accent: 'text-rose-600 dark:text-rose-400 bg-rose-50 dark:bg-rose-950/50',
  },
  {
    icon: Sparkles,
    title: 'AI Productivity Insights',
    description: 'Receive personalized recommendations to improve your workflow and boost team productivity with AI-powered insights.',
    accent: 'text-violet-600 dark:text-violet-400 bg-violet-50 dark:bg-violet-950/50',
  },
]

const FeatureCard = ({ feature, index }) => {
  const ref = useRef(null)
  const isInView = useInView(ref, { once: true, margin: '-80px' })
  const Icon = feature.icon

  return (
    <motion.div
      ref={ref}
      initial={{ opacity: 0, y: 24 }}
      animate={isInView ? { opacity: 1, y: 0 } : {}}
      transition={{ duration: 0.45, delay: index * 0.07, ease: 'easeOut' }}
      whileHover={{ y: -3, transition: { duration: 0.18 } }}
      className="group p-6 rounded-xl border border-gray-200 dark:border-zinc-800 bg-white dark:bg-[#18181B] hover:border-gray-300 dark:hover:border-zinc-700 hover:shadow-md dark:hover:shadow-black/30 transition-all duration-200 cursor-default"
    >
      <div className={`inline-flex p-2.5 rounded-lg mb-4 ${feature.accent}`}>
        <Icon size={18} />
      </div>
      <h3 className="text-[15px] font-semibold text-gray-900 dark:text-white mb-2 leading-snug">
        {feature.title}
      </h3>
      <p className="text-[13.5px] text-gray-500 dark:text-zinc-400 leading-relaxed">
        {feature.description}
      </p>
    </motion.div>
  )
}

const Features = () => {
  const ref = useRef(null)
  const isInView = useInView(ref, { once: true, margin: '-80px' })

  return (
    <section id="features" className="py-24 px-4 sm:px-6 border-t border-gray-100 dark:border-zinc-800/60">
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
            <Sparkles size={11} />
            Powerful Features
          </div>
          <h2 className="text-3xl sm:text-4xl font-bold tracking-tight text-gray-900 dark:text-white leading-tight mb-4">
            Everything your team needs,<br className="hidden sm:block" /> in one place
          </h2>
          <div className="flex flex-col sm:flex-row sm:items-end justify-between gap-4">
            <p className="text-base text-gray-500 dark:text-zinc-400 max-w-lg leading-relaxed">
              Built for modern teams who want to work smarter, collaborate better, and achieve more with the power of AI.
            </p>
            <Link to="/register">
              <motion.button
                whileHover={{ scale: 1.02 }}
                whileTap={{ scale: 0.98 }}
                className="group flex items-center gap-1.5 text-sm font-medium text-[#4F46E5] dark:text-indigo-400 hover:text-[#4338CA] dark:hover:text-indigo-300 transition-colors flex-shrink-0"
              >
                Explore all features
                <ArrowRight size={14} className="group-hover:translate-x-0.5 transition-transform" />
              </motion.button>
            </Link>
          </div>
        </motion.div>

        {/* Grid */}
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
          {FEATURES.map((feature, index) => (
            <FeatureCard key={feature.title} feature={feature} index={index} />
          ))}
        </div>

        {/* Stats bar */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={isInView ? { opacity: 1, y: 0 } : {}}
          transition={{ duration: 0.5, delay: 0.55 }}
          className="mt-14 rounded-xl border border-gray-200 dark:border-zinc-800 bg-gray-50/60 dark:bg-zinc-900/40 px-8 py-6 grid grid-cols-2 md:grid-cols-4 gap-6 divide-x-0 md:divide-x divide-gray-200 dark:divide-zinc-800"
        >
          {[
            { value: '10K+', label: 'Active teams' },
            { value: '50K+', label: 'Tasks completed daily' },
            { value: '99.9%', label: 'Uptime SLA' },
            { value: '4.9 / 5', label: 'User satisfaction' },
          ].map((stat, i) => (
            <div key={stat.label} className={`text-center ${i > 0 ? 'md:pl-6' : ''}`}>
              <div className="text-2xl font-bold text-gray-900 dark:text-white">{stat.value}</div>
              <div className="text-xs text-gray-500 dark:text-zinc-500 mt-1">{stat.label}</div>
            </div>
          ))}
        </motion.div>
      </div>
    </section>
  )
}

export default Features
