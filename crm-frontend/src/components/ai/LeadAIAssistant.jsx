import { useMutation } from '@tanstack/react-query'
import { FiArrowRight, FiPhoneCall, FiTrendingUp } from 'react-icons/fi'
import { aiService } from '../../services/aiService'
import AIResponseCard from './AIResponseCard'

const LeadAIAssistant = ({ lead, activities = [] }) => {
  const mutation = useMutation({
    mutationFn: () => aiService.getSmartReplies({
      message: `Analyze this CRM lead and produce sales guidance for ${lead.name} at ${lead.company || 'unknown company'}.`,
      context: [
        `Stage: ${lead.status}`,
        `Priority: ${lead.priority}`,
        `Deal value: ${lead.dealValue}`,
        `Expected close date: ${lead.expectedCloseDate || 'not set'}`,
        `Notes: ${lead.notes || 'none'}`,
        `Recent activity: ${activities.slice(0, 6).map((item) => item.description).join('; ')}`,
      ].join('\n'),
      conversationHistory: '',
      suggestionCount: 5,
    }),
  })

  const data = mutation.data
  const markdown = data ? [
    `### AI lead score and deal risk\n${(data.suggestions || []).map((item) => `- ${item}`).join('\n')}`,
    `### Follow-up suggestions\n${(data.followUpQuestions || []).map((item) => `- ${item}`).join('\n')}`,
    `### Suggested next actions\n${(data.actionRecommendations || []).map((item) => `- ${item}`).join('\n')}`,
  ].join('\n\n') : 'Generate AI sales insights for this lead to reveal risk, follow-up angles, and next actions.'

  return (
    <AIResponseCard
      title="AI CRM Assistant"
      subtitle="Lead scoring, deal risk, follow-up suggestions, and customer summary"
      loading={mutation.isPending}
      error={mutation.error?.message}
      markdown={markdown}
      onRetry={() => mutation.mutate()}
      actions={(
        <>
          <button type="button" className="btn-primary inline-flex items-center gap-2" onClick={() => mutation.mutate()}>
            <FiTrendingUp />
            Analyze lead
          </button>
          {(data?.actionRecommendations || []).slice(0, 2).map((action) => (
            <span key={action} className="inline-flex items-center gap-2 rounded-full bg-orange-50 px-3 py-2 text-xs font-bold text-orange-700 dark:bg-orange-950 dark:text-orange-300">
              <FiArrowRight />
              {action}
            </span>
          ))}
          {data?.followUpQuestions?.[0] && (
            <span className="inline-flex items-center gap-2 rounded-full bg-green-50 px-3 py-2 text-xs font-bold text-green-700 dark:bg-green-950 dark:text-green-300">
              <FiPhoneCall />
              Follow up ready
            </span>
          )}
        </>
      )}
    />
  )
}

export default LeadAIAssistant
