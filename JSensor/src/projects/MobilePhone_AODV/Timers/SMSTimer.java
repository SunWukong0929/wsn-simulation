package projects.MobilePhone_AODV.Timers;

import jsensor.nodes.events.TimerEvent;
import jsensor.runtime.Jsensor;
import projects.MobilePhone_AODV.AODVAlgorithm.AODVMessageTransmissionModel;
import projects.MobilePhone_AODV.AODVAlgorithm.AODVNode;
import projects.MobilePhone_AODV.AODVAlgorithm.AODVRemoveRouteEntryTimer;
import projects.MobilePhone_AODV.AODVAlgorithm.AODVRouteEntry;
import projects.MobilePhone_AODV.AODVAlgorithm.AODVUtils;
import projects.MobilePhone_AODV.AODVAlgorithm.AODVUtils.AODVConstants;
import projects.MobilePhone_AODV.Messages.SMSMessage;
import projects.MobilePhone_AODV.Nodes.CellPhone;

public class SMSTimer extends TimerEvent {
    @Override
    public void fire() {
        boolean sourceNodeSignal;
        AODVNode destNode;        
        
        do {
            destNode = (AODVNode) this.node.getRandomNode("CellPhone");

        } while (destNode == null || destNode == this.node);
            
        sourceNodeSignal = hasSignal((AODVNode) this.node);

        if (sourceNodeSignal) {
            AODVNode sourceNodesAntenna = (AODVNode) Jsensor.getNodeByID(((CellPhone) this.node).
                    getMyAntenna()); 
            
            boolean routeFound = ((AODVNode) this.node).getAodv().routeDiscovery(sourceNodesAntenna,
                    destNode.getID());

            if (routeFound == true) {
                SMSMessage msg = new SMSMessage((AODVNode) this.node, destNode, 0,
                                                this.node.getChunk());
                msg.setMessage("This is the message number " + this.node.getChunk() +
                               " created by the node " + node.getID() + " path " +
                               this.node.getID());
                
                Jsensor.log("* time: " + Jsensor.currentTime + " nodeID: " +
                            this.node.getID() + " sendTo: " + destNode.getID() +
                            " byAntenna: " + sourceNodesAntenna.getID());
                
                // Updates source node's cache 
                AODVRouteEntry routeToDestNode = new AODVRouteEntry(destNode.getID(), sourceNodesAntenna.getID(), 1, AODVMessageTransmissionModel.timeToReach() * AODVConstants.getActiveRouteTime() + Jsensor.currentTime);
                ((AODVNode) this.node).getRouteCache().storeRouteEntry(destNode.getID(), routeToDestNode);
                ((AODVNode) this.node).getAodv().setRouteTtlTimer(((AODVNode) this.node), destNode.getID());                                     
                
                ((AODVNode) this.node).getAodv().sendMessage(((AODVNode) this.node), (AODVNode) Jsensor.getNodeByID(routeToDestNode.getNext()), msg);
            } else {                                
                Jsensor.log("[No route] time: " + Jsensor.currentTime + " source node ID: " + this.node.getID() +
                            " dest node ID: " + destNode.getID());
            }
        } else if (!sourceNodeSignal) {            
            Jsensor.log("[No signal] time: " + Jsensor.currentTime + " nodeID: "
                    + this.node.getID());
            Jsensor.log(
                    "[No signal] position: " + this.node.getPosition().getPosX()
                            + ", " + this.node.getPosition().getPosY());
        } else {                         
            Jsensor.log("[No signal] time: " + Jsensor.currentTime + " nodeID: "
                    + destNode.getID());
            Jsensor.log(
                    "[No signal] position: " + destNode.getPosition().getPosX()
                            + ", " + destNode.getPosition().getPosY());
        }                  
    }

    private boolean hasSignal(AODVNode node) {
        if (node instanceof CellPhone) {
            ((CellPhone) node).findAntenna();

            return ((CellPhone) node).getMyAntenna() > -1;
        } else {
            return true;
        }
    }
}
