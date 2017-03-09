package projects.MobilePhone_AODV.AODVAlgorithm;

import projects.MobilePhone_AODV.AODVAlgorithm.AODVUtils.AODVConstants;
import projects.MobilePhone_AODV.Nodes.CellPhone;
import jsensor.nodes.events.TimerEvent;
import jsensor.runtime.Jsensor;

public class AODVResendTimer extends TimerEvent {
    private AODVMessage message;

    public void setMessage(AODVMessage message) {
        this.message = message;
    }

    @Override
    public void fire() {        
        boolean routeFound = ((AODVNode) this.node).getAodv().routeDiscovery((AODVNode) this.node, message.getDestination().getID());

        if (routeFound == true) {                       
            AODVRouteEntry re = ((AODVNode) this.node).getRouteCache().getRouteEntry(message.getDestination().getID());
            ((AODVNode) this.node).aodv.sendMessage((AODVNode) this.node, (AODVNode) Jsensor.getNodeByID(re.getNext()), message);            
        } else {                       
            Jsensor.log("[No route] time: " + Jsensor.currentTime + " nodeID: " + message.getDestination().getID());
            Jsensor.log("[No route] position: " + message.getDestination().getPosition().getPosX() + ", " + message.getDestination().getPosition().getPosY());
        }
    }
}
