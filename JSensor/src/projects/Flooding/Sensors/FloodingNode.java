package projects.Flooding.Sensors;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import jsensor.runtime.Jsensor;
import jsensor.nodes.Node;
import jsensor.nodes.messages.Inbox;
import jsensor.nodes.messages.Message;
import org.jenetics.BitChromosome;
import org.jenetics.BitGene;
import org.jenetics.Genotype;
import org.jenetics.engine.Engine;
import org.jenetics.engine.EvolutionResult;
import org.jenetics.util.Factory;
import projects.Flooding.CustomGlobal;
import projects.Flooding.Messages.FloodingMessage;
import projects.Flooding.Messages.FloodingMessageControl;
import projects.Flooding.Timers.FloodingTimer;


/**
 * @author danniel & Matheus
 */
public class FloodingNode extends Node {

    public LinkedList<Long> messagesIDs;

    // Following variables were created by me \/

    // Constants

    public final double PACKET_SIZE = 400;
    public final double INITIAL_NODE_ENERGY = 0.05f;
    public final double IDLE_STATE_ENERGY = this.PACKET_SIZE * 5 * Math.pow(10, -9);
    public final double ACQUIRE_ENERGY = this.PACKET_SIZE * 50 * Math.pow(10, -9);
    public final double PROCESS_ENERGY = this.PACKET_SIZE * 30 * Math.pow(10, -9);

    // Variables
    public boolean sleep = false;
    public boolean isDead = false;
    public double residualEnergy = this.INITIAL_NODE_ENERGY;

    public double dataAggregationEnergy = this.PACKET_SIZE * 5 * Math.pow(10, -9);
    public double amplificationEnergyPerArea = this.PACKET_SIZE * 5 * Math.pow(10, -12);

    public ArrayList<Double> neighbors;

    // Finish him /\

    public synchronized double getEnergyExpenditure(boolean isClusterHead) {
        float energyExpenditure = 0;

        if (isClusterHead) {

            double distanceToSync = distanceToSync();

            if (distanceToSync <= this.getCommunicationRadio()) {
                // Transmitting
                energyExpenditure += this.IDLE_STATE_ENERGY + this.PACKET_SIZE * Math.pow(distanceToSync, -4);
                // Receiving
                energyExpenditure += this.getNeighbours().size() * this.IDLE_STATE_ENERGY + this.PACKET_SIZE * Math.pow(distanceToSync, -4);
            } else {
                // Transmitting
                energyExpenditure += this.IDLE_STATE_ENERGY + this.PACKET_SIZE * Math.pow(distanceToSync, -2);
                // Receiving
                energyExpenditure += this.getNeighbours().size() * this.IDLE_STATE_ENERGY + this.PACKET_SIZE * Math.pow(distanceToSync, -2);
            }
        }

        energyExpenditure += (ACQUIRE_ENERGY + PROCESS_ENERGY);

        return energyExpenditure;
    }

    // ??
    public synchronized double distanceToSync() {
        Point here = new Point(this.position.getPosX(), this.position.getPosY());
        Point sync = new Point(Jsensor.getNodeByID(1).getPosition().getPosX(), Jsensor.getNodeByID(1).getPosition().getPosY());
        return here.distance(sync);
    }

    public synchronized double distance(int to) {
        Point here = new Point(this.position.getPosX(), this.position.getPosY());
        Point sync = new Point(Jsensor.getNodeByID(to).getPosition().getPosX(), Jsensor.getNodeByID(to).getPosition().getPosY());
        return here.distance(sync);
    }

    public synchronized void updateResidualEnergy(boolean isClusterHead) {
        this.residualEnergy -= this.getEnergyExpenditure(isClusterHead);
//        System.out.println("residualEnergy: " + this.residualEnergy + " : ID: " + this.getID());
        if (this.residualEnergy < 0) {
            this.residualEnergy = 0;
            this.isDead = true;
            Jsensor.log("Node: " + this.getID() + " is dead.");
        } else {
            this.notification();
        }

    }

    @Override
    public void handleMessages(Inbox inbox) {
        if (!isDead && !sleep) {
            while (inbox.hasMoreMessages()) {

                Message message = inbox.getNextMessage();

                if (message instanceof FloodingMessage) {
                    FloodingMessage floodingMessage = (FloodingMessage) message;

                    if (this.messagesIDs.contains(floodingMessage.getID())) {
                        continue;
                    }

                    this.messagesIDs.add(floodingMessage.getID());

                    if (floodingMessage.getDestination().equals(this)) {
                        Jsensor.log("time: " + Jsensor.currentTime +
                                "\t sensorID: " + this.ID +
                                "\t receivedFrom: " + floodingMessage.getSender().getID() +
                                "\t hops: " + floodingMessage.getHops() +
                                "\t msg: " + floodingMessage.getMsg().concat(this.ID + ""));
                    } else {
                        floodingMessage.setMsg(floodingMessage.getMsg().concat(this.ID + " - "));
                        this.multicast(message);

                    }
                }
                if (message instanceof FloodingMessageControl) {
                    FloodingMessageControl floodingMessage = (FloodingMessageControl) message;

                    if (this.messagesIDs.contains(floodingMessage.getID())) {
                        continue;
                    }
                    this.messagesIDs.add(floodingMessage.getID());
                    this.neighbors.set(floodingMessage.getSender().getID(), floodingMessage.getEnergy());
                    this.multicast(message);
                }
            }
        }
    }

    @Override
    public void onCreation() {
        //initializes the list of messages received by the node.
        this.neighbors = new ArrayList<>(Collections.nCopies(Jsensor.getNumNodes() + 1, 0.0));

        this.messagesIDs = new LinkedList<Long>();
        this.notification();

        //sends the first messages if is one of the selected nodes
        if (this.ID < 10) {
            int time = 10 + this.ID * 10;
            FloodingTimer ft = new FloodingTimer();
            ft.startRelative(time, this);
        }
    }

    public void select() {
        BitChromosome initial = BitChromosome.of(Jsensor.getNumNodes());

        Factory<Genotype<BitGene>> gtf =
                Genotype.of(initial);

        Engine<BitGene, Integer> engine = Engine
                .builder(CustomGlobal::eval, gtf)
                .build();

        Genotype<BitGene> result = engine.stream()
                .limit(500)
                .collect(EvolutionResult.toBestGenotype());
        // GA FINISH

        // UPDATE RESIDUAL ENERGY FOR EACH NODE

        for (int i = 1; i <= result.getChromosome().as(BitChromosome.class).toCanonicalString().length(); i++) {
            FloodingNode node = (FloodingNode) Jsensor.runtime.getSensorByID(i);
            if (node.residualEnergy > 0)
                node.updateResidualEnergy(result.getChromosome().getGene(i - 1).getBit());
        }
        for (int i = 2; i <= result.getChromosome().as(BitChromosome.class).toCanonicalString().length() ; i++) {
//            this.unicast();
            // set wake up or rest
        }
        System.out.println("RESULT:\n" + );
    }

    public void notification() {
        if (!isDead && !sleep) {
            Jsensor.log("time: " + Jsensor.currentTime +
                    "\t sender: " + this.getID() +
                    "\t residualEnergy: " + residualEnergy);
            FloodingMessageControl control = new FloodingMessageControl(residualEnergy, this, 0, this.getID());
            this.multicast(control);
        }
    }

    public void rest() {
        sleep = true;
    }

    public void wakeUp() {
        sleep = false;
    }
}
