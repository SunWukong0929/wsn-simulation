package projects.MobilePhone_AODV.AODVAlgorithm;

public class AODVRouteEntry {
    private int destination, next, hops;
    private long ttl;
    
    public AODVRouteEntry(int destination, int next, int hops, long ttl) {
        this.destination = destination;
        this.next = next;
        this.hops = hops;
        this.ttl = ttl;
    }
    
    public AODVRouteEntry() {
        
    }
    
    public int getDestination() {
        return destination;
    }
    
    public void setDestination(int destination) {
        this.destination = destination;
    }
    
    public int getNext() {
        return next;
    }
    
    public void setNext(int next) {
        this.next = next;
    }
    
    public int getHops() {
        return hops;
    }
    
    public void setHops(int hops) {
        this.hops = hops;
    }
    
    public long getTtl() {
        return ttl;
    }
    
    public void setTTL(long ttl) {
        this.ttl = ttl;
    }
    
    @Override
    public String toString() {
        return "Destination: " + this.destination + ", Next: " + this.next +
               ", Hops: " + this.hops + ", TTL: " + this.ttl + "\n";
    }
}
