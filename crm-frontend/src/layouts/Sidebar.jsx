import { NavLink } from 'react-router-dom'
import {
  LayoutDashboard, FolderOpen, CheckSquare, Users,
  MessageSquare, BarChart3, Zap, Settings,
} from 'lucide-react'

const NAV = [
  { path: '/dashboard',  icon: LayoutDashboard, label: 'Dashboard'  },
  { path: '/workspaces', icon: FolderOpen,       label: 'Workspaces' },
  { path: '/crm',        icon: Users,            label: 'CRM Pipeline'},
  { path: '/chat',       icon: MessageSquare,    label: 'Chat'       },
  { path: '/analytics',  icon: BarChart3,        label: 'Analytics'  },
  { path: '/ai-insights',icon: Zap,              label: 'AI Insights'},
  { path: '/settings',   icon: Settings,         label: 'Settings'   },
]

const Sidebar = () => (
  <aside className="w-56 flex-shrink-0 hidden lg:flex flex-col
    bg-white dark:bg-[#161B22]
    border-r border-gray-200 dark:border-[#30363D]">

    {/* Logo */}
    <div className="flex items-center gap-2 px-4 h-14 border-b border-gray-200 dark:border-[#30363D]">
      <div className="w-6 h-6 bg-violet-600 rounded-md flex items-center justify-center flex-shrink-0">
        <Zap size={13} className="text-white" />
      </div>
      <span className="text-[14px] font-semibold text-gray-900 dark:text-[#E6EDF3] tracking-tight">
        AI CRM
      </span>
    </div>

    {/* Nav */}
    <nav className="flex-1 px-2 py-3 space-y-0.5 overflow-y-auto">
      {NAV.map(({ path, icon: Icon, label }) => (
        <NavLink
          key={path}
          to={path}
          className={({ isActive }) =>
            `flex items-center gap-2.5 px-3 py-2 rounded-md text-[13px] font-medium transition-colors duration-100 ${
              isActive
                ? 'bg-gray-100 dark:bg-[#21262D] text-gray-900 dark:text-[#E6EDF3]'
                : 'text-gray-600 dark:text-[#8B949E] hover:text-gray-900 dark:hover:text-[#E6EDF3] hover:bg-gray-100 dark:hover:bg-[#21262D]'
            }`
          }
        >
          <Icon size={15} className="flex-shrink-0" />
          {label}
        </NavLink>
      ))}
    </nav>
  </aside>
)

export default Sidebar
