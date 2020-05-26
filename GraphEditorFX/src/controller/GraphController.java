package controller;

import controller.dir.DirectionVerifier;
import controller.dir.ConVerifier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;

import java.util.*;

import static model.DistanceMatrix.INFINITY;


public class GraphController {
    private Graph graph;
    private DistanceMatrix distanceMatrix;
    private Matrix adjacencyMatrix;


    public GraphController(Graph graph) {
        this.graph = graph;
        distanceMatrix = new DistanceMatrix(graph);
        adjacencyMatrix = new Matrix(graph);
    }

    public Graph getGraph() {
        return graph;
    }

    public Matrix adjacencyMatrix() {
        return adjacencyMatrix;
    }

    public ObservableList<Node> getNodes() {
        return graph.getNodes();
    }

    public ObservableList<Arc> getArcs() {
        return graph.getArcs();
    }

    public Matrix getAdjacencyMatrix() {
        return adjacencyMatrix;
    }

    public DistanceMatrix getDistanceMatrix() {
        return distanceMatrix;
    }

    public void addNode(Node node) {
        graph.getNodes().add(node);
    }

    public void removeNode(Node node) {
        graph.getNodes().remove(node);

        ObservableList<Arc> arcsToRemove = FXCollections.observableArrayList();

        for (Arc arc : graph.getArcs()) {
            if (arc.getBegin().equals(node) || arc.getEnd().equals(node)) {
                arcsToRemove.add(arc);
            }
        }

        graph.getArcs().removeAll(arcsToRemove);
    }

    public void addArc(Arc arc) {
        graph.getArcs().add(arc);
    }

    public void removeArc(Arc arc) {
        graph.getArcs().remove(arc);
    }

    /*
        Metrics
     */

    // Calculation of a node degree
    public int degreeOf(Node node) {
        int degree = 0;

        for (Arc arc : graph.getArcs()) {
            if (arc.getBegin().equals(node) || arc.getEnd().equals(node)) {
                degree++;
            }
        }

        return degree;
    }

    // Calculation of the nodes' eccentricities
    private Map<Node, Integer> eccentricities() {
        Map<Node, Integer> eccentricities = new HashMap<>();

        int eccentricity;

        for (Node node : distanceMatrix.getDistancesMap().keySet()) {
            eccentricity = 0;

            for (Integer distance : distanceMatrix.getDistancesMap().get(node).values()) {
                if ((distance > eccentricity) && (distance != INFINITY)) {
                    eccentricity = distance;
                }
            }

            eccentricities.put(node, eccentricity);
        }

        return eccentricities;
    }

    // Calculation of a graph diameter
    public int diameter() {
        int diameter = 0;

        for (Integer eccentricity : eccentricities().values()) {
            if ((eccentricity > diameter) && (eccentricity != INFINITY)) {
                diameter = eccentricity;
            }
        }

        return diameter;
    }

    // Calculation of a graph radius
    public int radius() {
        int radius = INFINITY;

        for (Integer eccentricity : eccentricities().values()) {
            if ((eccentricity < radius) && (eccentricity != 0)) {
                radius = eccentricity;
            }
        }

        return radius == INFINITY ? 0 : radius;
    }

    // Taking of graph centers
    public ObservableList<Node> centers() {
        ObservableList<Node> centres = FXCollections.observableArrayList();
        int radius = radius();

        for (Node node : eccentricities().keySet()) {
            if (eccentricities().get(node) == radius) {
                centres.add(node);
            }
        }

        return centres;
    }

    // Check for graph planarity
    public boolean isPlanar() {
        return new DirectionVerifier(graph).verify();
    }

    // Check for graph complete
    public boolean isConnective() {
        try {
            if (!this.getNodes().isEmpty()) {
                if (!this.getArcs().isEmpty()) {
                    for (Node begin : this.getNodes()) {
                        for (Node end : this.getNodes()) {
                            Integer distance = this.getDistanceMatrix().getDistancesMap().get(begin).get(end);

                            if (distance == DistanceMatrix.INFINITY) {
                                return false;
                            }
                        }
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
            return true;
        }catch(NullPointerException ex){return false;}
    }

    // Check is graph a tree
    public boolean isTree() {
        return !graph.containsLoop() && new ConVerifier(graph).verify();
    }

    /*
     *      Other algorithms
     */

    // Finding all of hamiltonian cycles in the graph
    public ObservableList<Path> eulerianCycles() {
        ObservableList<Path> hamiltonianCycles = FXCollections.observableArrayList();

        for (Node begin : graph.getNodes()) {
            ObservableList<Path> cycles = findAllEulerianCyclesFrom(begin);
            for (Path cycleFromThisNode : cycles) {
                if (!hamiltonianCycles.contains(cycleFromThisNode)) {
                    hamiltonianCycles.add(cycleFromThisNode);
                }
            }
        }

        return hamiltonianCycles;
    }

    // Coloring of nodes
    public Map<Node, String> colorizeNodes() {
        return new Colorer(graph).colorizeNodes();
    }

    // Making all nodes adjacent to all nodes
    public void makeComplete() {

    }

    /*
     *      Utility
     */

    // Finds all possible Hamiltonian cycles begins with the node given
    private ObservableList<Path> findAllEulerianCyclesFrom(Node begin) {
        Map<Arc, Boolean> visitedArcs = new HashMap<>();
        ObservableList<Path> hamiltonianCyclesBeginsWithThisNode = FXCollections.observableArrayList();
        Path trackingCycle = new Path();

        for (Arc arc : graph.getArcs()) {
            visitedArcs.put(arc, false);
        }

        dfsEulerianCycle(begin, trackingCycle, visitedArcs, hamiltonianCyclesBeginsWithThisNode,begin, 0, 0);

        return hamiltonianCyclesBeginsWithThisNode;
    }

    private void dfsEulerianCycle(Node begin, Path trackingCycle,
                                  Map<Arc, Boolean> visitedArcs,
                                  ObservableList<Path> hamiltonianCyclesBeginsWithThisNode, Node first, int key, int dKey) {
       // Node first = new Node();
        System.out.println("Key "+ dKey);

        //if((trackingCycle.getPath().size()+dKey)>graph.getArcs().size()) {return;}


        if (key==1){
            //System.out.println("first "+ first.getIdentifier()+ " Begin "+ begin.getIdentifier());

            for (Arc arc : graph.getArcs()) {

                if (arc.getBegin().equals(first) && arc.getEnd().equals(begin)) {
                    visitedArcs.replace(arc, false);
                    //System.out.println("Done");

                }
            }

        }

        System.out.println(trackingCycle.getPath().size() + "   dfdsfsdf  "+ graph.getArcs().size());
        /*for (Node node: trackingCycle.getPath()){
            System.out.println(node.getIdentifier());
        }*/

        System.out.println("   ");
        for (Arc arc : graph.getArcs()) {

            System.out.println(visitedArcs.get(arc));

        }

        System.out.println("   ");

        if ((trackingCycle.getPath().size()+dKey) == (graph.getArcs().size()/*-dKey*/)) {
            System.out.println("size "+(trackingCycle.getPath().size()+dKey));
            if (graph.getArcs().contains(new Arc(trackingCycle.getPath().get(trackingCycle.getPath().size() - 1),
                    trackingCycle.getPath().get(0)))) {

                for (Arc arc : graph.getArcs()) {
                    if (arc.getBegin().equals(trackingCycle.getPath().get(trackingCycle.getPath().size() - 1)) && arc.getEnd().equals(trackingCycle.getPath().get(0))) {

                        if (!visitedArcs.get(arc)) {
                            System.out.println("dnjnjnjn");
                            Path hamiltonianCycle = new Path(trackingCycle);
                            hamiltonianCycle.getPath().add(trackingCycle.getPath().get(0));

                            for (Path cycle : hamiltonianCyclesBeginsWithThisNode) {
                                if (hamiltonianCycle.getPath().contains(cycle.getPath())) {
                                    return;
                                }
                            }

                            hamiltonianCyclesBeginsWithThisNode.add(hamiltonianCycle);

                            return;
                        }
                    }
                }
            }
        }

        for (Node adjacentNode : adjacencyMatrix.adjacentNodesOf(begin)) {
          //  System.out.println(" Begin "+ begin.getIdentifier());
            for (Arc arc : graph.getArcs()) {
                if (arc.getBegin().equals(begin) && arc.getEnd().equals(adjacentNode)) {

                    if (!visitedArcs.get(arc)) {
                        if(!arc.isDirected()){
                            for (Arc dirArc : graph.getArcs()) {
                                if (dirArc.getBegin().equals(arc.getEnd()) && dirArc.getEnd().equals(arc.getBegin())) {
                                    if (!visitedArcs.get(dirArc)) {
                                        visitedArcs.replace(arc, true);
                                        visitedArcs.replace(dirArc, true);
                                        trackingCycle.getPath().add(adjacentNode);
                                        //trackingCycle.getPath().add(adjacentNode);
                                        //dKey++;

                                        dfsEulerianCycle(adjacentNode, trackingCycle, visitedArcs, hamiltonianCyclesBeginsWithThisNode, first, ++key, ++dKey);

                                        dKey--;
                                        visitedArcs.replace(arc, false);
                                        visitedArcs.replace(dirArc, false);
                                        trackingCycle.getPath().remove(trackingCycle.getPath().size() - 1);
                                        //trackingCycle.getPath().remove(trackingCycle.getPath().size() - 1);
                                    }
                                }
                            }
                        }
                        else {
                            visitedArcs.replace(arc, true);
                            trackingCycle.getPath().add(adjacentNode);


                            dfsEulerianCycle(adjacentNode, trackingCycle, visitedArcs, hamiltonianCyclesBeginsWithThisNode, first, ++key, dKey);

                            visitedArcs.replace(arc, false);
                            trackingCycle.getPath().remove(trackingCycle.getPath().size() - 1);

                        }
                    }
                }
            }
        }

    }

















    // Finding all of hamiltonian cycles in the graph
    public ObservableList<Path> pathBetween(Node begin, Node end) {
        ObservableList<Path> hamiltonianCycles = FXCollections.observableArrayList();


        ObservableList<Path> cycles = findAllPathBetween(begin, end);
        for (Path cycleFromThisNode : cycles) {
            if (!hamiltonianCycles.contains(cycleFromThisNode)) {
                hamiltonianCycles.add(cycleFromThisNode);
            }
        }


        return hamiltonianCycles;
    }

    // Finds all possible Hamiltonian cycles begins with the node given
    private ObservableList<Path> findAllPathBetween(Node begin, Node end) {
        Map<Node, Boolean> visitedNodes = new HashMap<>();
        ObservableList<Path> hamiltonianCyclesBeginsWithThisNode = FXCollections.observableArrayList();
        Path trackingCycle = new Path();

        for (Node node : graph.getNodes()) {
            visitedNodes.put(node, false);
        }

        dfsPathBetween(begin, end, trackingCycle, visitedNodes, hamiltonianCyclesBeginsWithThisNode, 0);

        return hamiltonianCyclesBeginsWithThisNode;
    }

    private void dfsPathBetween(Node begin,Node end, Path trackingCycle,
                                     Map<Node, Boolean> visitedNodes,
                                     ObservableList<Path> hamiltonianCyclesBeginsWithThisNode, int key) {

        if (begin == end) {

                Path hamiltonianCycle = new Path(trackingCycle);
                


                for (Path cycle : hamiltonianCyclesBeginsWithThisNode) {
                    if (hamiltonianCycle.getPath().contains(cycle.getPath())) {
                        return;
                    }
                }

                hamiltonianCyclesBeginsWithThisNode.add(hamiltonianCycle);

                return;

        }

        for (Node adjacentNode : adjacencyMatrix.adjacentNodesOf(begin)) {
            if (!visitedNodes.get(adjacentNode)) {

                if (key==0){
                    visitedNodes.replace(begin, true);
                    trackingCycle.getPath().add(begin);
                    key++;
                }

                visitedNodes.replace(adjacentNode, true);
                trackingCycle.getPath().add(adjacentNode);



                dfsPathBetween(adjacentNode, end, trackingCycle, visitedNodes, hamiltonianCyclesBeginsWithThisNode, 1);

                visitedNodes.replace(adjacentNode, false);
                trackingCycle.getPath().remove(trackingCycle.getPath().size() - 1);
            }
        }
    }




















}
