package projects.Flooding.Timers;

import jsensor.runtime.Jsensor;
import jsensor.nodes.Node;
import jsensor.nodes.events.TimerEvent;
import jsensor.utils.GenerateFilesOmnet;
import projects.Flooding.Messages.FloodingMessage;


/**
 *
 * @author Danniel & Matheus
 */
public class FloodingTimer extends TimerEvent{

    @Override
    public void fire() {
    	
    	Node destination = this.node.getRandomNode("FloodingNode");
    	while(true){
    		if(destination == null) {
                destination = this.node.getRandomNode("FloodingNode");
                continue;
            }     
            
    		if(this.node == destination){
    			destination = this.node.getRandomNode("FloodingNode");
                continue;
    		}
    		break;
    	}
        
        FloodingMessage message = new FloodingMessage(this.node, destination, 0, ""+this.node.getID(), this.node.getChunk());
        
        String messagetext = "Created by the sensor: "+Integer.toString(this.node.getID()) + " Path: ";
        
        message.setMsg(messagetext);
    	Jsensor.log("time: "+ Jsensor.currentTime +"\t sensorID: "+this.node.getID()+ "\t sendTo: " +destination.getID());
		
    	GenerateFilesOmnet.addStartNode(this.node.getID(), destination.getID(), Jsensor.currentTime);
	    this.node.multicast(message);
    }    
}
