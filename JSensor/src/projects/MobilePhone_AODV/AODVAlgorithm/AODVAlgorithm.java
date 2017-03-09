package projects.MobilePhone_AODV.AODVAlgorithm;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.SortedSet;

import jsensor.nodes.Node;
import jsensor.runtime.Jsensor;
import projects.MobilePhone_AODV.AODVAlgorithm.AODVRouteRequest;
import projects.MobilePhone_AODV.AODVAlgorithm.AODVUtils.AODVConstants;
import projects.MobilePhone_AODV.AODVAlgorithm.AODVNode;

public class AODVAlgorithm {        
    public boolean routeDiscovery(AODVNode sourceNode, int destNodeID) {
        SortedSet<Node> neighbourList;
        Queue<AODVNode> queue = new LinkedList<AODVNode>();
        HashSet<AODVNode> visitedNodes = new HashSet<AODVNode>();
        
        // Marks sourceNode as visited
        visitedNodes.add(sourceNode);
        // Inserts source node in the queue
        queue.add(sourceNode);
        // Initializes field that represents RREQ
        sourceNode.setRreq(new AODVRouteRequest(sourceNode.getID(), destNodeID, 0));

        while (queue.size() > 0) {
            AODVNode node = queue.poll();
            
            if (destNodeID == node.getID()) {
                // Dequeued node is the destination node            
                routeReply(sourceNode, (AODVNode) Jsensor.getNodeByID(destNodeID), node);
                
                return true;
            } else if (node.enableForwarding == true) {
                // Search for a route in node's cache
                AODVRouteEntry cachedRoute = ((AODVNode) node).getRouteCache().getRouteEntry(destNodeID);

                if (cachedRoute != null && node.getID() != sourceNode.getID() && cachedRoute.getNext() != node.getPreviousNode().getID()) {                                        
                    // Updates ttl field of route entry
                    long ttl = node.getRouteCache().getRouteEntry(destNodeID).getTtl();
                    node.getRouteCache().getRouteEntry(destNodeID).setTTL(ttl + AODVConstants.getActiveRouteTime());
                    
                    routeReply(sourceNode, (AODVNode) Jsensor.getNodeByID(destNodeID), node);
                    
                    return true;                    
                } else {
                    neighbourList = node.getNeighbours().getNodesList();

                    for (Node neighbour : neighbourList) {
                        if (visitedNodes.contains(neighbour) == false) {
                            visitedNodes.add((AODVNode) neighbour);
                            queue.add((AODVNode) neighbour);                            

                            // Route Request
                            AODVRouteRequest rreq = new AODVRouteRequest(sourceNode.getID(), destNodeID, node.getRreq().getHopCount() + 1);                            
                            ((AODVNode) neighbour).setRreq(rreq);

                            // Updates neighbour's cache
                            AODVRouteEntry re = new AODVRouteEntry(sourceNode.getID(), node.getID(), node.getRreq().getHopCount() + 1, AODVMessageTransmissionModel.timeToReach() * AODVConstants.getActiveRouteTime() + Jsensor.currentTime);
                            ((AODVNode) neighbour).getRouteCache().storeRouteEntry(sourceNode.getID(), re);
                                                       
                            // Sets cached route's remove time
                            setRouteTtlTimer(((AODVNode) neighbour), sourceNode.getID());
                            
                            ((AODVNode) neighbour).setPreviousNode(node);                                                                                   
                        }
                    }
                }
            }
        }

        return false;
    }    

    public boolean sendMessage(AODVNode from, AODVNode to, AODVMessage msg) {
        if (from.isNeighbour(to)) {
            from.unicast(msg, to);

            // Message was sent
            return true;
        } else {
            // Message was not sent
            return false;
        }
    }
    
    public void newRouteDiscovery(AODVNode node, AODVMessage msg) {
        AODVResendTimer rt = new AODVResendTimer();
        rt.setMessage(msg);
        rt.startRelative(1, node);                       
    }
    
    public void removeRouteFromPrecursors(AODVNode node, int destination) {
        SortedSet<Node> neighbourList = node.getNeighbours().getNodesList();
        
        for (Node neighbour : neighbourList)
            ((AODVNode) neighbour).getRouteCache().removeRoute(destination);
    }       
    
    private void routeReply(AODVNode sourceNode, AODVNode destNode, AODVNode node) {
        AODVNode previousNode = node.getPreviousNode();
        int nextNode = node.getID();
                
        for (int i = 0; i < node.getRreq().getHopCount(); i++) {
            AODVRouteEntry re = new AODVRouteEntry(destNode.getID(), nextNode, i + 1, AODVMessageTransmissionModel.timeToReach() * AODVConstants.getActiveRouteTime() + Jsensor.currentTime);
            previousNode.getRouteCache().storeRouteEntry(destNode.getID(), re);
            setRouteTtlTimer(previousNode, destNode.getID());
            nextNode = previousNode.getID();
            previousNode = previousNode.getPreviousNode();
        }
    }
    
    public void setRouteTtlTimer(AODVNode node, int destination) {
        /*
        AODVRemoveRouteEntryTimer timer = new AODVRemoveRouteEntryTimer();                
        timer.setEntryToRemove(destination);
        timer.startRelative(AODVConstants.getActiveRouteTime(), node);
        */        
    }
}
