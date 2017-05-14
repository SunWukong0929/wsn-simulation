package projects.Flooding;

import jsensor.runtime.AbsCustomGlobal;
import jsensor.runtime.Jsensor;
import org.jenetics.BitChromosome;
import org.jenetics.BitGene;
import org.jenetics.Genotype;
import projects.Flooding.Sensors.FloodingNode;

/**
 * @author danniel & Matheus
 */

public class CustomGlobal extends AbsCustomGlobal {

    public synchronized static Integer eval(Genotype<BitGene> gt) {

        BitChromosome thisChrom = gt.getChromosome().as(BitChromosome.class);

        double fitness = 0;

        double extremeEnergyExpenditure = 0;
        double expectedEnergyExpenditure = 0;
        double sumDistances = 0;

        for (int i = 1; i <= thisChrom.length(); i++) {
//            System.out.println(i);

            FloodingNode node = (FloodingNode) Jsensor.runtime.getSensorByID(i);

//            extremeEnergyExpenditure += node.getEnergyExpenditure(true);
//            expectedEnergyExpenditure += node.getEnergyExpenditure(thisChrom.get(i));
            sumDistances += node.distanceToSync();
//
            fitness += node.residualEnergy / node.INITIAL_NODE_ENERGY;
        }

//        fitness += extremeEnergyExpenditure / expectedEnergyExpenditure;
        fitness += 1 / sumDistances;


        // IMPORTANTE É SSO AUQI
        return (int) Math.pow(fitness, 12);
    }

    @Override
    public boolean hasTerminated() {
        return false;
    }

    @Override
    public void preRun() {
        ((FloodingNode) Jsensor.getNodeByID(1)).residualEnergy = 9999.0f;
    }

    @Override
    public void preRound() {
        end();
        send();
        ((FloodingNode) Jsensor.getNodeByID(1)).select();

    }


    @Override
    public void postRound() {

    }

    @Override
    public void postRun() {

    }

    private void send() {
        for (int i = 0; i < Jsensor.getNumNodes(); i++) {
            if (Math.random() * 100 < 30) {

            }
        }

    }

    private boolean end() {
        for (int i = 2; i <= Jsensor.getNumNodes(); i++) {
            if (!((FloodingNode) Jsensor.getNodeByID(i)).isDead)
                return true;
        }
        Jsensor.runtime.setAbort(true);
        return false;
    }
}
