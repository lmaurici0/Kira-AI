package com.kiraai.backend.dto;

public	class	AuthResponse	{
    private	String	token;
    private	String	userId;
    private	String	name;
    private	String	pictureUrl;
    public	AuthResponse(String	token,	String	userId,	String	name,	String	pictureUrl)	{
        this.token	=	token;
        this.userId	=	userId;
        this.name	=	name;
        this.pictureUrl	=	pictureUrl;
    }
    public	String	getToken()	{	return	token;	}
    public	String	getUserId()	{	return	userId;	}
    public	String	getName()	{	return	name;	}
    public	String	getPictureUrl()	{	return	pictureUrl;	}
}