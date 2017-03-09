package projects.MobilePhone_DSR.Nodes;

import java.util.LinkedList;

import jsensor.nodes.messages.Message;
import jsensor.nodes.messages.Inbox;
import jsensor.runtime.Jsensor;
import projects.MobilePhone_DSR.CellModels.SetFactCell;
import projects.MobilePhone_DSR.DSRAlgorithm.DSRMessageTransmissionModel;
import projects.MobilePhone_DSR.DSRAlgorithm.DSRNode;
import projects.MobilePhone_DSR.DSRAlgorithm.DSRResendTimer;
import projects.MobilePhone_DSR.DSRAlgorithm.DSRRoute;
import projects.MobilePhone_DSR.DSRAlgorithm.DSRUtils.DSRConstants;
import projects.MobilePhone_DSR.Messages.SMSMessage;

public class Antenna extends DSRNode {    
    private LinkedList<Long> messagesIDs;

    @Override
    public void handleMessages(Inbox inbox) {
        int currentNodeIndex, routeSize;
        long ttl = DSRMessageTransmissionModel.timeToReach() * DSRConstants.getActiveRouteTime();
        
        if (!this.isActive())
            return;

        SetFactCell setThunder = new SetFactCell();
        setThunder.addCell(this, this.position);

        while (inbox.hasMoreMessages()) {
            Message m = inbox.getNextMessage();

            if (m instanceof SMSMessage) {
                SMSMessage smsMessage = (SMSMessage) m;
                
                if (!this.messagesIDs.contains(smsMessage.getID())) {
                    currentNodeIndex = smsMessage.getRoute().getRoute().indexOf(this.ID);
                    routeSize = smsMessage.getRoute().getRoute().size();
                    DSRRoute partialRoute;

                    // Updates node's cache with reverted route
                    ttl += Jsensor.currentTime;
                    partialRoute = new DSRRoute(smsMessage.getRoute().getRoute().subList(0, currentNodeIndex + 1), ttl);
                    this.getRouteCache().storeRoute(smsMessage.getRoute().getRoute().getFirst(), this.dsr.revertRoute(partialRoute));
                    // Timer to remove route
                    this.dsr.setRouteTtlTimer(this, smsMessage.getRoute().getRoute().getFirst());
                    

                    int lastAntennaID = smsMessage.getRoute().getRoute().get(routeSize - 2);
                    
                    if (this.dsr.destinationNodeFound(this.getID(), lastAntennaID) == true) {                        
                        if (this.getNeighbours().getNodesList().contains(smsMessage.getDestination())) {
                            // Antenna still can reach original destination node
                            this.unicast(m, Jsensor.getNodeByID(smsMessage.getDestination().getID()));
                        } else {
                            Jsensor.log("[No signal] time: " + Jsensor.currentTime + 
                                        " nodeID: " + this.getID());
                            Jsensor.log("[No signal] position: "+ this.getPosition().getPosX() +
                                        ", " + this.getPosition().getPosY());
                        }
                    } else {
                        this.messagesIDs.add(smsMessage.getID());
                        ((SMSMessage) m).setMessage(smsMessage.getMessage().concat(" - " + Integer.toString(this.ID)));

                        // Unicasts message to next node
                        DSRNode nextNode = this.dsr.nextNode(this.ID, smsMessage.getRoute());
                        boolean sendResult = this.dsr.sendMessage(this, nextNode, smsMessage);

                        if (sendResult == true) {
                            // Updates node's cache with part of source route
                            ttl += Jsensor.currentTime;
                            partialRoute = new DSRRoute(smsMessage.getRoute().getRoute().subList(currentNodeIndex, routeSize), ttl);
                            this.getRouteCache().storeRoute(smsMessage.getRoute().getRoute().getLast(), partialRoute);
                            // Timer to remove route
                            this.dsr.setRouteTtlTimer(this, smsMessage.getRoute().getRoute().getLast());
                        } else {
                            // Route error                            
                            Jsensor.log("[Route error] time: " + Jsensor.currentTime + 
                                        " route to node " + smsMessage.getDestination().getID() +
                                        " must be rediscovered");
                            // Route must be deleted from source node's cache
                            this.getRouteCache().removeRoute(nextNode.getID());
                            this.dsr.routeError(this.getID(), smsMessage.getRoute());                            
                            this.dsr.newRouteDiscovery(this, smsMessage);                            
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onCreation() {
        //initializes the list of messages received by the node.
        this.messagesIDs = new LinkedList<Long>();
    }
}
