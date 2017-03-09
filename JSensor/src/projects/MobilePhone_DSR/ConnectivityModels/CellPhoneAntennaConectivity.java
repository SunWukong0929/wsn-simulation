package projects.MobilePhone_DSR.ConnectivityModels;

import jsensor.nodes.Node;
import jsensor.nodes.models.ConnectivityModel;
import projects.MobilePhone_DSR.Nodes.Antenna;
import projects.MobilePhone_DSR.Nodes.CellPhone;

/**
 *
 * @author Matheus
 */
public class CellPhoneAntennaConectivity extends ConnectivityModel
{
    @Override
    public boolean isConnected(Node from, Node to) 
    {
    	if((from instanceof Antenna) && (to instanceof CellPhone))
    		return true;
    	else if ((from instanceof CellPhone) && (to instanceof Antenna))
    		return true;
    	else if((from instanceof Antenna) && (to instanceof Antenna))
    		return true;
    	else
    		return false;
    }
}
