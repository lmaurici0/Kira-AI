package com.kiraai.backend.service;

import	com.kiraai.backend.dto.ChatMessageResponse;
import	com.kiraai.backend.model.Conversation;
import	com.kiraai.backend.model.Message;
import	com.kiraai.backend.repository.ConversationRepository;
import	com.kiraai.backend.repository.MessageRepository;
import	org.springframework.stereotype.Service;
import	java.time.Instant;
import java.util.List;

@Service
public	class	ChatService	{
    private	final	ConversationRepository	conversationRepository;
    private	final	MessageRepository	messageRepository;
    private	final	GeminiService	geminiService;
    public	ChatService(ConversationRepository	conversationRepository,
                          MessageRepository	messageRepository,
                          GeminiService	geminiService)	{
        this.conversationRepository	=	conversationRepository;
        this.messageRepository	=	messageRepository;
        this.geminiService	=	geminiService;
    }
    public	ChatMessageResponse	sendMessage(String	userId,	String	conversationId,	String	content)	{
        Conversation	conversation	=	conversationRepository.findById(conversationId)
                .orElseThrow(()	->	new	IllegalArgumentException("Conversa	nao	encontrada"));
        if	(!conversation.getUserId().equals(userId))	{
            throw	new	SecurityException("Essa	conversa	nao	pertence	a	este	usuario");
        }
        Message	userMessage	=	new	Message();
        userMessage.setConversationId(conversationId);
        userMessage.setRole("user");
        userMessage.setContent(content);
        userMessage.setCreatedAt(Instant.now());
        messageRepository.save(userMessage);
        String	aiReply	=	geminiService.generateReply(
                content,	conversation.getSystemInstruction(),	conversation.getTemperature());
        Message	aiMessage	=	new	Message();
        aiMessage.setConversationId(conversationId);
        aiMessage.setRole("assistant");
        aiMessage.setContent(aiReply);
        aiMessage.setCreatedAt(Instant.now());
        messageRepository.save(aiMessage);
        conversation.setUpdatedAt(Instant.now());
        conversationRepository.save(conversation);
        return	new	ChatMessageResponse(conversationId,	"assistant",	aiReply,	null);
    }

    public	Conversation	createConversation(String	userId)	{
        Conversation	conversation	=	new	Conversation();
        conversation.setUserId(userId);
        conversation.setTitle("Nova	conversa");
        conversation.setTemperature(0.7);
        conversation.setCreatedAt(Instant.now());
        conversation.setUpdatedAt(Instant.now());
        return	conversationRepository.save(conversation);
    }

    public List<Conversation> listConversations(String userId) {
        return conversationRepository.findByUserIdOrderByUpdatedAtDesc(userId);
    }
}