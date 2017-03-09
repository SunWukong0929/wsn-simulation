package projects.MobilePhone_DSR.Messages;

import projects.MobilePhone_DSR.DSRAlgorithm.DSRMessage;
import projects.MobilePhone_DSR.DSRAlgorithm.DSRNode;
import projects.MobilePhone_DSR.DSRAlgorithm.DSRRoute;


/**
 *
 * @author Matheus
 */
public class SMSMessage extends DSRMessage{
	
    private String message;

    public SMSMessage(DSRNode sender, DSRNode destination, int hops, short chunck) {
    	super(sender, destination, hops, chunck);
    	
        this.message = "";
    }

    public SMSMessage(DSRNode sender, DSRNode destination, int hops, long ID,
                      String message, DSRRoute route) {
        super(ID, sender, destination, hops, route);
        
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public SMSMessage clone() {
        return new SMSMessage(this.getSender(), this.getDestination(),
                              this.getHops() + 1, this.getID(), this.message,
                              this.getRoute());
    }
}
