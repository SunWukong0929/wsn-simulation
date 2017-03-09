package projects.MobilePhone_DSR.DSRAlgorithm;

import jsensor.nodes.events.TimerEvent;
import jsensor.runtime.Jsensor;

public class DSRRemoveRouteEntryTimer extends TimerEvent {
    private int entryToRemove;

    @Override
    public void fire() {
        DSRRoute re = ((DSRNode) this.node).getRouteCache().getRoute(entryToRemove); 
        
        if (re != null && re.getTtl() == Jsensor.currentTime )
            ((DSRNode) this.node).getRouteCache().removeRoute(entryToRemove);            
    }

    public void setEntryToRemove(int entryToRemove) {
        this.entryToRemove = entryToRemove;
    }    
}
