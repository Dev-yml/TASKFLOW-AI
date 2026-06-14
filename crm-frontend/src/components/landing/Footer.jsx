import { Link } from 'react-router-dom'
import { ExternalLink, Mail, Zap, Heart, Globe, MessageCircle } from 'lucide-react'

const NAV = {
  Product: [
    { label: 'Features', href: '#features' },
    { label: 'Pricing', href: '#pricing' },
    { label: 'AI Features', href: '#ai' },
    { label: 'Changelog', href: '#' },
    { label: 'Roadmap', href: '#' },
  ],
  Company: [
    { label: 'About', href: '#' },
    { label: 'Blog', href: '#' },
    { label: 'Careers', href: '#' },
    { label: 'Press Kit', href: '#' },
    { label: 'Contact', href: '#' },
  ],
  Resources: [
    { label: 'Documentation', href: '#' },
    { label: 'API Reference', href: '#' },
    { label: 'Help Centre', href: '#' },
    { label: 'Community', href: '#' },
    { label: 'Status', href: '#' },
  ],
}

const SOCIALS = [
  { icon: Globe, href: '#', label: 'Website' },
  { icon: MessageCircle, href: '#', label: 'Community' },
  { icon: Mail, href: 'mailto:hello@taskflow.ai', label: 'Email' },
  { icon: ExternalLink, href: '#', label: 'Blog' },
]

const Footer = () => {
  const year = new Date().getFullYear()

  return (
    <footer className="border-t border-gray-200 dark:border-zinc-800 bg-white dark:bg-[#09090B]">
      <div className="max-w-5xl mx-auto px-4 sm:px-6 py-16">

        {/* Top */}
        <div className="grid grid-cols-2 md:grid-cols-5 gap-8 mb-12">

          {/* Brand */}
          <div className="col-span-2 md:col-span-2">
            <Link to="/" className="flex items-center gap-2 mb-4 group w-fit">
              <div className="w-7 h-7 bg-[#4F46E5] rounded-md flex items-center justify-center group-hover:bg-[#4338CA] transition-colors">
                <Zap size={14} className="text-white" />
              </div>
              <span className="text-[15px] font-semibold text-gray-900 dark:text-white tracking-tight">
                TaskFlow<span className="text-[#4F46E5]"> AI</span>
              </span>
            </Link>
            <p className="text-[13px] text-gray-500 dark:text-zinc-500 leading-relaxed mb-5 max-w-xs">
              AI-powered collaboration platform for modern teams. Task management, chat, CRM, and insights — unified.
            </p>
            <div className="flex items-center gap-3">
              {SOCIALS.map(({ icon: Icon, href, label }) => (
                <a
                  key={label}
                  href={href}
                  target="_blank"
                  rel="noopener noreferrer"
                  aria-label={label}
                  className="w-8 h-8 rounded-md border border-gray-200 dark:border-zinc-800 flex items-center justify-center text-gray-400 dark:text-zinc-500 hover:text-gray-700 dark:hover:text-zinc-300 hover:border-gray-300 dark:hover:border-zinc-700 transition-colors"
                >
                  <Icon size={14} />
                </a>
              ))}
            </div>
          </div>

          {/* Nav columns */}
          {Object.entries(NAV).map(([section, links]) => (
            <div key={section}>
              <div className="text-[11px] font-bold uppercase tracking-widest text-gray-400 dark:text-zinc-600 mb-4">{section}</div>
              <ul className="space-y-2.5">
                {links.map(({ label, href }) => (
                  <li key={label}>
                    <a
                      href={href}
                      className="text-[13px] text-gray-500 dark:text-zinc-500 hover:text-gray-800 dark:hover:text-zinc-300 transition-colors"
                    >
                      {label}
                    </a>
                  </li>
                ))}
              </ul>
            </div>
          ))}
        </div>

        {/* CTA banner */}
        <div className="rounded-xl border border-[#4F46E5]/20 bg-[#4F46E5]/4 dark:bg-indigo-950/20 px-6 py-5 flex flex-col sm:flex-row items-center justify-between gap-4 mb-12">
          <div>
            <div className="text-[14px] font-semibold text-gray-900 dark:text-white mb-0.5">Start shipping faster today</div>
            <div className="text-[12.5px] text-gray-500 dark:text-zinc-400">No credit card required · 14-day free trial · Cancel anytime</div>
          </div>
          <Link to="/register">
            <button className="flex items-center gap-2 px-5 py-2.5 bg-[#4F46E5] hover:bg-[#4338CA] text-white text-sm font-medium rounded-lg transition-colors shadow-sm flex-shrink-0">
              Get started free
            </button>
          </Link>
        </div>

        {/* Bottom bar */}
        <div className="flex flex-col sm:flex-row items-center justify-between gap-4 pt-6 border-t border-gray-100 dark:border-zinc-800/60">
          <div className="text-[12px] text-gray-400 dark:text-zinc-600">
            © {year} TaskFlow AI, Inc. All rights reserved.
          </div>
          <div className="flex items-center gap-5">
            {['Privacy Policy', 'Terms of Service', 'Cookie Policy'].map((item) => (
              <a key={item} href="#" className="text-[12px] text-gray-400 dark:text-zinc-600 hover:text-gray-600 dark:hover:text-zinc-400 transition-colors">
                {item}
              </a>
            ))}
          </div>
          <div className="flex items-center gap-1 text-[12px] text-gray-400 dark:text-zinc-600">
            Made with <Heart size={12} className="text-rose-400 fill-current mx-0.5" /> by the TaskFlow team
          </div>
        </div>
      </div>
    </footer>
  )
}

export default Footer
