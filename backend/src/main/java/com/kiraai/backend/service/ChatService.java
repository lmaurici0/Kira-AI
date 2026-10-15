package com.kiraai.backend.service;

import com.kiraai.backend.dto.ChatMessageResponse;
import com.kiraai.backend.model.Conversation;
import com.kiraai.backend.model.Message;
import com.kiraai.backend.repository.ConversationRepository;
import com.kiraai.backend.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.function.Consumer;

@Service
public class ChatService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final GeminiService geminiService;

    public ChatService(
            ConversationRepository conversationRepository,
            MessageRepository messageRepository,
            GeminiService geminiService) {

        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.geminiService = geminiService;
    }

    public ChatMessageResponse sendMessage(
            String userId,
            String conversationId,
            String content) {

        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Conversa nao encontrada"));

        if (!conversation.getUserId().equals(userId)) {
            throw new SecurityException(
                    "Essa conversa nao pertence a este usuario");
        }

        // Mensagem do usuário
        Message userMessage = new Message();
        userMessage.setConversationId(conversationId);
        userMessage.setRole("user");
        userMessage.setContent(content);
        userMessage.setCreatedAt(Instant.now());

        messageRepository.save(userMessage);

        // Resposta do Gemini
        String aiReply = geminiService.generateReply(
                content,
                conversation.getSystemInstruction(),
                conversation.getTemperature()
        );

        // Mensagem da IA
        Message aiMessage = new Message();
        aiMessage.setConversationId(conversationId);
        aiMessage.setRole("assistant");
        aiMessage.setContent(aiReply);
        aiMessage.setCreatedAt(Instant.now());

        messageRepository.save(aiMessage);

        conversation.setUpdatedAt(Instant.now());
        conversationRepository.save(conversation);

        return new ChatMessageResponse(
                conversationId,
                "assistant",
                aiReply,
                null
        );
    }

    public void sendMessageStream(
            String userId,
            String conversationId,
            String content,
            Consumer<String> onChunk,
            Runnable onComplete) {

        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Conversa nao encontrada"));

        if (!conversation.getUserId().equals(userId)) {
            throw new SecurityException(
                    "Conversa nao pertence a este usuario");
        }

        // Salva a mensagem do usuário
        Message userMessage = new Message();
        userMessage.setConversationId(conversationId);
        userMessage.setRole("user");
        userMessage.setContent(content);
        userMessage.setCreatedAt(Instant.now());

        messageRepository.save(userMessage);

        StringBuilder fullResponse = new StringBuilder();

        geminiService.generateReplyStream(
                content,
                conversation.getSystemInstruction(),
                conversation.getTemperature(),

                chunk -> {
                    if (chunk != null && !chunk.isEmpty()) {
                        fullResponse.append(chunk);
                        onChunk.accept(chunk);
                    }
                },

                () -> {
                    // Salva a resposta completa da IA depois
                    // que todos os chunks foram recebidos.
                    Message aiMessage = new Message();
                    aiMessage.setConversationId(conversationId);
                    aiMessage.setRole("assistant");
                    aiMessage.setContent(fullResponse.toString());
                    aiMessage.setCreatedAt(Instant.now());

                    messageRepository.save(aiMessage);

                    conversation.setUpdatedAt(Instant.now());
                    conversationRepository.save(conversation);

                    onComplete.run();
                }
        );
    }

    public Conversation createConversation(String userId) {

        Conversation conversation = new Conversation();

        conversation.setUserId(userId);
        conversation.setTitle("Nova conversa");
        conversation.setTemperature(0.7);
        conversation.setCreatedAt(Instant.now());
        conversation.setUpdatedAt(Instant.now());

        return conversationRepository.save(conversation);
    }

    public List<Conversation> listConversations(String userId) {
        return conversationRepository
                .findByUserIdOrderByUpdatedAtDesc(userId);
    }

    public List<Message> getMessages(
            String userId,
            String conversationId) {

        Conversation conversation = conversationRepository
                .findById(conversationId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Conversa nao encontrada"));

        if (!conversation.getUserId().equals(userId)) {
            throw new SecurityException(
                    "Conversa nao pertence a este usuario");
        }

        return messageRepository
                .findByConversationIdOrderByCreatedAtAsc(conversationId);
    }
}