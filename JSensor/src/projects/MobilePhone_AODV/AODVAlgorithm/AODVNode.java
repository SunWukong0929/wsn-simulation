package projects.MobilePhone_AODV.AODVAlgorithm;

import java.util.LinkedList;

import jsensor.nodes.Node;

public abstract class AODVNode extends Node {
    protected AODVAlgorithm aodv;        
    private ThreadLocal<AODVRouteRequest> rreq;
    //private LinkedList<Integer> precursorsList;
    private AODVRouteCache routeCache;
    private ThreadLocal<AODVNode> previousNode;
    protected boolean enableForwarding;

    public AODVNode() {
        super();
        routeCache = new AODVRouteCache();        
        aodv = new AODVAlgorithm();
        //precursorsList = new LinkedList<Integer>();
        previousNode = new ThreadLocal<AODVNode>();
        enableForwarding = true;
        rreq = new ThreadLocal<AODVRouteRequest>() {
            @Override
            protected AODVRouteRequest initialValue() {
                return new AODVRouteRequest();
            }
        };        
    }
    
    /*
    public LinkedList<Integer> getPrecursorList() {
        return precursorsList;
    }
    
    public void insertPrecursor(int precursor) {
        precursorsList.add(precursor);
    }
    */
    
    public AODVAlgorithm getAodv() {
        return aodv;
    }

    public void setAodv(AODVAlgorithm aodv) {
        this.aodv = aodv;
    }
    
    public AODVRouteRequest getRreq() {
        return rreq.get();
    }

    public void setRreq(AODVRouteRequest rreq) {
        this.rreq.set(rreq);
    }         
    
    public AODVRouteCache getRouteCache() {
        return routeCache;
    }
    
    public AODVNode getPreviousNode() {
        return previousNode.get();
    }

    public void setPreviousNode(AODVNode previousNode) {
        this.previousNode.set(previousNode);
    }
    
    public boolean getEnableForwarding() {
        return enableForwarding;
    }

    public void setEnableForwarding(boolean enableForwarding) {
        this.enableForwarding = enableForwarding;
    }

    public boolean isNeighbour(AODVNode node) {
        return this.getNeighbours().getNodesList().contains(node);
    }
}
