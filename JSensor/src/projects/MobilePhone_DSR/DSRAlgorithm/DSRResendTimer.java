package projects.MobilePhone_DSR.DSRAlgorithm;

import jsensor.nodes.events.TimerEvent;
import jsensor.runtime.Jsensor;

import projects.MobilePhone_DSR.DSRAlgorithm.DSRAlgorithm;

public class DSRResendTimer extends TimerEvent {    
    private DSRMessage message;

    public void setMessage(DSRMessage message) {
        this.message = message;
    }    

    @Override
    public void fire() {
        Jsensor.log("[Resend message] time: " + Jsensor.currentTime + " node " +
                    this.node.getID() + " will resend the message to node " + message.getSender().getID());
        
        DSRRoute sourceRoute = ((DSRNode) this.node).getDsr().routeDiscovery((DSRNode) this.node, message.getDestination().getID());
        
        if (sourceRoute != null) {
            // Adds source route to this node's cache
            ((DSRNode) this.node).getRouteCache().storeRoute(message.getDestination().getID(), sourceRoute);
            // Timer to remove route
            ((DSRNode) this.node).getDsr().setRouteTtlTimer(((DSRNode) this.node), message.getDestination().getID());
            sourceRoute.getRoute().addFirst(this.node.getID());
            message.setRoute(sourceRoute);                        
            ((DSRNode) this.node).getDsr().sendMessage((DSRNode) this.node, (DSRNode) Jsensor.getNodeByID(sourceRoute.getRoute().getFirst()), message);            
        } else {
            Jsensor.log("[No route] time: " + Jsensor.currentTime + " nodeID: " +
                        message.getDestination().getID());
            Jsensor.log("[No route] position: " + message.getDestination().getPosition().getPosX() +
                        ", " + message.getDestination().getPosition().getPosY());
        }
    }
}
