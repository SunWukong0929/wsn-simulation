package projects.MobilePhone_AODV.ConnectivityModels;

import jsensor.nodes.Node;
import jsensor.nodes.models.ConnectivityModel;
import projects.MobilePhone_AODV.Nodes.Antenna;
import projects.MobilePhone_AODV.Nodes.CellPhone;

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
