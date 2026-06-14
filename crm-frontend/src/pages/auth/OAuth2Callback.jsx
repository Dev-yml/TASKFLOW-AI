import { useEffect, useRef } from 'react'
import { useNavigate, useSearchParams } from 'react-router-dom'
import { useDispatch } from 'react-redux'
import { loginSuccess } from '../../store/slices/authSlice'
import toast from 'react-hot-toast'
import Spinner from '../../components/common/Spinner'

const API_BASE = import.meta.env.VITE_API_URL || 'http://localhost:8080/api'

/**
 * Landing page for /oauth2/callback?token=JWT
 *
 * Root cause of "Failed to load profile" error:
 *   api.js reads the token from Redux store (store.getState().auth.token).
 *   At this point Redux is still null — localStorage was written but Redux
 *   has not been updated yet. So the /api/users/me request fires with no
 *   Authorization header → anonymous context → 403 → profile load fails.
 *
 * Fix:
 *   Use a raw fetch() with the token directly in the header,
 *   bypassing the Axios instance entirely.
 *   Only after the profile is successfully loaded do we dispatch loginSuccess,
 *   which updates Redux AND localStorage atomically.
 */
const OAuth2Callback = () => {
  const [params] = useSearchParams()
  const dispatch = useDispatch()
  const navigate = useNavigate()
  const handled = useRef(false)

  useEffect(() => {
    if (handled.current) return
    handled.current = true

    const token = params.get('token')
    const error = params.get('error')

    if (error) {
      toast.error('Social login failed. Please try again.')
      navigate('/login', { replace: true })
      return
    }

    if (!token) {
      toast.error('No authentication token received.')
      navigate('/login', { replace: true })
      return
    }

    // Fetch the user profile using the token directly in the header.
    // We deliberately bypass api.js / Axios to avoid the Redux timing issue.
    fetch(`${API_BASE}/users/me`, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json',
      },
    })
      .then(async (res) => {
        if (!res.ok) {
          const body = await res.json().catch(() => ({}))
          throw new Error(body?.message || `HTTP ${res.status}`)
        }
        return res.json()
      })
      .then((body) => {
        // Backend wraps in { success, data, ... }
        const user = body?.data ?? body

        if (!user?.id) throw new Error('Invalid user data received')

        // Now update Redux + localStorage atomically
        dispatch(loginSuccess({ token, user }))
        toast.success(`Welcome, ${user.displayName || user.fullName || 'User'}!`)
        navigate('/dashboard', { replace: true })
      })
      .catch((err) => {
        console.error('OAuth2 profile load failed:', err)
        toast.error('Failed to load your profile. Please try signing in again.')
        navigate('/login', { replace: true })
      })
  }, []) // eslint-disable-line react-hooks/exhaustive-deps

  return (
    <div className="flex h-screen items-center justify-center bg-gray-50 dark:bg-[#0D1117]">
      <div className="text-center space-y-4">
        <Spinner size="lg" />
        <p className="text-sm text-gray-500 dark:text-[#8B949E]">
          Completing sign-in, please wait…
        </p>
      </div>
    </div>
  )
}

export default OAuth2Callback
