package projects.MobilePhone_AODV.Facts;

import jsensor.nodes.events.EventModel;
import jsensor.nodes.monitoring.CellModel;
import projects.MobilePhone_AODV.CellModels.MyCell;

enum climates{
	sun,
	cloud,
	rain,
	rainWithThunders
}

public class ClimateAbstractFact extends EventModel{
	int previousTimeClimate;
	climates previousClimate;
	
	public ClimateAbstractFact(){}
	
	public ClimateAbstractFact(int previousTimeClimate, climates previousClimate){
		this.previousTimeClimate = previousTimeClimate;
		this.previousClimate = previousClimate;
	}
	
	@Override
	public EventModel clone() {
		return new ClimateAbstractFact(this.previousTimeClimate, this.previousClimate);
	}

	@Override
	public CellModel setValue(CellModel cell) {
		Float evaporationUnit;
		Float rainUnit;
		double x;
		climates Climate = climates.sun;		
		
		if(previousClimate == climates.sun && previousTimeClimate < 60){
			
			previousClimate = climates.sun;
			previousTimeClimate ++;
			
			x = getRandom(cell.getChunk()).nextInt(10);//0(inclusive) e 10 (exclusive)
			//System.out.println("---------"+ x +"----------");
			if(x < 5){
				Climate = climates.sun;
			}else if(x >= 5 && x < 7){
				Climate = climates.cloud;
				previousTimeClimate = 60;
			}else if(x >= 7 && x < 9){
				Climate = climates.rain;
				previousTimeClimate = 60;
			}else{
				Climate = climates.rainWithThunders;
				previousTimeClimate = 60;
			}
			
		}else if(previousClimate == climates.cloud && previousTimeClimate < 90){
			previousClimate = climates.cloud;
			previousTimeClimate ++;
			x = getRandom(cell.getChunk()).nextInt(10);
			if(x < 5){
				Climate = climates.cloud;
			}else if(x >= 5 && x < 7){
				Climate = climates.rain;
				previousTimeClimate = 90;
			}else if(x >= 7 && x < 9){
				Climate = climates.sun;
				previousTimeClimate = 0;
			}else{
				Climate = climates.rainWithThunders;
				previousTimeClimate = 90;
			}
			
		}else if(previousClimate == climates.rain && previousTimeClimate < 95){
			previousClimate = climates.rain;
			previousTimeClimate ++;
			x = getRandom(cell.getChunk()).nextInt(10);
			if(x < 5){
				Climate = climates.rain;
			}else if(x >= 5 && x < 7){
				Climate = climates.cloud;
				previousTimeClimate = 60;
			}else if(x >= 7 && x < 9){
				Climate = climates.rainWithThunders;
			}else{
				Climate = climates.sun;
				previousTimeClimate = 0;
			}
			
		}else {
			previousTimeClimate = 0;
			previousClimate = climates.sun;
			x = getRandom(cell.getChunk()).nextInt(10);
			if(x < 5)
				Climate = climates.sun;
			else if(x >= 5 && x < 7)
				Climate = climates.cloud;
			else if(x >= 7 && x < 9)
				Climate = climates.rain;
			else Climate = climates.rainWithThunders;
		}
		
		switch (Climate){
		case sun:
			evaporationUnit = new Float(getRandom(cell.getChunk()).nextInt(20));
			
			//add new count of water
			if(((MyCell)cell).getWater() - evaporationUnit > 0)
				((MyCell)cell).setWater(((MyCell)cell).getWater() - evaporationUnit);
			else ((MyCell)cell).setWater(new Float(0));
			break;
			
		case cloud:
			evaporationUnit = new Float(getRandom(cell.getChunk()).nextInt(10));
							
			//add new count of water
			if(((MyCell)cell).getWater() - evaporationUnit > 0)
				((MyCell)cell).setWater(((MyCell)cell).getWater() - evaporationUnit);
			else ((MyCell)cell).setWater(new Float(0));
			break;
			
		case rain:
			rainUnit = new Float(getRandom(cell.getChunk()).nextInt(20));
			
			//add new count of water
			((MyCell)cell).setWater(((MyCell)cell).getWater() + rainUnit);
			break;
			
		case rainWithThunders:
			//System.out.println("-------------------");
			rainUnit = new Float(getRandom(cell.getChunk()).nextInt(25));
			
			//add new count of water
			((MyCell)cell).setWater(((MyCell)cell).getWater() + rainUnit);
			//Not all the cells are struck by thunder[between 18 and 20]
			if(rainUnit > 23)
				((MyCell)cell).setThunder(true);
			break;
		default:
			System.out.println("Invalide Climate!");
		}		
		return cell;
	}	
}
