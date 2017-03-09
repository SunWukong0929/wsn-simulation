package projects.Flooding.ConnectivityModels;

import jsensor.nodes.Node;
import jsensor.nodes.models.ConnectivityModel;


/**
 *
 * @author danniel & Matheus
 */
public class FloodingNodeConectivity extends ConnectivityModel
{
    @Override
    public boolean isConnected(Node from, Node to) {
        return true;
    }
}
