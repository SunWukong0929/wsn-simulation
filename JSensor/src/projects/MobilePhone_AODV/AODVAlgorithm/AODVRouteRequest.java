package projects.MobilePhone_AODV.AODVAlgorithm;

public class AODVRouteRequest {
    private int sourceID, destID, hopCount;    
    
    public AODVRouteRequest(int sourceID, int destID, int hopCount) {
        this.sourceID = sourceID;
        this.destID = destID;        
        this.hopCount = hopCount;
    }
    
    public AODVRouteRequest() {
        
    }
    
    public int getSourceID() {
        return sourceID;
    }
    
    public void setSourceID(int sourceID) {
        this.sourceID = sourceID;
    }
    
    public int getDestID() {
        return destID;
    }
    
    public void setDestID(int destID) {
        this.destID = destID;
    }    
    
    public int getHopCount() {
        return hopCount;
    }
    
    public void setHopCount(int hopCount) {
        this.hopCount = hopCount;
    }       
}
