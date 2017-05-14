package projects.Flooding.Messages;

import jsensor.nodes.Node;
import jsensor.nodes.messages.Message;

/**
 * Created by matheus on 5/11/17.
 */
public class FloodingPing extends Message {
    private int energy;
    private Node sender;
    private int hops;
    short chunk;
    Node destination;

    public FloodingPing(int type, Node sender, Node destination, int hops, long ID) {
        this.energy = energy;
        this.sender = sender;
        this.hops = hops;
        this.destination = destination;
    }

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getHops() {
        return hops;
    }

    public void setHops(int hops) {
        this.hops = hops;
    }

    public short getChunk() {
        return chunk;
    }

    public Node getSender() {
        return sender;
    }

    public void setSender(Node sender) {
        this.sender = sender;
    }

    public Node getDestination() {
        return destination;
    }

    public void setDestination(Node destination) {
        this.destination = destination;
    }

    @Override
    public Message clone() {
        return new FloodingPing(energy, sender, destination, hops + 1, this.getID());
    }
}