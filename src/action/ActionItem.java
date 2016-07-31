package action;

import java.io.Serializable;
import java.util.List;

public class ActionItem implements Serializable {
	
	private Object action;
	private List<String> recipients;
	private boolean broadcast;
	
	public ActionItem(Object e) {
		this.action = e;
	}
	
	public void setRecipients(List<String> recipients) { this.recipients = recipients; }
	public void addRecipient(String recipient) { this.recipients.add(recipient); }
	public void setBroadcast() { broadcast = true; }
	public void unsetBroadcast() { broadcast = false; }
	public boolean willBroadcast() { return broadcast; }
	public List<String> recipients() { return recipients; }
	
	public String toString() {
		return action.toString();
	}
}
