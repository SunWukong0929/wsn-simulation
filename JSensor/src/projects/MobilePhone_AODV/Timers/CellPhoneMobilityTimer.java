package projects.MobilePhone_AODV.Timers;

import java.util.Hashtable;

import jsensor.nodes.Node;
import jsensor.nodes.models.MobilityModel;
import jsensor.runtime.Jsensor;
import jsensor.utils.Position;

public class CellPhoneMobilityTimer extends MobilityModel
{	
	private Hashtable<Integer, Integer> nodes;
	
	public CellPhoneMobilityTimer(){
		this.nodes = new Hashtable<>();
	}
	
	public CellPhoneMobilityTimer(Hashtable<Integer, Integer> nodes){
		this.nodes = nodes;
	}
	
    @Override
    public MobilityModel clone() {
        return new CellPhoneMobilityTimer(this.nodes);
    }

    @Override
    public Position getNextPosition(Node n) 
    {
    	return nextMoviment(n);
    }
   
    public Position nextMoviment(Node n){
    	int x = n.getPosition().getPosX();
    	int y = n.getPosition().getPosY();
    	int direction, newDirection = 0;
    	
    	if(nodes.containsKey(n.getID())){
    		direction = nodes.get(n.getID());
    		
    		//if is stopped.
    		if(direction == 8){
    			//probability of remain stopped = 0.8
    			if(n.getRandom().nextDouble() < 0.8)
    				return n.getPosition();
    		}
    		//if is walking.
    		else{
    			//probability of stop = 0.2
    			if(n.getRandom().nextDouble() < 0.2){
    				nodes.put(n.getID(), 8);
    				return n.getPosition();
    			}
    		}
    		
    		//probability of change the moviment
        	if(n.getRandom().nextDouble() > 0.8){
        		do{
        			newDirection = n.getRandom().nextInt(8);
        			
        		}while(direction == newDirection);	
        	}	
    	}
    	
    	direction = needChange(n, x, y, newDirection);
    	
    	nodes.put(n.getID(), direction);
    
    	switch(direction){
    		case 0:{ //System.out.println(n.getPosition().getPosX()+","+ n.getPosition().getPosY() + 1); 
    		return new Position(n.getPosition().getPosX(), n.getPosition().getPosY() + 1);}
    		case 1:{ //System.out.println(n.getPosition().getPosX() + 1 +","+ n.getPosition().getPosY() + 1); 
    		return new Position(n.getPosition().getPosX() + 1, n.getPosition().getPosY() + 1);}
    		case 2:{ //System.out.println(n.getPosition().getPosX() + 1 +","+ n.getPosition().getPosY()); 
    		return new Position(n.getPosition().getPosX() + 1, n.getPosition().getPosY());}
    		case 3:{ //System.out.println((n.getPosition().getPosX() + 1) +","+ (n.getPosition().getPosY() - 1)); 
    		return new Position(n.getPosition().getPosX() + 1, n.getPosition().getPosY() - 1);}
    		case 4:{ //System.out.println((n.getPosition().getPosX()) +","+ (n.getPosition().getPosY() - 1)); 
    		return new Position(n.getPosition().getPosX(), n.getPosition().getPosY() - 1);}
    		case 5:{ //System.out.println((n.getPosition().getPosX()) - 1 +","+ (n.getPosition().getPosY() - 1)); 
    		return new Position(n.getPosition().getPosX() - 1, n.getPosition().getPosY() - 1);}
    		case 6:{ //System.out.println(n.getPosition().getPosX() - 1 +","+ n.getPosition().getPosY()); 
    		return new Position(n.getPosition().getPosX() - 1, n.getPosition().getPosY());}
    		case 7:{ //System.out.println(n.getPosition().getPosX() - 1 +","+ n.getPosition().getPosY() + 1); 
    		return new Position(n.getPosition().getPosX() - 1, n.getPosition().getPosY() + 1);}		
    	}
		return null;
    }
    
    public int needChange(Node n, int x, int y, int direction){
    	//checks if the position have to change
    	if(x == 0){
    		if(y == 0){
    			direction = n.getRandom().nextInt(3);
    		}
    		else if(y == Jsensor.getDimY()){
    			direction = 3 + n.getRandom().nextInt(3);
    		}
    		else{
    			if(direction >= 5 && direction <=7){
    				direction = n.getRandom().nextInt(5);
    			}
    		}
    	}
    	else if(x == Jsensor.getDimX()){
    		if(y == Jsensor.getDimY()){
    			direction = 4 + n.getRandom().nextInt(3);
    		}
    		else{
    			if(direction >= 1 && direction <= 3){
    				direction = n.getRandom().nextInt(5);
    				if(direction != 0)
    					direction += 3;
    			}
    		}
    	}
    	
    	if(y == 0){
    		if(x == Jsensor.getDimX()){
    			direction = n.getRandom().nextInt(3);
    			if(direction != 0)
    				direction += 5;
    		}
    		else{
    			if(direction >= 3 && direction <= 5){
    				direction = n.getRandom().nextInt(5);
    				if(direction == 3)
    					direction += 3;
    				else if(direction == 4)
    					direction += 3;
    			}
    		}
    	}
    	else if(y == Jsensor.getDimY()){
    		if(direction ==0 || direction == 1 || direction == 7){
    			direction = 2 + n.getRandom().nextInt(5);
    		}
    	}
    	return direction;
    }
}

