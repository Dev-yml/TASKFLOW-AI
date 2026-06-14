import { useSelector, useDispatch } from 'react-redux'
import { toggleTheme, setTheme } from '../store/slices/themeSlice'

export const useTheme = () => {
  const dispatch = useDispatch()
  const { isDark } = useSelector((state) => state.theme)

  const toggle = () => {
    dispatch(toggleTheme())
  }

  const setDarkMode = (value) => {
    dispatch(setTheme(value))
  }

  return {
    isDark,
    toggle,
    setDarkMode,
  }
}
