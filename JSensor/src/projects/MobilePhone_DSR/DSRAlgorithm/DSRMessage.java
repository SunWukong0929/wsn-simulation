package projects.MobilePhone_DSR.DSRAlgorithm;

import jsensor.nodes.messages.Message;

public class DSRMessage extends Message {
    private int hops;
    private DSRNode sender, destination;
    private DSRRoute route;

    public DSRMessage(DSRNode sender, DSRNode destination, int hops, short chunk) {
        super(chunk);

        this.sender = sender;
        this.destination = destination;
        this.hops = hops;
        this.route = new DSRRoute();
    }
    
    public DSRMessage(DSRNode sender, DSRNode destination, int hops, long ID) {
        this.setID(ID);
        this.sender = sender;
        this.destination = destination;
        this.hops = hops;
        this.route = new DSRRoute();
    }

    public DSRMessage(long ID, DSRNode sender, DSRNode destination, int hops,
                      DSRRoute route) {       
        this.setID(ID);
        this.sender = sender;
        this.destination = destination;
        this.hops = hops;
        this.route = route;
    }
    
    public DSRMessage() {
        
    }

    public DSRNode getSender() {
        return sender;
    }

    public void setSender(DSRNode sender) {
        this.sender = sender;
    }

    public DSRNode getDestination() {
        return destination;
    }

    public void setDestination(DSRNode destination) {
        this.destination = destination;
    }
   
    public int getHops() {
        return hops;
    }

    public void setHops(int hops) {
        this.hops = hops;
    }

    public DSRRoute getRoute() {
        return route;
    }

    public void setRoute(DSRRoute route) {
        this.route = new DSRRoute(route);
    }

    @Override
    public Message clone() {
        return new DSRMessage(this.getID(), this.sender, this.destination,
                              this.hops + 1, this.route);
    }
}