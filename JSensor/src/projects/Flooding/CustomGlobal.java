package projects.Flooding;

import jsensor.runtime.AbsCustomGlobal;

/**
 *
 * @author danniel & Matheus
 */
public class CustomGlobal extends AbsCustomGlobal{
	
    @Override
    public boolean hasTerminated() {
        return false;
    }
    
    @Override
    public void preRun() {
    }

    @Override
    public void preRound() {
    }

    @Override
    public void postRound() {
    }

	@Override
	public void postRun() {
	}
}
