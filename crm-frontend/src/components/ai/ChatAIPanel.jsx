import { useMemo } from 'react'
import { useMutation } from '@tanstack/react-query'
import toast from 'react-hot-toast'
import { FiCheckSquare, FiMessageSquare, FiSend } from 'react-icons/fi'
import { aiService } from '../../services/aiService'
import AIResponseCard from './AIResponseCard'

const ChatAIPanel = ({ room, messages, onSendMessage }) => {
  const transcript = useMemo(() => messages.slice(-80).map((message) => (
    `${message.senderName || 'User'}: ${message.content}`
  )), [messages])

  const summaryMutation = useMutation({
    mutationFn: () => aiService.summarizeChat({
      chatRoomId: room.id,
      chatRoomName: room.name,
      messages: transcript,
      messageCount: transcript.length,
      context: `Room type: ${room.type}`,
    }),
  })

  const replyMutation = useMutation({
    mutationFn: () => aiService.getSmartReplies({
      message: messages[messages.length - 1]?.content || `Summarize ${room.name}`,
      context: `Generate concise smart replies and action recommendations for ${room.name}.`,
      conversationHistory: transcript.slice(-20).join('\n'),
      suggestionCount: 3,
    }),
  })

  const summary = summaryMutation.data
  const replies = replyMutation.data
  const markdown = [
    summary && `### Conversation summary\n${summary.summary}\n\n### Key points\n${(summary.keyPoints || []).map((point) => `- ${point}`).join('\n')}\n\n### Decisions\n${(summary.decisions || []).map((decision) => `- ${decision}`).join('\n')}\n\n### Action items\n${(summary.actionItems || []).map((item) => `- ${item}`).join('\n')}`,
    replies && `### Smart replies\n${(replies.suggestions || []).map((reply) => `- ${reply}`).join('\n')}\n\n### Follow-up questions\n${(replies.followUpQuestions || []).map((question) => `- ${question}`).join('\n')}\n\n### Action recommendations\n${(replies.actionRecommendations || []).map((action) => `- ${action}`).join('\n')}`,
  ].filter(Boolean).join('\n\n')

  return (
    <AIResponseCard
      title="AI Chat Intelligence"
      subtitle="Summaries, smart replies, decisions, and action items"
      loading={summaryMutation.isPending || replyMutation.isPending}
      error={summaryMutation.error?.message || replyMutation.error?.message}
      markdown={markdown || 'Choose an AI action to analyze this conversation.'}
      onRetry={() => {
        summaryMutation.mutate()
        replyMutation.mutate()
      }}
      actions={(
        <>
          <button type="button" className="btn-primary inline-flex items-center gap-2" onClick={() => summaryMutation.mutate()}>
            <FiMessageSquare />
            Summarize chat
          </button>
          <button type="button" className="btn-secondary inline-flex items-center gap-2" onClick={() => replyMutation.mutate()}>
            <FiSend />
            Smart replies
          </button>
          {(summary?.actionItems || []).slice(0, 3).map((item) => (
            <button
              key={item}
              type="button"
              className="btn-outline inline-flex items-center gap-2"
              onClick={() => {
                onSendMessage?.(`Action item captured: ${item}`)
                toast.success('Action item posted to chat')
              }}
            >
              <FiCheckSquare />
              Post action
            </button>
          ))}
        </>
      )}
    />
  )
}

export default ChatAIPanel
