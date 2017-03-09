package projects.MobilePhone_AODV.CellModels;

import jsensor.nodes.Node;
import jsensor.nodes.monitoring.CellModel;
import jsensor.nodes.monitoring.HandleCells;
import jsensor.runtime.Jsensor;

public class SetFactCell extends HandleCells {    
	@Override
	public void fire(Node node, CellModel cell) {
		cell = (MyCell) Jsensor.getCell(node.getPosition());
    	if(((MyCell)cell).getThunder()){
    		((MyCell)cell).setThunder(false);
    		Jsensor.setCell(cell);
    		node.deactivate(node.getRandom().nextInt(50));
    		
			Jsensor.log(">>>>> Antenna "+node.getID()+" struck by Thunder!");
    	}
    	
    	if(((MyCell)cell).getWater() > 120){
    	    node.deactivate(node.getRandom().nextInt(50));
    		
			Jsensor.log(">>>>> Antenna "+node.getID()+" is full of water!");
    	}
	}
	
}
