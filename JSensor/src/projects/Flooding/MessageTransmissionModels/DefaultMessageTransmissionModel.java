package projects.Flooding.MessageTransmissionModels;

import jsensor.nodes.Node;
import jsensor.nodes.messages.Message;
import jsensor.nodes.models.MessageTransmissionModel;

public class DefaultMessageTransmissionModel extends MessageTransmissionModel{

	@Override
	public float timeToReach(Node startSensor, Node endSensor, Message msg) {
		return 9;
	}

}
