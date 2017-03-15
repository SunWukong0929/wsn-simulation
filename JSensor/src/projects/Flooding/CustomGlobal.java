package projects.Flooding;

import jsensor.runtime.AbsCustomGlobal;
import jsensor.runtime.Jsensor;
import projects.Flooding.Sensors.FloodingNode;
import org.jenetics.BitChromosome;
import org.jenetics.BitGene;
import org.jenetics.Genotype;
import org.jenetics.engine.Engine;
import org.jenetics.engine.EvolutionResult;
import org.jenetics.util.Factory;

/**
 * @author danniel & Matheus
 */

public class CustomGlobal extends AbsCustomGlobal {

    private synchronized static Integer eval(Genotype<BitGene> gt) {

        BitChromosome thisChrom = gt.getChromosome().as(BitChromosome.class);

        double fitness = 0;

        double extremeEnergyExpenditure = 0;
        double expectedEnergyExpenditure = 0;
        double sumDistances = 0;

        for (int i = 1; i <= gt.length(); i++) {
            FloodingNode node = (FloodingNode) Jsensor.runtime.getSensorByID(i);

            extremeEnergyExpenditure += node.getEnergyExpenditure(true);
            expectedEnergyExpenditure += node.getEnergyExpenditure(thisChrom.get(i));
            sumDistances += node.distanceToSync();

            fitness += node.residualEnergy/node.INITIAL_NODE_ENERGY;
        }

        fitness += extremeEnergyExpenditure/expectedEnergyExpenditure;
        fitness += 1/sumDistances;

        // DESCONSIDERE \/
        fitness = Math.pow(fitness, 12);
        int fitnessNormalized = (int) fitness;
        Integer fit = (Integer) fitnessNormalized;

        // IMPORTANTE É SSO AUQI
        return fit;
    }

    @Override
    public boolean hasTerminated() {
        return false;
    }
    
    @Override
    public void preRun() {

    }

    @Override
    public void preRound() {

        // GA START
        BitChromosome initial = BitChromosome.of(1000);

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

        for (int i = 1; i <= result.length(); i++) {
            FloodingNode node = (FloodingNode) Jsensor.runtime.getSensorByID(i);
            node.updateResidualEnergy(result.getChromosome().getGene(i-1).getBit());
        }
        System.out.println("Residual Energy First Node:\n" + ((FloodingNode) Jsensor.runtime.getSensorByID(1)).residualEnergy);
        System.out.println("RESULT:\n" + result.getChromosome().as(BitChromosome.class).toCanonicalString());

    }

    @Override
    public void postRound() {

    }

	@Override
	public void postRun() {
    }
}
