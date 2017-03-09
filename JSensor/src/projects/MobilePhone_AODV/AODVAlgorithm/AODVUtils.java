package projects.MobilePhone_AODV.AODVAlgorithm;

public class AODVUtils {    
    /*Test
    public static int ROUTENOTFOUND = 0;
    public static int ROUTEFOUND = 0;
    public static int NEWROUTEFOUND = 0;
    public static int NEWROUTENOTFOUND = 0;
    public static int ROUTE = 0;
    public static int CACHEDROUTE = 0;
    public static int NOROUTE = 0;
    public static int NODES = 0;
    public static int SOURCENODENOSIGNAL = 0;
    
    public static void printAux() {
        System.out.println("Routes found: " + ROUTEFOUND);
        System.out.println("Routes not found: " + ROUTENOTFOUND);
        System.out.println("New routes found: " + NEWROUTEFOUND);
        System.out.println("New routes not found: " + NEWROUTENOTFOUND);
        System.out.println("Discovered routes: " + ROUTE);
        System.out.println("Caches routes found: " + CACHEDROUTE);
        System.out.println("NO ROUTE: " + NOROUTE);
        System.out.println("NO SIGNAL: " + SOURCENODENOSIGNAL);
    }
    */
    
    public static class AODVConstants {
        private static final int activeRouteTime = 3000;
        
        public static int getActiveRouteTime() {
            return activeRouteTime;
        }
    }
}
