package com.kiraai.backend.controller;
import	com.kiraai.backend.dto.ChatMessageRequest;
import	com.kiraai.backend.dto.ChatMessageResponse;
import	com.kiraai.backend.model.Conversation;
import com.kiraai.backend.model.Message;
import	com.kiraai.backend.service.ChatService;
import	org.springframework.http.ResponseEntity;
import	org.springframework.security.core.Authentication;
import	org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
public	class	ChatController	{
    private	final	ChatService	chatService;
    public	ChatController(ChatService	chatService)	{
        this.chatService	=	chatService;
    }
    @PostMapping("/conversations")
    public	ResponseEntity<Conversation>	createConversation(Authentication	authentication)	{
        String	userId	=	(String)	authentication.getPrincipal();
        Conversation	conversation	=	chatService.createConversation(userId);
        return	ResponseEntity.ok(conversation);
    }
    @PostMapping("/conversations/{conversationId}/messages")
    public	ResponseEntity<ChatMessageResponse>	sendMessage(
            @PathVariable	String	conversationId,
            @RequestBody	ChatMessageRequest	request,
            Authentication	authentication)	{
        String	userId	=	(String)	authentication.getPrincipal();
        ChatMessageResponse	response	=	chatService.sendMessage(userId,	conversationId,	request.getContent());
        return	ResponseEntity.ok(response);
    }

    @GetMapping("/conversations")
    public ResponseEntity<List<Conversation>> listConversations(Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        return ResponseEntity.ok(chatService.listConversations(userId));
    }

    @GetMapping("/conversations/{conversationId}/messages")
    public ResponseEntity<List<Message>> getMessages(
            @PathVariable String conversationId, Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        return ResponseEntity.ok(chatService.getMessages(userId, conversationId));
    }
}