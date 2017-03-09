package projects.MobilePhone_DSR.DSRAlgorithm;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.LinkedList;

public class DSRRoute {
    private LinkedList<Integer> route;
    private long ttl;
    
    public DSRRoute() {
        this.route = new LinkedList<Integer>();
    }
    
    public DSRRoute(List<Integer> route, long ttl) {
        this.route = new LinkedList<Integer>(route);
        this.ttl = ttl;
    }
    
    public DSRRoute(DSRRoute route) {
        this.route = new LinkedList<Integer>(route.getRoute());
        this.ttl = route.getTtl();
    }
    
    public LinkedList<Integer> getRoute() {
        return route;
    }
           
    public void setRoute(DSRRoute route) {
        this.route = new LinkedList<Integer>(route.getRoute());
    }
    
    public long getTtl() {
        return ttl;
    }
    
    public void setTtl(long ttl) {
        this.ttl = ttl;
    }
    
    public String toString() {
        return route.toString();
    }
}
