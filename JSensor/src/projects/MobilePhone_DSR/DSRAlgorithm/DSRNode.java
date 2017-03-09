package projects.MobilePhone_DSR.DSRAlgorithm;

import java.util.LinkedList;

import jsensor.nodes.Node;

public abstract class DSRNode extends Node {
    protected DSRAlgorithm dsr;    
    private ThreadLocal<LinkedList<Integer>> forwardRoute;
    private DSRRouteCache routeCache;
    protected boolean enableForwarding;   

    public DSRNode() {
        super();        
        routeCache = new DSRRouteCache();
        dsr = new DSRAlgorithm();
        enableForwarding = true;
        forwardRoute = new ThreadLocal<LinkedList<Integer>>() {
            @Override protected LinkedList<Integer> initialValue() {
                return new LinkedList<Integer>();
            }
        };        
    }
       
    public DSRAlgorithm getDsr() {
        return dsr;
    }
    
    public DSRRouteCache getRouteCache() {
        return routeCache;               
    }
    
    public ThreadLocal<LinkedList<Integer>> getForwardRoute() {
        return forwardRoute;
    }
    
    public void setForwardRoute(DSRRoute forwardRoute) {
        this.forwardRoute.set(new LinkedList<Integer>(forwardRoute.getRoute()));
    }
    
    public boolean isEnableForwarding() {
        return enableForwarding;
    }

    public void setEnableForwarding(boolean enableForwarding) {
        this.enableForwarding = enableForwarding;
    }

    public boolean isNeighbour(DSRNode node) {
        return this.getNeighbours().getNodesList().contains(node);
    }
}
