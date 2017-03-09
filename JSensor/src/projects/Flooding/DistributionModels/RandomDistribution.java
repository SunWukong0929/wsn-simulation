package projects.Flooding.DistributionModels;

import jsensor.nodes.Node;
import jsensor.nodes.models.DistributionModelNode;
import jsensor.utils.Configuration;
import jsensor.utils.Position;

/**
 *
 * @authorMatheus
 */
public class RandomDistribution extends DistributionModelNode
{
	 @Override
	    public Position getPosition(Node s) {
	        return new Position(s.getRandom().nextInt(Configuration.dimX), s.getRandom().nextInt(Configuration.dimY));
	    }
}
