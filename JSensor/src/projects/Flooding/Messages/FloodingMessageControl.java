package projects.Flooding.Messages;

import jsensor.nodes.Node;
import jsensor.nodes.messages.Message;

/**
 * Created by matheus on 5/11/17.
 */
public class FloodingMessageControl extends Message {
    private double energy;
    private Node sender;
    private int hops;
    short chunk;


    public FloodingMessageControl(double energy, Node sender, int hops, long ID) {
        this.energy = energy;
        this.sender = sender;
        this.hops = hops;
    }

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
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

    @Override
    public Message clone() {
        return new FloodingMessageControl(energy, sender, hops + 1, this.getID());
    }
}