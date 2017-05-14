package projects.Flooding.Messages;

import jsensor.nodes.Node;
import jsensor.nodes.messages.Message;
import jsensor.runtime.Jsensor;

/**
 * Created by Dev on 14/05/2017.
 */
public class HeaderControl extends Message {
    private String heads;
    private Node sender;
    private int hops;
    short chunk;


    public HeaderControl(String heads, int hops, long ID) {
        this.setID(ID);
        this.heads = heads;
        this.sender = Jsensor.getNodeByID(1);
        this.hops = hops;
    }

    public HeaderControl(String heads, int hops, short chunk) {
        this.chunk = chunk;
        this.heads = heads;
        this.sender = Jsensor.getNodeByID(1);
        this.hops = hops;
    }

    public String getHeads() {
        return heads;
    }

    public void setHeads(String heads) {
        this.heads = heads;
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
        return new HeaderControl(heads, hops + 1, this.getID());
    }
}