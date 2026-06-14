package com.arjun.crm.entity;

import com.arjun.crm.enums.MessageType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "chat_messages", indexes = {
        @Index(name = "idx_chat_room_created", columnList = "chat_room_id,created_at"),
        @Index(name = "idx_sender_id", columnList = "sender_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type", nullable = false, length = 20)
    @Builder.Default
    private MessageType messageType = MessageType.TEXT;

    @Column(name = "is_edited", nullable = false)
    @Builder.Default
    private Boolean isEdited = false;

    @Column(name = "is_deleted", nullable = false)
    @Builder.Default
    private Boolean isDeleted = false;

    /** Server-relative download URL for FILE / IMAGE messages. Null for TEXT. */
    @Column(name = "attachment_url", length = 1000)
    private String attachmentUrl;

    /** Original filename shown to the receiver. */
    @Column(name = "attachment_name", length = 500)
    private String attachmentName;

    /** MIME type, e.g. application/pdf, image/png. */
    @Column(name = "attachment_type", length = 200)
    private String attachmentType;

    /** File size in bytes. */
    @Column(name = "attachment_size")
    private Long attachmentSize;

    /**
     * Stored as UTC Instant → serialized as ISO-8601 with 'Z' suffix.
     * e.g. "2026-06-04T13:00:00Z"
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
