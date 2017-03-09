package projects.MobilePhone_AODV.AODVAlgorithm;

import jsensor.nodes.messages.Message;

public class AODVMessage extends Message {
    private int hops;     
    private AODVNode sender, destination;

    public AODVMessage(AODVNode sender, AODVNode destination, int hops, short chunk) {
        super(chunk);

        this.sender = sender;
        this.destination = destination;
        this.hops = hops;
    }   

    public AODVMessage(long ID, AODVNode sender, AODVNode destination, int hops) {
        super();
        
        this.setID(ID);
        this.sender = sender;
        this.destination = destination;        
        this.hops = hops;
    }
    
    public AODVMessage(long ID, AODVNode sender, AODVNode destination, int hops,
                       int intermediate) {
        super();
        
        this.setID(ID);
        this.sender = sender;
        this.destination = destination;        
        this.hops = hops;
    }

    public AODVNode getSender() {
        return sender;
    }

    public void setSender(AODVNode sender) {
        this.sender = sender;
    }

    public AODVNode getDestination() {
        return destination;
    }

    public void setDestination(AODVNode destination) {
        this.destination = destination;
    }

    public int getHops() {
        return hops;
    }

    public void setHops(int hops) {
        this.hops = hops;
    }

    @Override
    public Message clone() {
        return new AODVMessage(this.getID(), this.sender, this.destination,
                               this.hops + 1);
    }
}