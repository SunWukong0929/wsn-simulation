package projects.MobilePhone_AODV.Messages;

import projects.MobilePhone_AODV.AODVAlgorithm.AODVMessage;
import projects.MobilePhone_AODV.AODVAlgorithm.AODVNode;

/**
 *
 * @author Matheus
 */
public class SMSMessage extends AODVMessage{
    private String message;

    public SMSMessage(AODVNode sender, AODVNode destination, int hops, short chunck) {
    	super(sender, destination, hops, chunck);
    	
        this.message = "";
    }

    public SMSMessage(AODVNode sender, AODVNode destination, int hops, long ID,
                      String message) {
        super(ID, sender, destination, hops);
        
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
                              this.getHops() + 1, this.getID(), this.message);
    }
}
