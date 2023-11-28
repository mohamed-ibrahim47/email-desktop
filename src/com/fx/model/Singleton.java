package com.fx.model;

public class Singleton {
	// this class is used to bind massage data between main controller and email detail controller 
	//replaced with absrtactcontroller
	
	private Singleton(){}
	private static Singleton instance = new Singleton();	
	public static Singleton getIntance(){
		return instance;
	}	
	
	
	private EmailMessageBean message;
	public EmailMessageBean getMessage() {
		return message;
	}

	public void setMessage(EmailMessageBean message) {
		this.message = message;
	}
}
