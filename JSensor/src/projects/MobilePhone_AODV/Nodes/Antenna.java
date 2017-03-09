package projects.MobilePhone_AODV.Nodes;

import java.util.LinkedList;

import jsensor.nodes.messages.Message;
import jsensor.nodes.messages.Inbox;
import jsensor.runtime.Jsensor;
import projects.MobilePhone_AODV.AODVAlgorithm.AODVNode;
import projects.MobilePhone_AODV.AODVAlgorithm.AODVRouteEntry;
import projects.MobilePhone_AODV.CellModels.SetFactCell;
import projects.MobilePhone_AODV.Messages.SMSMessage;

public class Antenna extends AODVNode {
    private LinkedList<Long> messagesIDs;
    
    @Override
    public void handleMessages(Inbox inbox) {       
        if (!this.isActive())
            return;

        SetFactCell setThunder = new SetFactCell();
        setThunder.addCell(this, this.position);

        while (inbox.hasMoreMessages()) {
            Message m = inbox.getNextMessage();

            if (m instanceof SMSMessage) {
                SMSMessage smsMessage = (SMSMessage) m;
                
                if (!this.messagesIDs.contains(smsMessage.getID())) {
                    this.messagesIDs.add(smsMessage.getID());
                    smsMessage.setMessage(smsMessage.getMessage().concat(" - " + Integer.toString(this.ID)));
                                                                  
                    AODVRouteEntry routeEntry = this.getRouteCache().getRouteEntry(smsMessage.getDestination().getID());

                    if (routeEntry != null) {
                        // Unicasts message to next node
                        
                        if (routeEntry.getNext() == smsMessage.getDestination().getID()) {                            
                            if (this.getNeighbours().getNodesList().contains(smsMessage.getDestination())) {
                                // Antenna still can reach original destination node
                                this.unicast(m, Jsensor.getNodeByID(smsMessage.getDestination().getID()));
                            } else {
                                System.out.println("Destino sem sinal");
                                
                                Jsensor.log("[No signal] time: " + Jsensor.currentTime + 
                                            " nodeID: " + this.getID());
                                Jsensor.log("[No signal] position: "+ this.getPosition().getPosX() +
                                            ", " + this.getPosition().getPosY());
                            }
                        } else {
                            AODVNode nextNode = (AODVNode) Jsensor.getNodeByID(routeEntry.getNext());
                            boolean sendResult = this.aodv.sendMessage(this, nextNode, smsMessage);
                            
                            if (sendResult == false) {
                                Jsensor.log("[Route error] time: " + Jsensor.currentTime + 
                                        " route to node " + smsMessage.getDestination().getID() +
                                        " must be rediscovered");
                                
                                this.aodv.removeRouteFromPrecursors(this, nextNode.getID());                            
                                this.aodv.newRouteDiscovery(this, smsMessage);
                            }
                        }
                    } else {
                        this.aodv.newRouteDiscovery(this, smsMessage);
                    }
                }
            }
        }
    }

    @Override
    public void onCreation() {
        //initializes the list of messages received by the node.
        this.messagesIDs = new LinkedList<Long>();
        
        /*
        System.out.println("Antenna " + this.getID() + " position: " + "("
                + this.getPosition().getPosX() + "; "
                + this.getPosition().getPosY() + ")");*/
    }
}
