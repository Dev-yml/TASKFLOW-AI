import { z } from 'zod'

export const createLeadSchema = z.object({
  name: z.string().min(1, 'Name is required'),
  email: z.string().email('Invalid email address'),
  phone: z.string().optional(),
  company: z.string().optional(),
  position: z.string().optional(),
  dealValue: z.number().min(0, 'Deal value must be positive').optional(),
  status: z.enum(['LEAD', 'QUALIFIED', 'PROPOSAL', 'NEGOTIATION', 'WON', 'LOST']),
  priority: z.enum(['LOW', 'MEDIUM', 'HIGH', 'URGENT']),
  assignedToId: z.number().optional().nullable(),
  workspaceId: z.number(),
  tags: z.array(z.string()).optional(),
  notes: z.string().optional(),
  expectedCloseDate: z.string().optional().nullable(),
})

export const updateLeadSchema = z.object({
  name: z.string().min(1, 'Name is required').optional(),
  email: z.string().email('Invalid email address').optional(),
  phone: z.string().optional(),
  company: z.string().optional(),
  position: z.string().optional(),
  dealValue: z.number().min(0, 'Deal value must be positive').optional(),
  status: z.enum(['LEAD', 'QUALIFIED', 'PROPOSAL', 'NEGOTIATION', 'WON', 'LOST']).optional(),
  priority: z.enum(['LOW', 'MEDIUM', 'HIGH', 'URGENT']).optional(),
  assignedToId: z.number().optional().nullable(),
  tags: z.array(z.string()).optional(),
  notes: z.string().optional(),
  expectedCloseDate: z.string().optional().nullable(),
})

export const updateLeadStatusSchema = z.object({
  status: z.enum(['LEAD', 'QUALIFIED', 'PROPOSAL', 'NEGOTIATION', 'WON', 'LOST']),
  notes: z.string().optional(),
})
