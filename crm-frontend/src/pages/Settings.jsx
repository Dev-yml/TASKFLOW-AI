import { useRef, useState } from 'react'
import { useDispatch } from 'react-redux'
import { useMutation, useQueryClient } from '@tanstack/react-query'
import toast from 'react-hot-toast'
import {
  FiUser, FiLock, FiBell, FiSun, FiUpload, FiTrash2, FiSave,
} from 'react-icons/fi'
import { authService } from '../services/authService'
import { updateUser } from '../store/slices/authSlice'
import { useAuth } from '../hooks/useAuth'
import UserAvatar from '../components/common/UserAvatar'
import { useTheme } from '../hooks/useTheme'

/* ─── Tabs ────────────────────────────────────────────────────────────────── */
const TABS = [
  { id: 'profile',       label: 'Profile',       icon: FiUser  },
  { id: 'appearance',    label: 'Appearance',     icon: FiSun   },
  { id: 'notifications', label: 'Notifications',  icon: FiBell  },
  { id: 'security',      label: 'Security',       icon: FiLock  },
]

/* ─── Shared field components ─────────────────────────────────────────────── */
const Field = ({ label, hint, children }) => (
  <div>
    <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1.5">{label}</label>
    {children}
    {hint && <p className="mt-1 text-xs text-gray-400 dark:text-gray-600">{hint}</p>}
  </div>
)

const Input = ({ className = '', ...props }) => (
  <input
    className={`w-full rounded-lg border border-gray-200 dark:border-gray-600 bg-white dark:bg-gray-700
      px-3.5 py-2.5 text-sm text-gray-900 dark:text-white placeholder-gray-400 dark:placeholder-gray-500
      focus:border-[#4F46E5] focus:outline-none focus:ring-2 focus:ring-[#4F46E5]/20
      disabled:cursor-not-allowed disabled:bg-gray-50 dark:disabled:bg-gray-800 disabled:text-gray-400
      transition-shadow ${className}`}
    {...props}
  />
)

const Textarea = ({ className = '', ...props }) => (
  <textarea
    rows={4}
    className={`w-full rounded-lg border border-gray-200 dark:border-gray-600 bg-white dark:bg-gray-700
      px-3.5 py-2.5 text-sm text-gray-900 dark:text-white placeholder-gray-400 dark:placeholder-gray-500
      focus:border-[#4F46E5] focus:outline-none focus:ring-2 focus:ring-[#4F46E5]/20
      transition-shadow resize-none ${className}`}
    {...props}
  />
)

const SaveBtn = ({ loading, label = 'Save changes' }) => (
  <button
    type="submit"
    disabled={loading}
    className="flex items-center gap-2 px-5 py-2.5 bg-[#4F46E5] hover:bg-[#4338CA]
      text-white text-sm font-medium rounded-lg transition-colors disabled:opacity-50 shadow-sm"
  >
    <FiSave size={14} />
    {loading ? 'Saving…' : label}
  </button>
)

/* ─── Profile tab ─────────────────────────────────────────────────────────── */
const ProfileTab = ({ user }) => {
  const dispatch = useDispatch()
  const queryClient = useQueryClient()
  const fileRef = useRef(null)
  const [uploading, setUploading] = useState(false)

  const [form, setForm] = useState({
    fullName:          user?.fullName          ?? '',
    firstName:         user?.firstName         ?? '',
    lastName:          user?.lastName          ?? '',
    displayName:       user?.displayName       ?? '',
    bio:               user?.bio               ?? '',
    designation:       user?.designation       ?? '',
    department:        user?.department        ?? '',
    phoneNumber:       user?.phoneNumber       ?? '',
    location:          user?.location          ?? '',
    website:           user?.website           ?? '',
    linkedinUrl:       user?.linkedinUrl       ?? '',
    githubUrl:         user?.githubUrl         ?? '',
    timezone:          user?.timezone          ?? '',
    preferredLanguage: user?.preferredLanguage ?? '',
  })

  const set = (k) => (e) => setForm((p) => ({ ...p, [k]: e.target.value }))

  const updateMutation = useMutation({
    mutationFn: (data) => authService.updateProfile(data),
    onSuccess: (res) => {
      const updated = res?.data ?? res
      dispatch(updateUser(updated))
      queryClient.invalidateQueries(['profile'])
      toast.success('Profile saved')
    },
    onError: (e) => toast.error(e?.response?.data?.message || 'Failed to save'),
  })

  const avatarMutation = useMutation({
    mutationFn: (url) => authService.updateAvatar(url),
    onSuccess: (res) => {
      const updated = res?.data ?? res
      dispatch(updateUser(updated))
      queryClient.invalidateQueries(['profile'])
      toast.success('Photo updated')
    },
    onError: () => toast.error('Failed to update photo'),
  })

  const removeAvatarMutation = useMutation({
    mutationFn: () => authService.removeAvatar(),
    onSuccess: (res) => {
      const updated = res?.data ?? res
      dispatch(updateUser(updated))
      queryClient.invalidateQueries(['profile'])
      toast.success('Photo removed')
    },
    onError: () => toast.error('Failed to remove photo'),
  })

  const handleFileChange = async (e) => {
    const file = e.target.files?.[0]
    if (!file) return
    if (file.size > 2 * 1024 * 1024) { toast.error('Max file size is 2 MB'); return }
    try {
      setUploading(true)
      const dataUrl = await authService.uploadImageToCloudinary(file)
      avatarMutation.mutate(dataUrl)
    } catch (err) {
      toast.error(err.message || 'Upload failed')
    } finally {
      setUploading(false)
      e.target.value = ''
    }
  }

  const previewUser = { ...user, ...form }

  return (
    <form
      onSubmit={(e) => { e.preventDefault(); updateMutation.mutate(form) }}
      className="space-y-8"
    >
      {/* Avatar section */}
      <div className="rounded-xl border border-gray-200 dark:border-gray-700 bg-white dark:bg-gray-800 p-6">
        <h3 className="text-sm font-semibold text-gray-900 dark:text-white mb-4">Profile photo</h3>
        <div className="flex flex-col sm:flex-row items-start sm:items-center gap-5">
          <UserAvatar user={previewUser} size="xl" />
          <div className="space-y-2">
            <input ref={fileRef} type="file" accept="image/*" className="hidden" onChange={handleFileChange} />
            <button
              type="button"
              onClick={() => fileRef.current?.click()}
              disabled={uploading || avatarMutation.isPending}
              className="flex items-center gap-2 px-4 py-2 border border-gray-200 dark:border-gray-700
                text-sm font-medium text-gray-700 dark:text-gray-300 rounded-lg
                hover:bg-gray-50 dark:hover:bg-gray-700 transition-colors disabled:opacity-50"
            >
              <FiUpload size={14} />
              {uploading || avatarMutation.isPending ? 'Uploading…' : user?.profileImageUrl ? 'Change photo' : 'Upload photo'}
            </button>
            {user?.profileImageUrl && (
              <button
                type="button"
                onClick={() => removeAvatarMutation.mutate()}
                disabled={removeAvatarMutation.isPending}
                className="flex items-center gap-2 px-4 py-2 text-sm font-medium text-red-600 dark:text-red-400
                  hover:bg-red-50 dark:hover:bg-red-900/20 rounded-lg transition-colors disabled:opacity-50"
              >
                <FiTrash2 size={14} />
                Remove photo
              </button>
            )}
            <p className="text-xs text-gray-400 dark:text-gray-500">JPG, PNG or GIF · Max 2 MB</p>
          </div>
        </div>
      </div>

      {/* Basic info */}
      <div className="rounded-xl border border-gray-200 dark:border-gray-700 bg-white dark:bg-gray-800 p-6 space-y-5">
        <h3 className="text-sm font-semibold text-gray-900 dark:text-white">Basic information</h3>

        <Field label="Email" hint="Email address cannot be changed.">
          <Input type="email" value={user?.email || ''} disabled />
        </Field>

        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
          <Field label="First name">
            <Input value={form.firstName} onChange={set('firstName')} placeholder="First name" />
          </Field>
          <Field label="Last name">
            <Input value={form.lastName} onChange={set('lastName')} placeholder="Last name" />
          </Field>
        </div>

        <Field label="Full name (display in app)" hint="Used wherever your name appears.">
          <Input value={form.fullName} onChange={set('fullName')} placeholder="Full name" required />
        </Field>

        <Field label="Display name" hint="Short name or handle. Defaults to full name if empty.">
          <Input value={form.displayName} onChange={set('displayName')} placeholder="e.g. arjun or @arjun" />
        </Field>

        <Field label="Bio">
          <Textarea value={form.bio} onChange={set('bio')} placeholder="Tell your team a bit about yourself…" />
        </Field>
      </div>

      {/* Work */}
      <div className="rounded-xl border border-gray-200 dark:border-gray-700 bg-white dark:bg-gray-800 p-6 space-y-5">
        <h3 className="text-sm font-semibold text-gray-900 dark:text-white">Work</h3>
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
          <Field label="Job title / Designation">
            <Input value={form.designation} onChange={set('designation')} placeholder="e.g. Senior Engineer" />
          </Field>
          <Field label="Department">
            <Input value={form.department} onChange={set('department')} placeholder="e.g. Engineering" />
          </Field>
        </div>
      </div>

      {/* Contact */}
      <div className="rounded-xl border border-gray-200 dark:border-gray-700 bg-white dark:bg-gray-800 p-6 space-y-5">
        <h3 className="text-sm font-semibold text-gray-900 dark:text-white">Contact &amp; links</h3>
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
          <Field label="Phone number">
            <Input value={form.phoneNumber} onChange={set('phoneNumber')} placeholder="+1 (555) 000-0000" />
          </Field>
          <Field label="Location">
            <Input value={form.location} onChange={set('location')} placeholder="e.g. Mumbai, India" />
          </Field>
        </div>
        <Field label="Website">
          <Input value={form.website} onChange={set('website')} placeholder="https://yoursite.com" />
        </Field>
        <Field label="LinkedIn URL">
          <Input value={form.linkedinUrl} onChange={set('linkedinUrl')} placeholder="https://linkedin.com/in/username" />
        </Field>
        <Field label="GitHub URL">
          <Input value={form.githubUrl} onChange={set('githubUrl')} placeholder="https://github.com/username" />
        </Field>
      </div>

      {/* Preferences */}
      <div className="rounded-xl border border-gray-200 dark:border-gray-700 bg-white dark:bg-gray-800 p-6 space-y-5">
        <h3 className="text-sm font-semibold text-gray-900 dark:text-white">Preferences</h3>
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
          <Field label="Timezone">
            <Input value={form.timezone} onChange={set('timezone')} placeholder="e.g. Asia/Kolkata" />
          </Field>
          <Field label="Preferred language">
            <Input value={form.preferredLanguage} onChange={set('preferredLanguage')} placeholder="e.g. English" />
          </Field>
        </div>
      </div>

      <div className="flex justify-end">
        <SaveBtn loading={updateMutation.isPending} />
      </div>
    </form>
  )
}

/* ─── Appearance tab ──────────────────────────────────────────────────────── */
const AppearanceTab = () => {
  const { isDark, toggle } = useTheme()
  return (
    <div className="rounded-xl border border-gray-200 dark:border-gray-700 bg-white dark:bg-gray-800 p-6 space-y-5">
      <h3 className="text-sm font-semibold text-gray-900 dark:text-white">Theme</h3>
      <div className="grid grid-cols-2 gap-3">
        {[
          { label: 'Light', dark: false },
          { label: 'Dark',  dark: true  },
        ].map(({ label, dark }) => (
          <button
            key={label}
            type="button"
            onClick={() => { if (isDark !== dark) toggle() }}
            className={`rounded-xl border-2 p-4 text-sm font-medium transition-all
              ${isDark === dark
                ? 'border-[#4F46E5] text-[#4F46E5] dark:text-indigo-400 bg-[#4F46E5]/5 dark:bg-indigo-950/30'
                : 'border-gray-200 dark:border-gray-700 text-gray-600 dark:text-gray-400 hover:border-gray-300 dark:hover:border-gray-600'
              }`}
          >
            {label} mode
          </button>
        ))}
      </div>
    </div>
  )
}

/* ─── Notifications tab ───────────────────────────────────────────────────── */
const NotificationsTab = () => {
  const [prefs, setPrefs] = useState({
    taskAssigned:   true,
    taskCompleted:  true,
    mentions:       true,
    chatMessages:   false,
    weeklyDigest:   true,
  })
  const toggle = (k) => setPrefs((p) => ({ ...p, [k]: !p[k] }))

  const items = [
    { key: 'taskAssigned',  label: 'Task assigned to me',      desc: 'When a task is assigned or reassigned to you' },
    { key: 'taskCompleted', label: 'Task completed',           desc: 'When someone completes a task in your projects' },
    { key: 'mentions',      label: 'Mentions & comments',      desc: 'When someone @mentions you in a comment' },
    { key: 'chatMessages',  label: 'Chat messages',            desc: 'Real-time chat notifications' },
    { key: 'weeklyDigest',  label: 'Weekly activity digest',   desc: 'Summary of your team activity every Monday' },
  ]

  return (
    <div className="rounded-xl border border-gray-200 dark:border-gray-700 bg-white dark:bg-gray-800 p-6 divide-y divide-gray-100 dark:divide-gray-700">
      {items.map(({ key, label, desc }) => (
        <div key={key} className="flex items-center justify-between py-4 first:pt-0 last:pb-0">
          <div>
            <p className="text-sm font-medium text-gray-900 dark:text-white">{label}</p>
            <p className="text-xs text-gray-500 dark:text-gray-400 mt-0.5">{desc}</p>
          </div>
          <button
            type="button"
            onClick={() => toggle(key)}
            className={`relative inline-flex h-5 w-9 flex-shrink-0 rounded-full border-2 border-transparent
              transition-colors focus:outline-none
              ${prefs[key] ? 'bg-[#4F46E5]' : 'bg-gray-200 dark:bg-gray-600'}`}
          >
            <span className={`pointer-events-none inline-block h-4 w-4 rounded-full bg-white shadow
              transform transition-transform ${prefs[key] ? 'translate-x-4' : 'translate-x-0'}`} />
          </button>
        </div>
      ))}
    </div>
  )
}

/* ─── Security tab ────────────────────────────────────────────────────────── */
const SecurityTab = () => {
  const [form, setForm] = useState({ currentPassword: '', newPassword: '', confirmPassword: '' })
  const set = (k) => (e) => setForm((p) => ({ ...p, [k]: e.target.value }))

  const mutation = useMutation({
    mutationFn: (data) => authService.changePassword({ currentPassword: data.currentPassword, newPassword: data.newPassword }),
    onSuccess: () => {
      toast.success('Password changed')
      setForm({ currentPassword: '', newPassword: '', confirmPassword: '' })
    },
    onError: (e) => toast.error(e?.response?.data?.message || 'Failed to change password'),
  })

  const handleSubmit = (e) => {
    e.preventDefault()
    if (form.newPassword !== form.confirmPassword) { toast.error('Passwords do not match'); return }
    if (form.newPassword.length < 8) { toast.error('Minimum 8 characters'); return }
    mutation.mutate(form)
  }

  return (
    <form onSubmit={handleSubmit} className="space-y-6">
      <div className="rounded-xl border border-gray-200 dark:border-gray-700 bg-white dark:bg-gray-800 p-6 space-y-5">
        <h3 className="text-sm font-semibold text-gray-900 dark:text-white">Change password</h3>
        <Field label="Current password">
          <Input type="password" value={form.currentPassword} onChange={set('currentPassword')} placeholder="Enter current password" required />
        </Field>
        <Field label="New password" hint="Minimum 8 characters.">
          <Input type="password" value={form.newPassword} onChange={set('newPassword')} placeholder="New password" required />
        </Field>
        <Field label="Confirm new password">
          <Input type="password" value={form.confirmPassword} onChange={set('confirmPassword')} placeholder="Confirm new password" required />
        </Field>
      </div>
      <div className="flex justify-end">
        <SaveBtn loading={mutation.isPending} label="Update password" />
      </div>
    </form>
  )
}

/* ─── Main Settings page ──────────────────────────────────────────────────── */
const Settings = () => {
  const { user } = useAuth()
  const [tab, setTab] = useState('profile')

  const content = {
    profile:       <ProfileTab user={user} />,
    appearance:    <AppearanceTab />,
    notifications: <NotificationsTab />,
    security:      <SecurityTab />,
  }

  return (
    <div className="max-w-4xl mx-auto">
      <h1 className="text-2xl font-bold text-gray-900 dark:text-white mb-6">Settings</h1>

      <div className="flex flex-col sm:flex-row gap-6">
        {/* Sidebar nav */}
        <nav className="sm:w-44 flex-shrink-0">
          <ul className="flex sm:flex-col gap-1 overflow-x-auto pb-1 sm:pb-0">
            {TABS.map(({ id, label, icon: Icon }) => (
              <li key={id}>
                <button
                  type="button"
                  onClick={() => setTab(id)}
                  className={`flex items-center gap-2.5 w-full px-3.5 py-2.5 rounded-lg text-sm font-medium
                    whitespace-nowrap transition-colors
                    ${tab === id
                      ? 'bg-[#4F46E5]/10 dark:bg-indigo-950/50 text-[#4F46E5] dark:text-indigo-400'
                      : 'text-gray-600 dark:text-gray-400 hover:bg-gray-100 dark:hover:bg-gray-800'
                    }`}
                >
                  <Icon size={15} className="flex-shrink-0" />
                  {label}
                </button>
              </li>
            ))}
          </ul>
        </nav>

        {/* Tab content */}
        <div className="flex-1 min-w-0">
          {content[tab]}
        </div>
      </div>
    </div>
  )
}

export default Settings
