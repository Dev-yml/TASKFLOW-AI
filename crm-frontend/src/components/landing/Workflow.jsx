import { useRef } from 'react'
import { motion, useInView } from 'framer-motion'
import { Users, FolderPlus, CheckSquare, MessageCircle, TrendingUp, ArrowRight } from 'lucide-react'

const STEPS = [
  {
    icon: Users,
    number: '01',
    title: 'Create your workspace',
    description: 'Set up your team workspace in seconds and invite members with role-based access control.',
  },
  {
    icon: FolderPlus,
    number: '02',
    title: 'Organise into projects',
    description: 'Structure work into projects with custom workflows, milestones, and team assignments.',
  },
  {
    icon: CheckSquare,
    number: '03',
    title: 'Manage tasks with AI',
    description: 'Break down projects into actionable tasks. AI automatically prioritises based on urgency.',
  },
  {
    icon: MessageCircle,
    number: '04',
    title: 'Collaborate in real time',
    description: 'Communicate instantly with integrated chat, comments, @mentions, and file sharing.',
  },
  {
    icon: TrendingUp,
    number: '05',
    title: 'Track & improve',
    description: 'Monitor progress with analytics dashboards and AI-powered productivity insights.',
  },
]

const STATS = [
  { value: '10K+', label: 'Active users' },
  { value: '50K+', label: 'Tasks completed' },
  { value: '99.9%', label: 'Uptime' },
  { value: '4.9/5', label: 'User rating' },
]

const Workflow = () => {
  const ref = useRef(null)
  const isInView = useInView(ref, { once: true, margin: '-80px' })

  return (
    <section id="workflow" className="py-24 px-4 sm:px-6 border-t border-gray-100 dark:border-zinc-800/60 bg-gray-50/40 dark:bg-[#0D0D10]">
      <div className="max-w-5xl mx-auto">

        {/* Header */}
        <motion.div
          ref={ref}
          initial={{ opacity: 0, y: 20 }}
          animate={isInView ? { opacity: 1, y: 0 } : {}}
          transition={{ duration: 0.5 }}
          className="mb-16"
        >
          <div className="inline-flex items-center gap-2 border border-emerald-500/25 bg-emerald-50 dark:bg-emerald-950/40 text-emerald-700 dark:text-emerald-400 px-3 py-1 rounded-full text-xs font-semibold tracking-wide mb-4">
            How it works
          </div>
          <div className="flex flex-col sm:flex-row sm:items-end justify-between gap-4">
            <div>
              <h2 className="text-3xl sm:text-4xl font-bold tracking-tight text-gray-900 dark:text-white leading-tight mb-3">
                Simple workflow,<br className="hidden sm:block" /> powerful results
              </h2>
              <p className="text-base text-gray-500 dark:text-zinc-400 max-w-lg leading-relaxed">
                From workspace setup to analytics, our workflow keeps your team aligned and productive in five clear steps.
              </p>
            </div>
          </div>
        </motion.div>

        {/* Steps — vertical timeline on mobile, horizontal on desktop */}
        <div className="relative">

          {/* Desktop: horizontal connector line */}
          <div className="hidden lg:block absolute top-8 left-[calc(10%-0px)] right-[calc(10%-0px)] h-px bg-gray-200 dark:bg-zinc-800 z-0" />

          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-5 gap-6 lg:gap-4 relative z-10">
            {STEPS.map((step, index) => {
              const Icon = step.icon
              return (
                <motion.div
                  key={step.number}
                  initial={{ opacity: 0, y: 24 }}
                  animate={isInView ? { opacity: 1, y: 0 } : {}}
                  transition={{ duration: 0.45, delay: index * 0.09, ease: 'easeOut' }}
                  className="group relative flex flex-col"
                >
                  {/* Mobile: left border timeline */}
                  <div className="sm:hidden absolute left-0 top-0 bottom-0 w-px bg-gray-200 dark:bg-zinc-800" />

                  <div className="sm:hidden pl-6">
                    <div className="flex items-center gap-3 mb-2">
                      <div className="flex-shrink-0 w-8 h-8 rounded-lg border border-gray-200 dark:border-zinc-700 bg-white dark:bg-zinc-900 flex items-center justify-center shadow-sm">
                        <Icon size={15} className="text-[#4F46E5]" />
                      </div>
                      <span className="text-[11px] font-bold text-gray-300 dark:text-zinc-600 tracking-widest">{step.number}</span>
                    </div>
                    <h3 className="text-[14px] font-semibold text-gray-900 dark:text-white mb-1.5">{step.title}</h3>
                    <p className="text-[13px] text-gray-500 dark:text-zinc-400 leading-relaxed">{step.description}</p>
                  </div>

                  {/* Desktop card */}
                  <div className="hidden sm:flex flex-col items-start lg:items-center lg:text-center gap-3 p-5 rounded-xl border border-gray-200 dark:border-zinc-800 bg-white dark:bg-[#18181B] hover:border-gray-300 dark:hover:border-zinc-700 hover:shadow-md dark:hover:shadow-black/30 transition-all duration-200 h-full group-hover:-translate-y-1">
                    {/* Step icon with number badge */}
                    <div className="relative flex-shrink-0">
                      <div className="w-10 h-10 rounded-xl border border-gray-200 dark:border-zinc-700 bg-gray-50 dark:bg-zinc-900 flex items-center justify-center">
                        <Icon size={17} className="text-[#4F46E5]" />
                      </div>
                      <div className="absolute -top-1.5 -right-1.5 w-5 h-5 rounded-full bg-white dark:bg-zinc-800 border border-gray-200 dark:border-zinc-700 flex items-center justify-center">
                        <span className="text-[8px] font-bold text-[#4F46E5]">{step.number}</span>
                      </div>
                    </div>

                    {/* Arrow between steps (desktop) */}
                    {index < STEPS.length - 1 && (
                      <div className="hidden lg:flex absolute -right-2 top-1/2 -translate-y-1/2 z-20">
                        <ArrowRight size={12} className="text-gray-300 dark:text-zinc-700" />
                      </div>
                    )}

                    <div>
                      <h3 className="text-[13px] font-semibold text-gray-900 dark:text-white mb-1.5 leading-snug">{step.title}</h3>
                      <p className="text-[12px] text-gray-500 dark:text-zinc-400 leading-relaxed">{step.description}</p>
                    </div>
                  </div>
                </motion.div>
              )
            })}
          </div>
        </div>

        {/* Stats bar */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={isInView ? { opacity: 1, y: 0 } : {}}
          transition={{ duration: 0.5, delay: 0.6 }}
          className="mt-16 grid grid-cols-2 md:grid-cols-4 gap-px bg-gray-200 dark:bg-zinc-800 rounded-xl overflow-hidden border border-gray-200 dark:border-zinc-800"
        >
          {STATS.map((stat) => (
            <div key={stat.label} className="bg-white dark:bg-[#18181B] px-6 py-5 text-center">
              <div className="text-2xl font-bold text-gray-900 dark:text-white">{stat.value}</div>
              <div className="text-xs text-gray-500 dark:text-zinc-500 mt-1">{stat.label}</div>
            </div>
          ))}
        </motion.div>
      </div>
    </section>
  )
}

export default Workflow
