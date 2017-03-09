package projects.Flooding.ReliabilityModels;

import jsensor.nodes.messages.Packet;
import jsensor.nodes.models.ReliabilityModel;

public class ReliableDelivery extends ReliabilityModel{

	@Override
	public boolean reachesDestination(Packet p) {
		return true;
	}

}
