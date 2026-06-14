import { motion } from 'framer-motion'
import { Link } from 'react-router-dom'
import { ArrowRight, Play, CheckCircle2, Users, BarChart3, Sparkles, MessageSquare, KanbanSquare, TrendingUp, Cpu, Clock } from 'lucide-react'

const fadeUp = (delay = 0) => ({
  initial: { opacity: 0, y: 20 },
  animate: { opacity: 1, y: 0 },
  transition: { duration: 0.5, delay, ease: 'easeOut' },
})

const TRUST_ITEMS = [
  'No credit card required',
  '14-day free trial',
  'Cancel anytime',
]

const KANBAN_COLUMNS = [
  {
    label: 'To Do',
    color: 'bg-zinc-100 dark:bg-zinc-800',
    dot: 'bg-zinc-400',
    cards: [
      { title: 'Design system update', tag: 'Design', priority: 'Medium', avatar: 'AJ' },
      { title: 'User research report', tag: 'Research', priority: 'Low', avatar: 'SK' },
    ],
  },
  {
    label: 'In Progress',
    color: 'bg-blue-50 dark:bg-blue-950/40',
    dot: 'bg-blue-500',
    cards: [
      { title: 'API integration', tag: 'Engineering', priority: 'High', avatar: 'MK', ai: true },
      { title: 'Auth flow refactor', tag: 'Engineering', priority: 'High', avatar: 'RL' },
    ],
  },
  {
    label: 'Done',
    color: 'bg-emerald-50 dark:bg-emerald-950/40',
    dot: 'bg-emerald-500',
    cards: [
      { title: 'Q1 roadmap planning', tag: 'Strategy', priority: 'High', avatar: 'PR' },
    ],
  },
]

const PRIORITY_COLORS = {
  High: 'text-red-600 dark:text-red-400 bg-red-50 dark:bg-red-950/40',
  Medium: 'text-amber-600 dark:text-amber-400 bg-amber-50 dark:bg-amber-950/40',
  Low: 'text-zinc-500 dark:text-zinc-400 bg-zinc-100 dark:bg-zinc-800',
}

const TAG_COLORS = {
  Design: 'text-purple-700 dark:text-purple-400 bg-purple-50 dark:bg-purple-950/40',
  Engineering: 'text-blue-700 dark:text-blue-400 bg-blue-50 dark:bg-blue-950/40',
  Research: 'text-teal-700 dark:text-teal-400 bg-teal-50 dark:bg-teal-950/40',
  Strategy: 'text-orange-700 dark:text-orange-400 bg-orange-50 dark:bg-orange-950/40',
}

const AVATAR_COLORS = ['bg-violet-500', 'bg-sky-500', 'bg-emerald-500', 'bg-rose-500', 'bg-amber-500', 'bg-indigo-500']

function avatarColor(initials) {
  const idx = initials.charCodeAt(0) % AVATAR_COLORS.length
  return AVATAR_COLORS[idx]
}

const DashboardMockup = () => (
  <div className="relative w-full rounded-xl overflow-hidden border border-gray-200 dark:border-zinc-800 bg-white dark:bg-[#18181B] shadow-2xl shadow-gray-200/60 dark:shadow-black/40">
    {/* Browser chrome */}
    <div className="flex items-center gap-2 px-4 py-3 border-b border-gray-100 dark:border-zinc-800 bg-gray-50 dark:bg-zinc-900/60">
      <div className="flex gap-1.5">
        <div className="w-3 h-3 rounded-full bg-red-400/70" />
        <div className="w-3 h-3 rounded-full bg-amber-400/70" />
        <div className="w-3 h-3 rounded-full bg-green-400/70" />
      </div>
      <div className="flex-1 mx-3 bg-gray-200/70 dark:bg-zinc-700/50 rounded-md px-3 py-1 text-[11px] text-gray-400 dark:text-zinc-500 font-mono">
        app.taskflow.ai/board
      </div>
    </div>

    {/* App chrome */}
    <div className="flex h-[400px] sm:h-[460px]">
      {/* Sidebar */}
      <div className="hidden sm:flex flex-col w-44 border-r border-gray-100 dark:border-zinc-800 bg-gray-50/50 dark:bg-zinc-900/40 p-3 gap-1 flex-shrink-0">
        <div className="flex items-center gap-2 px-2 py-1.5 mb-2">
          <div className="w-5 h-5 bg-[#4F46E5] rounded flex items-center justify-center">
            <Sparkles size={11} className="text-white" />
          </div>
          <span className="text-xs font-semibold text-gray-800 dark:text-white">TaskFlow</span>
        </div>
        {[
          { icon: BarChart3, label: 'Dashboard', active: false },
          { icon: KanbanSquare, label: 'Board', active: true },
          { icon: MessageSquare, label: 'Chat', active: false },
          { icon: TrendingUp, label: 'Analytics', active: false },
          { icon: Cpu, label: 'AI Copilot', active: false },
        ].map(({ icon: Icon, label, active }) => (
          <div
            key={label}
            className={`flex items-center gap-2 px-2 py-1.5 rounded-md text-xs font-medium transition-colors ${
              active
                ? 'bg-[#4F46E5]/10 text-[#4F46E5] dark:text-indigo-400'
                : 'text-gray-500 dark:text-zinc-500'
            }`}
          >
            <Icon size={13} />
            {label}
          </div>
        ))}

        {/* Team online */}
        <div className="mt-auto pt-3 border-t border-gray-100 dark:border-zinc-800">
          <div className="text-[10px] font-semibold text-gray-400 dark:text-zinc-600 uppercase tracking-wider mb-2 px-2">Team online</div>
          {['AJ', 'SK', 'MK'].map((init, i) => (
            <div key={init} className="flex items-center gap-2 px-2 py-1">
              <div className={`relative w-5 h-5 rounded-full ${avatarColor(init)} flex items-center justify-center text-white text-[9px] font-bold`}>
                {init}
                <span className="absolute -bottom-0.5 -right-0.5 w-2 h-2 bg-emerald-500 rounded-full border border-white dark:border-zinc-900" />
              </div>
              <span className="text-[10px] text-gray-500 dark:text-zinc-500">{['Alex J.', 'Sara K.', 'Mike K.'][i]}</span>
            </div>
          ))}
        </div>
      </div>

      {/* Main content */}
      <div className="flex-1 flex flex-col overflow-hidden">
        {/* Top bar */}
        <div className="flex items-center justify-between px-4 py-3 border-b border-gray-100 dark:border-zinc-800">
          <div>
            <div className="text-sm font-semibold text-gray-900 dark:text-white">Sprint 12 — Q2 Roadmap</div>
            <div className="text-[11px] text-gray-400 dark:text-zinc-500 mt-0.5">14 tasks · 3 in progress · Due Jun 28</div>
          </div>
          <div className="flex items-center gap-2">
            <div className="flex -space-x-1.5">
              {['AJ', 'SK', 'MK', 'RL'].map((init) => (
                <div key={init} className={`w-6 h-6 rounded-full ${avatarColor(init)} border-2 border-white dark:border-[#18181B] flex items-center justify-center text-white text-[9px] font-bold`}>{init}</div>
              ))}
            </div>
            <div className="flex items-center gap-1 bg-[#4F46E5]/10 dark:bg-indigo-950/60 text-[#4F46E5] dark:text-indigo-400 rounded-md px-2 py-1 text-[10px] font-semibold">
              <Cpu size={10} />
              AI on
            </div>
          </div>
        </div>

        {/* Kanban columns */}
        <div className="flex gap-3 p-3 overflow-x-auto flex-1 bg-gray-50/40 dark:bg-zinc-900/20">
          {KANBAN_COLUMNS.map((col) => (
            <div key={col.label} className="flex-1 min-w-[140px] sm:min-w-[160px] flex flex-col gap-2">
              <div className="flex items-center gap-1.5 px-1">
                <div className={`w-2 h-2 rounded-full ${col.dot}`} />
                <span className="text-[11px] font-semibold text-gray-600 dark:text-zinc-400 uppercase tracking-wide">{col.label}</span>
                <span className="ml-auto text-[10px] text-gray-400 dark:text-zinc-600">{col.cards.length}</span>
              </div>
              {col.cards.map((card) => (
                <div
                  key={card.title}
                  className={`rounded-lg p-2.5 border border-gray-200/80 dark:border-zinc-700/60 bg-white dark:bg-zinc-800/80 shadow-sm hover:shadow-md transition-shadow cursor-pointer`}
                >
                  {card.ai && (
                    <div className="flex items-center gap-1 mb-1.5">
                      <Sparkles size={9} className="text-[#4F46E5]" />
                      <span className="text-[9px] font-semibold text-[#4F46E5] dark:text-indigo-400">AI Prioritized</span>
                    </div>
                  )}
                  <div className="text-[11px] font-medium text-gray-900 dark:text-white leading-snug mb-2">{card.title}</div>
                  <div className="flex items-center justify-between gap-1">
                    <span className={`text-[9px] font-semibold px-1.5 py-0.5 rounded ${TAG_COLORS[card.tag] || 'text-gray-500 bg-gray-100'}`}>{card.tag}</span>
                    <span className={`text-[9px] font-semibold px-1.5 py-0.5 rounded ${PRIORITY_COLORS[card.priority]}`}>{card.priority}</span>
                  </div>
                  <div className="flex items-center gap-1 mt-2">
                    <div className={`w-4.5 h-4.5 w-[18px] h-[18px] rounded-full ${avatarColor(card.avatar)} flex items-center justify-center text-white text-[8px] font-bold`}>{card.avatar}</div>
                  </div>
                </div>
              ))}
              {/* Add card placeholder */}
              <div className="rounded-lg border border-dashed border-gray-200 dark:border-zinc-700 px-3 py-2 text-[10px] text-gray-400 dark:text-zinc-600 text-center cursor-pointer hover:border-gray-300 dark:hover:border-zinc-600 transition-colors">
                + Add task
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  </div>
)

const Hero = () => (
  <section className="relative pt-28 pb-24 px-4 sm:px-6 overflow-hidden">
    {/* Subtle grid bg */}
    <div
      className="pointer-events-none absolute inset-0 dark:opacity-[0.03] opacity-[0.04]"
      style={{
        backgroundImage:
          'linear-gradient(to right, #6366f1 1px, transparent 1px), linear-gradient(to bottom, #6366f1 1px, transparent 1px)',
        backgroundSize: '64px 64px',
      }}
    />
    {/* Radial fade */}
    <div className="pointer-events-none absolute inset-0 bg-gradient-to-b from-transparent via-white/80 to-white dark:via-[#09090B]/80 dark:to-[#09090B]" />

    <div className="relative max-w-5xl mx-auto">
      {/* Badge */}
      <motion.div {...fadeUp(0)} className="flex justify-center mb-6">
        <div className="inline-flex items-center gap-2 border border-[#4F46E5]/30 bg-[#4F46E5]/5 text-[#4F46E5] dark:text-indigo-400 px-3.5 py-1.5 rounded-full text-xs font-semibold tracking-wide">
          <span className="w-1.5 h-1.5 bg-[#4F46E5] dark:bg-indigo-400 rounded-full animate-pulse" />
          AI-Powered Task Management — Now in Public Beta
        </div>
      </motion.div>

      {/* Headline */}
      <motion.h1
        {...fadeUp(0.07)}
        className="text-center text-4xl sm:text-5xl lg:text-6xl font-bold tracking-tight text-gray-900 dark:text-white leading-[1.1] mb-5"
      >
        The workspace where
        <br />
        <span className="text-[#4F46E5]">great teams ship faster</span>
      </motion.h1>

      {/* Subheading */}
      <motion.p
        {...fadeUp(0.14)}
        className="text-center text-lg sm:text-xl text-gray-500 dark:text-zinc-400 max-w-2xl mx-auto mb-10 leading-relaxed"
      >
        Unify task management, team chat, CRM pipeline, and AI insights in one platform — so your team stays focused on what matters.
      </motion.p>

      {/* CTAs */}
      <motion.div {...fadeUp(0.2)} className="flex flex-col sm:flex-row items-center justify-center gap-3 mb-8">
        <Link to="/register">
          <motion.button
            whileHover={{ scale: 1.02 }}
            whileTap={{ scale: 0.98 }}
            className="group flex items-center gap-2 px-6 py-3 bg-[#4F46E5] hover:bg-[#4338CA] text-white text-sm font-semibold rounded-lg shadow-md shadow-indigo-500/20 transition-all"
          >
            Start free trial
            <ArrowRight size={15} className="group-hover:translate-x-0.5 transition-transform" />
          </motion.button>
        </Link>
        <motion.button
          whileHover={{ scale: 1.02 }}
          whileTap={{ scale: 0.98 }}
          className="flex items-center gap-2 px-6 py-3 border border-gray-200 dark:border-zinc-700 text-gray-700 dark:text-zinc-300 hover:border-gray-300 dark:hover:border-zinc-600 hover:bg-gray-50 dark:hover:bg-zinc-800/60 text-sm font-medium rounded-lg transition-all"
        >
          <div className="flex items-center justify-center w-5 h-5 rounded-full bg-gray-100 dark:bg-zinc-700">
            <Play size={9} className="text-gray-600 dark:text-zinc-400 ml-0.5" />
          </div>
          Watch demo
        </motion.button>
      </motion.div>

      {/* Trust row */}
      <motion.div {...fadeUp(0.26)} className="flex flex-wrap items-center justify-center gap-x-6 gap-y-2 mb-16">
        {TRUST_ITEMS.map((item) => (
          <div key={item} className="flex items-center gap-1.5 text-sm text-gray-500 dark:text-zinc-500">
            <CheckCircle2 size={13} className="text-emerald-500" />
            {item}
          </div>
        ))}
      </motion.div>

      {/* Dashboard mockup */}
      <motion.div
        initial={{ opacity: 0, y: 32 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.7, delay: 0.32, ease: 'easeOut' }}
        className="relative"
      >
        <DashboardMockup />

        {/* Floating cards */}
        <motion.div
          animate={{ y: [0, -6, 0] }}
          transition={{ duration: 3.5, repeat: Infinity, ease: 'easeInOut' }}
          className="absolute -top-3 -right-2 sm:-right-6 bg-white dark:bg-zinc-800 border border-gray-200 dark:border-zinc-700 rounded-xl px-3 py-2 shadow-lg hidden sm:block"
        >
          <div className="flex items-center gap-2">
            <Sparkles size={13} className="text-[#4F46E5]" />
            <div>
              <div className="text-[11px] font-semibold text-gray-900 dark:text-white">AI Suggestion</div>
              <div className="text-[10px] text-gray-500 dark:text-zinc-400">Prioritize API task now</div>
            </div>
          </div>
        </motion.div>

        <motion.div
          animate={{ y: [0, 6, 0] }}
          transition={{ duration: 4, repeat: Infinity, ease: 'easeInOut', delay: 0.5 }}
          className="absolute -bottom-4 -left-2 sm:-left-6 bg-white dark:bg-zinc-800 border border-gray-200 dark:border-zinc-700 rounded-xl px-3 py-2 shadow-lg hidden sm:block"
        >
          <div className="flex items-center gap-2">
            <div className="flex -space-x-1">
              {['AJ', 'SK'].map((init) => (
                <div key={init} className={`w-5 h-5 rounded-full ${avatarColor(init)} border-2 border-white dark:border-zinc-800 flex items-center justify-center text-white text-[8px] font-bold`}>{init}</div>
              ))}
            </div>
            <div>
              <div className="text-[11px] font-semibold text-gray-900 dark:text-white">2 teammates online</div>
              <div className="flex items-center gap-1 text-[10px] text-emerald-500"><Clock size={9} /> Active now</div>
            </div>
          </div>
        </motion.div>

        <motion.div
          animate={{ y: [0, -5, 0] }}
          transition={{ duration: 3, repeat: Infinity, ease: 'easeInOut', delay: 1 }}
          className="absolute top-1/3 -right-2 sm:-right-8 bg-white dark:bg-zinc-800 border border-gray-200 dark:border-zinc-700 rounded-xl px-3 py-2 shadow-lg hidden sm:block"
        >
          <div className="flex items-center gap-2">
            <div className="w-8 h-8 rounded-full bg-emerald-50 dark:bg-emerald-950/60 flex items-center justify-center">
              <TrendingUp size={14} className="text-emerald-600 dark:text-emerald-400" />
            </div>
            <div>
              <div className="text-[11px] font-semibold text-gray-900 dark:text-white">+32% productivity</div>
              <div className="text-[10px] text-gray-400 dark:text-zinc-500">vs last sprint</div>
            </div>
          </div>
        </motion.div>
      </motion.div>

      {/* Social proof */}
      <motion.div {...fadeUp(0.5)} className="flex flex-col sm:flex-row items-center justify-center gap-6 mt-14">
        <div className="flex items-center gap-3">
          <div className="flex -space-x-2">
            {['AJ', 'SK', 'MK', 'RL', 'PR'].map((init) => (
              <div key={init} className={`w-8 h-8 rounded-full ${avatarColor(init)} border-2 border-white dark:border-[#09090B] flex items-center justify-center text-white text-[10px] font-bold`}>{init}</div>
            ))}
          </div>
          <div>
            <div className="text-sm font-semibold text-gray-900 dark:text-white">Trusted by 10,000+ teams</div>
            <div className="text-xs text-gray-400 dark:text-zinc-500">across 50+ countries</div>
          </div>
        </div>
        <div className="hidden sm:block w-px h-8 bg-gray-200 dark:bg-zinc-700" />
        <div className="flex items-center gap-1.5">
          {[1, 2, 3, 4, 5].map((i) => (
            <svg key={i} className="w-4 h-4 text-amber-400 fill-current" viewBox="0 0 20 20"><path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" /></svg>
          ))}
          <span className="text-sm font-semibold text-gray-900 dark:text-white ml-1">4.9</span>
          <span className="text-sm text-gray-400 dark:text-zinc-500">(2,400+ reviews)</span>
        </div>
      </motion.div>
    </div>
  </section>
)

export default Hero
