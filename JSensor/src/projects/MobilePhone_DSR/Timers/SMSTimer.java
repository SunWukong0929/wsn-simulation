package projects.MobilePhone_DSR.Timers;

import jsensor.nodes.events.TimerEvent;
import jsensor.runtime.Jsensor;
import projects.MobilePhone_DSR.DSRAlgorithm.DSRNode;
import projects.MobilePhone_DSR.DSRAlgorithm.DSRRoute;
import projects.MobilePhone_DSR.Messages.SMSMessage;
import projects.MobilePhone_DSR.Nodes.CellPhone;

public class SMSTimer extends TimerEvent {
    @Override
    public void fire() {
        boolean sourceNodeSignal;
        DSRNode destNode;

        do {
            destNode = (DSRNode) this.node.getRandomNode("CellPhone");

        } while (destNode == null || destNode == this.node);
        
        Jsensor.log("-> Node " + this.node.getID() + " (" + this.node.getPosition().getPosX() +
                    "; " + this.node.getPosition().getPosY() + ")" + " is looking for node: " +
                    destNode.getID() + " (" + destNode.getPosition().getPosX() + "; " +
                    destNode.getPosition().getPosY() + ")\n");

        sourceNodeSignal = hasSignal((DSRNode) this.node);

        if (sourceNodeSignal) {
            SMSMessage msg = new SMSMessage((DSRNode) this.node, destNode, 0,
                                            this.node.getChunk());

            msg.setMessage("This is the message number " + this.node.getChunk() +
                           " created by the node " + node.getID() + " path " +
                           this.node.getID());
            Jsensor.log("[Sent] time: " + Jsensor.currentTime + " nodeID: " + this.node.getID() +
                        " sendTo: " + destNode.getID() + " byAntenna: " + ((CellPhone) this.node).getMyAntenna());

            DSRNode sourceNodesAntenna = (DSRNode) Jsensor.getNodeByID(((CellPhone) this.node).
                                                                      getMyAntenna());
            
            DSRRoute sourceRoute = ((DSRNode) this.node).getDsr().routeDiscovery(sourceNodesAntenna,
                                                                                 destNode.getID());

            if (sourceRoute != null) {
                // Adds source (discovered) route to source node's cache
                ((DSRNode) this.node).getRouteCache().storeRoute(destNode.getID(), sourceRoute);
                // Adds source (discovered) route to source node antenna's cache
                sourceNodesAntenna.getRouteCache().storeRoute(destNode.getID(), sourceRoute);
                // Set timer to remove route from source node's cache
                ((DSRNode) this.node).getDsr().setRouteTtlTimer(((DSRNode) this.node), destNode.getID());
                // Set timer to remove route from source node antenna's cache
                sourceNodesAntenna.getDsr().setRouteTtlTimer(sourceNodesAntenna, destNode.getID());
                
                msg.setRoute(sourceRoute);                
                ((DSRNode) this.node).getDsr().sendMessage(((DSRNode) this.node), ((DSRNode) this.node).getDsr().nextNode(this.node.getID(), msg.getRoute()), msg);
            } else {                
                Jsensor.log("[No route] time: " + Jsensor.currentTime + " source node ID: " + this.node.getID() +
                            " dest node ID: " + destNode.getID());
            }
        } else if (!sourceNodeSignal) {
            // Source node is a CellPhone and has no signal

            Jsensor.log("[No signal] time: " + Jsensor.currentTime + " nodeID: " +
                        this.node.getID());
        } else {
            // Destination node is a CellPhone and has no signal

            Jsensor.log("[No signal] time: " + Jsensor.currentTime + " nodeID: " + destNode.getID());
            Jsensor.log("[No signal] position: " + destNode.getPosition().getPosX() +
                        ", " + destNode.getPosition().getPosY());
        }
    }

    private boolean hasSignal(DSRNode node) {
        if (node instanceof CellPhone) {
            ((CellPhone) node).findAntenna();

            return ((CellPhone) node).getMyAntenna() > -1;
        } else {
            return true;
        }
    }
}
