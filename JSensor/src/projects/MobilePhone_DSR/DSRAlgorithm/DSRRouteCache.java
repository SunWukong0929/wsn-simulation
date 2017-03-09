package projects.MobilePhone_DSR.DSRAlgorithm;

import java.util.concurrent.ConcurrentHashMap;

public class DSRRouteCache {
    private ConcurrentHashMap<Integer, DSRRoute> routeCache;
    
    public DSRRouteCache() {
        this.routeCache = new ConcurrentHashMap<Integer, DSRRoute>();
    }
    
    public DSRRouteCache(DSRRouteCache routeCache) {
        this.routeCache = new ConcurrentHashMap<Integer, DSRRoute>(routeCache.getRouteCache());
    }
    
    public ConcurrentHashMap<Integer, DSRRoute> getRouteCache() {
        return routeCache;
    }
    
    public DSRRoute getRoute(int destination) {
        return routeCache.get(destination);
    }   
    
    public void storeRoute(int destination, DSRRoute route) {
        DSRRoute r = routeCache.get(destination);
        
        if (r == null || r.getRoute().size() > route.getRoute().size())
            routeCache.put(destination, new DSRRoute(route));
    }
    
    public void removeRoute(int destination) {
        if (destination >= 0)
            routeCache.remove(destination);
    }
    
    public boolean containsRoute(int destination) {
        return routeCache.contains(destination);
    }
    
    public String toString() {
        return routeCache.toString();                
    }
}
