package projects.MobilePhone_DSR.DSRAlgorithm;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.SortedSet;

import projects.MobilePhone_DSR.DSRAlgorithm.DSRMessage;
import projects.MobilePhone_DSR.DSRAlgorithm.DSRNode;
import projects.MobilePhone_DSR.DSRAlgorithm.DSRResendTimer;
import projects.MobilePhone_DSR.DSRAlgorithm.DSRUtils.DSRConstants;
import jsensor.nodes.Node;
import jsensor.runtime.Jsensor;

public class DSRAlgorithm {
    public DSRRoute routeDiscovery(DSRNode sourceNode, int destNodeID) {
        long ttl = DSRMessageTransmissionModel.timeToReach() * DSRConstants.getActiveRouteTime();
        SortedSet<Node> neighbourList;
        Queue<DSRNode> queue = new LinkedList<DSRNode>();
        HashSet<DSRNode> visitedNodes = new HashSet<DSRNode>();
        LinkedList<Integer> route = null;

        sourceNode.getForwardRoute().get().clear();
        sourceNode.getForwardRoute().get().add(sourceNode.getID());
                
        // Marks sourceNode as visited
        visitedNodes.add(sourceNode);
        // Inserts source node in the queue
        queue.add(sourceNode);

        while (queue.size() > 0) {
            DSRNode node = queue.poll();

            if (destNodeID == node.getID()) {
                // Dequeued node is the destination node
                route = node.getForwardRoute().get();               
                
                return new DSRRoute(route, DSRMessageTransmissionModel.timeToReach() *
                                    DSRConstants.getActiveRouteTime() + Jsensor.currentTime);
            } else if (node.enableForwarding == true) {
                // Search for a route in node's cache
                DSRRoute cachedRoute = ((DSRNode) node).getRouteCache().getRoute(destNodeID);

                if (cachedRoute != null) {
                    // Dequeued node has a route to destination
                    route = node.getForwardRoute().get();                    
                    // To avoid duplicates when combining routes, last ID must not be included
                    route.removeLast();                                                           
                    // Concatenates forward route with cached route
                    route.addAll(cachedRoute.getRoute());

                    if (hasDuplicates(new DSRRoute(route, 0L)) == false) {
                        // Updates cached route's ttl field
                        cachedRoute.setTtl(cachedRoute.getTtl() +
                                           DSRMessageTransmissionModel.timeToReach() *
                                           DSRConstants.getActiveRouteTime());
                                                
                        return new DSRRoute(route, ttl);
                    } else {
                        route = null;
                    }
                } else {
                    neighbourList = node.getNeighbours().getNodesList();

                    for (Node neighbour : neighbourList) {
                        if (visitedNodes.contains(neighbour) == false) {
                            visitedNodes.add((DSRNode) neighbour);
                            queue.add((DSRNode) neighbour);

                            ((DSRNode) neighbour).getForwardRoute().get().clear();

                            // Updates route that will be forwarded
                            ttl += Jsensor.currentTime;
                            DSRRoute fwRoute = new DSRRoute(node.getForwardRoute().get(), ttl);
                            fwRoute.getRoute().add(neighbour.getID());
                            ((DSRNode) neighbour).setForwardRoute(fwRoute);
                            
                            // Updates neighbour with route to sourceNode
                            DSRRoute r = new DSRRoute(revertRoute(fwRoute));
                            ((DSRNode) neighbour).getRouteCache().storeRoute(sourceNode.getID(), r);
                            // Timer to remove route
                            setRouteTtlTimer((DSRNode) neighbour, sourceNode.getID());
                        }
                    }
                }
            }
        }

        return null;
    }

    public DSRRoute revertRoute(DSRRoute route) {
        Iterator<Integer> ite = route.getRoute().descendingIterator();

        DSRRoute revertedRoute = new DSRRoute();

        while (ite.hasNext() == true)
            revertedRoute.getRoute().add(ite.next());

        return revertedRoute;
    }

    private boolean hasDuplicates(DSRRoute route) {
        DSRRoute sortedRoute = new DSRRoute(sortRoute(route));

        for (int i = 0; i < sortedRoute.getRoute().size() - 1; i++) {
            if (sortedRoute.getRoute().get(i + 1).intValue() == sortedRoute.getRoute().get(i).intValue())                
                return true;
        }

        return false;
    }

    private DSRRoute sortRoute(DSRRoute route) {
        DSRRoute sortedRoute = new DSRRoute(route);
        sortedRoute.getRoute().sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer var, Integer var2) {
                if (var.intValue() < var2.intValue())
                    return -1;
                else if (var.intValue() > var2.intValue())
                    return 1;
                else
                    return 0;
            }
        });

        return sortedRoute;
    }

    public DSRNode nextNode(int nodeID, DSRRoute route) {
        int index;

        index = route.getRoute().indexOf(nodeID) + 1;

        return (DSRNode) Jsensor.getNodeByID(route.getRoute().get(index));
    }

    public DSRNode previousNode(int nodeID, DSRRoute route) {
        int index;

        index = route.getRoute().indexOf(nodeID) - 1;

        return (DSRNode) Jsensor.getNodeByID(route.getRoute().get(index));
    }

    public boolean sendMessage(DSRNode from, DSRNode to, DSRMessage msg) {
        if (from.isNeighbour(to)) {
            from.unicast(msg, to);

            // Message was sent
            return true;
        } else {
            // Message was not sent
            return false;
        }
    }

    public void routeError(int lastValidLink, DSRRoute route) {
        long ttl;
        int index = route.getRoute().indexOf(lastValidLink);
        
        DSRNode node = null;        
        
        for (int i = index - 1; i >= 0; i--) {
            node = ((DSRNode) Jsensor.getNodeByID(route.getRoute().get(i)));
            
            if (route.getRoute() != null)
                node.getRouteCache().removeRoute(route.getRoute().getLast());
            
            ttl = DSRMessageTransmissionModel.timeToReach() * DSRConstants.getActiveRouteTime() + Jsensor.currentTime;
            DSRRoute newRoute = new DSRRoute(route.getRoute().subList(i, index + 1), ttl);            
            node.getRouteCache().storeRoute(lastValidLink, newRoute);
            // Timer to remove route
            setRouteTtlTimer(node, lastValidLink);
        }
    }
    
    public boolean destinationNodeFound(int currentNodeID, int lastNodeID) {
        return currentNodeID == lastNodeID;
    }
    
    public void newRouteDiscovery(DSRNode node, DSRMessage msg) {
        DSRResendTimer rt = new DSRResendTimer();
        rt.setMessage(msg);
        rt.startRelative(1, node);                        
    }
    
    public void setRouteTtlTimer(DSRNode node, int destination) {
        /*
        DSRRemoveRouteEntryTimer timer = new DSRRemoveRouteEntryTimer();                
        timer.setEntryToRemove(destination);
        timer.startRelative(DSRConstants.getActiveRouteTime(), node);
        */
    }
}
