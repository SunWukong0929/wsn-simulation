package projects.MobilePhone_AODV.AODVAlgorithm;

import jsensor.nodes.events.TimerEvent;
import jsensor.runtime.Jsensor;

public class AODVRemoveRouteEntryTimer extends TimerEvent {
    private int entryToRemove;

    @Override
    public void fire() {
        AODVRouteEntry re = ((AODVNode) this.node).getRouteCache().getRouteEntry(entryToRemove); 
        
        if (re != null && re.getTtl() == Jsensor.currentTime )
            ((AODVNode) this.node).getRouteCache().removeRoute(entryToRemove);
    }

    public void setEntryToRemove(int entryToRemove) {
        this.entryToRemove = entryToRemove;
    }    
}
