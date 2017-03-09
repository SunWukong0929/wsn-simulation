package projects.MobilePhone_AODV.DistributionModels;

import jsensor.nodes.models.DistributionModelEvent;
import jsensor.nodes.monitoring.Event;
import jsensor.utils.Configuration;
import jsensor.utils.Position;

public class Centralized extends DistributionModelEvent{

	@Override
	public Position getPosition(Event e) {
		return new Position(Configuration.dimX/2, Configuration.dimY/2);
	}

}
