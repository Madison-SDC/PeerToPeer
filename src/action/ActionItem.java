package action;

import java.io.Serializable;

public class ActionItem implements Serializable {
	
	private Object action;
	
	public ActionItem(Object e) {
		this.action = e;
	}
	
	public String toString() {
		return action.toString();
	}
	
}
