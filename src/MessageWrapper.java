enum MessageType{
	MESSAGE, REGISTER_USERNAME, FRIENDS_ONLINE, LOGOUT
}

public class MessageWrapper{
	private MessageType _type;
	private String _recipient;
	private String _message;
	
	public MessageWrapper(MessageType type, String recipient, String message){
		this._type = type;
		this._recipient = recipient;
		this._message = message;
	}
	
	public MessageType getMessageType(){
		return this._type;
	}
	
	public String getRecipient(){
		return this._recipient;
	}
	
	public String getMessage(){
		return this._message;
	}
	
}