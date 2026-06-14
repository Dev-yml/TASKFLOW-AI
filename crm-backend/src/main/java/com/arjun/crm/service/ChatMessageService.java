package com.arjun.crm.service;

import com.arjun.crm.dto.request.ChatMessageRequest;
import com.arjun.crm.dto.request.ChatMessageUpdateRequest;
import com.arjun.crm.dto.response.ChatMessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface ChatMessageService {
    
    ChatMessageResponse sendMessage(ChatMessageRequest request);
    
    Page<ChatMessageResponse> getMessages(Long roomId, Pageable pageable);
    
    ChatMessageResponse updateMessage(Long messageId, ChatMessageUpdateRequest request);
    
    void deleteMessage(Long messageId);

    /**
     * Upload a file, store it, and send it as a FILE/IMAGE message.
     */
    ChatMessageResponse sendFileMessage(Long chatRoomId, MultipartFile file);
}
