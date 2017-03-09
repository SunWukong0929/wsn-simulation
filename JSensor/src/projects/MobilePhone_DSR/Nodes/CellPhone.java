package projects.MobilePhone_DSR.Nodes;

import java.util.LinkedList;

import projects.MobilePhone_DSR.DSRAlgorithm.DSRNode;
import projects.MobilePhone_DSR.DSRAlgorithm.DSRRoute;
import projects.MobilePhone_DSR.Messages.SMSMessage;
import projects.MobilePhone_DSR.Timers.CellPhoneMobilityTimer;
import projects.MobilePhone_DSR.Timers.SMSTimer;
import projects.MobilePhone_DSR.DSRAlgorithm.DSRResendTimer;
import jsensor.nodes.messages.Inbox;
import jsensor.nodes.messages.Message;
import jsensor.runtime.Jsensor;
import jsensor.utils.Configuration;

public class CellPhone extends DSRNode{

    private int myAntennaID;
    private LinkedList<Long> messagesIDs; 
           
    @Override
    public void handleMessages(Inbox inbox) {
        while (inbox.hasMoreMessages()) {
            Message msgTmp = inbox.getNextMessage();

            if (msgTmp instanceof SMSMessage) {                
                SMSMessage smsMessage = (SMSMessage) msgTmp;

                if (!this.messagesIDs.contains(smsMessage.getID())) {
                    this.messagesIDs.add(smsMessage.getID());

                    // Updates node's cache with reversed source route
                    DSRRoute revertedRoute = new DSRRoute(this.dsr.revertRoute(smsMessage.getRoute()));                    
                    revertedRoute.getRoute().removeFirst();
                    revertedRoute.getRoute().addLast(smsMessage.getSender().getID());
                    this.getRouteCache().storeRoute(smsMessage.getSender().getID(), revertedRoute);
                    // Timer to remove route
                    this.dsr.setRouteTtlTimer(this, smsMessage.getSender().getID());

                    Jsensor.log("[Received] time: " + Jsensor.currentTime + "\t sensorID: " +
                                this.ID + "\t receivedFrom: " + smsMessage.getSender().getID() +
                                "\t hops: " + smsMessage.getHops() + "\t msg: "+
                                smsMessage.getMessage().concat(" - " + String.valueOf(this.ID)));
                    
                    // Probability of reply a message
                    if (this.getRandom().nextDouble() < 0.6) {                                                                                  
                        this.findAntenna();
                        
                        if (this.getMyAntenna() > -1) {                                                                                                                       
                            SMSMessage replyMessage = new SMSMessage(this, smsMessage.getSender(), 0, this.getChunk());                                
                            replyMessage.setRoute(revertedRoute);
                            DSRNode nextNode = this.dsr.nextNode(this.ID, replyMessage.getRoute());
                            this.dsr.sendMessage(this, nextNode, replyMessage);                                
                        } else {
                            Jsensor.log("[No signal] time: " + Jsensor.currentTime + 
                                        " node " + this.getID() + " has no signal");
                        }
                    }
                }
            }
        }
    }
    
    public int getMyAntenna()
    {
    	return myAntennaID;
    } 
    
    public void findAntenna()
    {
    	if(this.getNeighbours().getNodesList().isEmpty())
    		myAntennaID = -1;
    	else
    		myAntennaID = this.getNeighbours().getNodesList().first().getID();
    }
    
    @Override
    public void onCreation() 
    {
    	//initializes the list of messages received by the node.
        this.messagesIDs = new LinkedList<Long>();
        this.myAntennaID = -1;
        //probability of send a message
    	if(this.getRandom().nextDouble() < 0.8){
			int time = 1 + this.getRandom().nextInt((int)Configuration.numberOfRounds);    	    
	    	SMSTimer st = new SMSTimer();
	    	st.startRelative(time, this);   
    	}
    	//probability of move
    	if(this.getRandom().nextDouble() < 0.3){
	    	CellPhoneMobilityTimer ct = new CellPhoneMobilityTimer();
	    	ct.start(this, this.getRandom().nextInt(100), Configuration.numberOfRounds, 10);	    	
    	}
    	
    	/*In order to avoid source routes of type:
    	cellphone (source), antenna, cellphone, antenna, cellphone (destination)
    	route forwarding must be disabled for nodes of type cell phone*/
    	this.enableForwarding = false;
    }
}
