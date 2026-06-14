import { useEffect, useState } from 'react'

export const useStreamingText = (text = '', speed = 12) => {
  const [streamedText, setStreamedText] = useState('')

  useEffect(() => {
    setStreamedText('')
    if (!text) return undefined

    let index = 0
    const interval = setInterval(() => {
      index += 1
      setStreamedText(text.slice(0, index))
      if (index >= text.length) clearInterval(interval)
    }, speed)

    return () => clearInterval(interval)
  }, [speed, text])

  return streamedText
}
