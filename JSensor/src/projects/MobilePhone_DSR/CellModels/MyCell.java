package projects.MobilePhone_DSR.CellModels;

import jsensor.nodes.monitoring.DefaultJsensorCell;

public class MyCell extends DefaultJsensorCell{
	private boolean thunder;
	private Float water;
	
	public Float getWater() {
		return water;
	}

	public void setWater(Float water) {
		this.water = water;
	}

	public MyCell(){
		this.thunder = false;
		this.water = new Float(0);
	}
	
	public MyCell(boolean thunder, Float water){
		this.thunder = thunder;
		this.water = water;
		
	}

	public boolean getThunder() {
		return this.thunder;
	}
	
	public void setThunder(boolean thunder) {
		this.thunder = thunder;
	}

	@Override
	public DefaultJsensorCell clone() {
		return new MyCell(this.thunder, this.water);
	}
}
