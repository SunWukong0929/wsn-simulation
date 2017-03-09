package projects.MobilePhone_AODV.AODVAlgorithm;

import java.util.HashMap;

public class AODVRouteCache {
    private HashMap<Integer, AODVRouteEntry> routeCache;
    
   
    public AODVRouteCache() {
        routeCache = new HashMap<Integer, AODVRouteEntry>();
    }
    
    public AODVRouteEntry getRouteEntry(int destination) {
        return routeCache.get(destination);
    }
    
    public void storeRouteEntry(int destination, AODVRouteEntry routeEntry) {
        routeCache.put(destination, routeEntry);
    }
    
    public void removeRoute(int destination) {
        routeCache.remove(destination);
    }
    
    @Override
    public String toString() {
        return routeCache.toString();
    }

}
