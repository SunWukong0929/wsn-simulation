package projects.Flooding.MessageTransmissionModels;

import jsensor.nodes.Node;
import jsensor.nodes.messages.Message;
import jsensor.nodes.models.MessageTransmissionModel;
import projects.Flooding.Messages.FloodingMessage;
import projects.Flooding.Messages.FloodingMessageControl;
import projects.Flooding.Messages.HeaderControl;

public class DefaultMessageTransmissionModel extends MessageTransmissionModel{

	@Override
	public float timeToReach(Node startSensor, Node endSensor, Message msg) {
		if(msg instanceof FloodingMessage)
			return 9;
		if(msg instanceof FloodingMessageControl)
			return 1;
		if(msg instanceof HeaderControl)
			return 10;
		return 9999;
	}


}
