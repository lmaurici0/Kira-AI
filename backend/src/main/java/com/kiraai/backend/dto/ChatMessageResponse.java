package com.kiraai.backend.dto;

public	class	ChatMessageResponse	{
    private	String	conversationId;
    private	String	role;
    private	String	content;
    private	Integer	tokenCount;
    public	ChatMessageResponse(String	conversationId,	String	role,	String	content,	Integer	tokenCount)	{
        this.conversationId	=	conversationId;
        this.role	=	role;
        this.content	=	content;
        this.tokenCount	=	tokenCount;
    }
    //	getters
    public	String	getConversationId()	{	return	conversationId;	}
    public	String	getRole()	{	return	role;	}
    public	String	getContent()	{	return	content;	}
    public	Integer	getTokenCount()	{	return	tokenCount;	}
}