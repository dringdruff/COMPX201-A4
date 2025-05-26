import java.util.*;

/**
 * A graph data structure implemented using an adjacency list.
 * This class provides methods for adding and removing nodes and edges,
 * checking if an edge exists, getting edges of a specific type, and printing the graph.
 */
public class Graph {

//    public static void main(String[] args) {
//        Graph graph = new Graph();
//
//        graph.addNode("Auckland");
//        graph.addNode("Christchurch");
//
//        graph.addEdge("Auckland", "Christchurch", "Plane");
//
//        System.out.println("Graph:");
//        graph.print();
//
//        System.out.println("Has Edge:" + graph.hasEdge("Auckland", "Christchurch", "Plane"));
//    }


    /** The adjacency list that stores the graph structure. */
    private final Map<Node, List<Edge>> adjacencyList;

    /**
     * Constructs a new empty graph.
     */
    public Graph() {
        adjacencyList = new HashMap<>();
    }


    /**
     * Adds a node to the graph.
     * If a node with the same name exists then it will not be added again.
     * 
     * @param name the name of the node to add
     */
    public void addNode(String name) {
        // Check that the node is valid
        if (name.isEmpty()) {
            System.out.println("No name provided");
            return;
        }

        // Create a new node
        Node node = new Node(name);
        // Add the node to the adjacency list
        adjacencyList.putIfAbsent(node, new ArrayList<>());
    }


    /**
     * Removes a node and all edges connected to it from the graph.
     * @param name the name of the node to remove
     */
    public void removeNode(String name) {
        Node nodeToRemove = new Node(name); // Create a new node to remove
        adjacencyList.remove(nodeToRemove); // Remove the node

        // Remove edges pointing to the node
        for (List<Edge> edges : adjacencyList.values()) {
            if (edges != null) {
                edges.removeIf(edge -> edge.getDestinationNode().equals(nodeToRemove));
            }
        }
    }


    /**
     * Adds a bidirectional edge between two nodes.
     * 
     * @param name1 the name of the first node
     * @param name2 the name of the second node
     * @param type the type of the edge (e.g., "Road", "Air", etc.)
     */
    public void addEdge(String name1, String name2, String type) {
        // Check that the edge is valid
        if (name1 == null || name2 == null || type == null || name1.equals(name2)) {
            System.out.println("Edge is invalid");
        }

        // Create nodes if they don't exist
        Node node1 = new Node(name1);
        Node node2 = new Node(name2);

        // Make sure that nodes exist in the adjacency list
        adjacencyList.putIfAbsent(node1, new ArrayList<>());
        adjacencyList.putIfAbsent(node2, new ArrayList<>());

        Edge edge = new Edge(node2, type); // Node1 -> Node2
        Edge reverseEdge = new Edge(node1, type); // Node2 -> Node1

        // Add edge to adjacency list
        if (!adjacencyList.get(node1).contains(edge)) {
            adjacencyList.get(node1).add(edge);
        }
        // Add bidirectional edge to adjacency list
        if (!adjacencyList.get(node2).contains(reverseEdge)) {
            adjacencyList.get(node2).add(reverseEdge);
        }
    }


    /**
     * Removes an edge between two nodes.
     * 
     * @param name1 the name of the first node
     * @param name2 the name of the second node
     * @param type the type of the edge to remove
     */
    public void removeEdge(String name1, String name2, String type) {
        // Create two node objects
        Node node1 = new Node(name1);
        Node node2 = new Node(name2);

        // Create two edge objects
        Edge edge1 = new Edge(node2, type); // Node1 -> Node2
        Edge edge2 = new Edge(node1, type); // Node2 -> Node1

        // Get the edges from the adjacency list
        List<Edge> edges1 = adjacencyList.get(node1);
        List<Edge> edges2 = adjacencyList.get(node2);

        // Remove the edges
        if (edges1 != null) edges1.remove(edge1);
        if (edges2 != null) edges2.remove(edge2);
    }


    /**
     * Checks if an edge exists between two nodes.
     * 
     * @param name1 the name of the first node
     * @param name2 the name of the second node
     * @param type the type of the edge to check
     * @return true if the edge exists
     */
    public boolean hasEdge(String name1, String name2, String type) {
        // Create two node objects
        Node node1 = new Node(name1);
        Node node2 = new Node(name2);

        // Get the list of edges from node1. If they do not return emply list
        List<Edge> edges = adjacencyList.getOrDefault(node1, new ArrayList<>());
        // Iterate through the edges and check if they match the type and destination
        for (Edge edge : edges) {
            if (edge.getDestinationNode().equals(node2) && edge.getKind().equals(type)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Gets all edges of a specific type in the graph.
     * 
     * @param type the type of edges to get
     * @return a string representation of all edges of the specified type
     */
    public String getEdgesOfType(String type) {
        // Create a list to store formatted edges
        List<String> edges = new ArrayList<>();
        // Iterate through adjacency list
        for (Map.Entry<Node, List<Edge>> entry : adjacencyList.entrySet()) {
            Node main = entry.getKey(); // Get the main node
            // Iterate through all the edges connected to this node
            for (Edge edge : entry.getValue()) {
                // Check if the edge matches the type
                if (edge.getKind().equals(type)) {
                    // Format the edge and add it to the list
                    String formattedEdge = String.format("(%s, %s)", main, edge.getDestinationNode());
                    edges.add(formattedEdge);
                }
            }
        }
        // Join the formatted edges with spaces
        return String.join(" ", edges);
    }


    /**
     * Prints the graph to the console.
     */
    public void print() {
        TreeSet<Node> sortedNodes = new TreeSet<>(Comparator.comparing(Node::getName));
        sortedNodes.addAll(adjacencyList.keySet());

        for (Node node : sortedNodes) {
            System.out.print(node.getName() + ": ");
            List<Edge> edges = adjacencyList.getOrDefault(node, new ArrayList<>());
            edges.sort(Comparator.comparing(e -> e.getDestinationNode().getName())); // Alphabetical order

            for (Edge edge : edges) {
                System.out.printf("(%s, %s) ", edge.getDestinationNode().getName(), edge.getKind());
            }
            System.out.println(); // Print new line after each node
        }
    }


    /**
     * Represents an edge in the graph.
     * An edge connects two nodes and has a type (kind).
     */
    private static class Edge {
        private final Node destinationNode;
        private final String kind;

        /**
         * Constructs a new edge.
         * 
         * @param destination the destination node of the edge
         * @param kind the type of the edge
         */
        public Edge(Node destination, String kind) {
            this.destinationNode = destination;
            this.kind = kind;
        }

        /**
         * Gets the destination node of this edge.
         * 
         * @return the destination node
         */
        public Node getDestinationNode() {
            return destinationNode;
        }

        /**
         * Gets the type of this edge.
         * 
         * @return the type of the edge
         */
        public String getKind() {
            return kind;
        }

        /**
         * Compares this edge with another for equality.
         * Two edges are equal if they have the same destination node and the same type.
         * 
         * @param obj the object to compare with
         * @return true if the objects are equal, false otherwise
         */
        @Override
        public boolean equals(Object obj) {
            // Check if comparing with same thing
            if (this == obj) return true;
            // Make sure object is an instance of Edge
            if (!(obj instanceof Edge)) return false;
            // Cast if it is an instance of Edge
            Edge other = (Edge) obj;

            // Return true if destination nodes are equal and the edge types match
            return destinationNode.equals(other.destinationNode) && kind.equals(other.kind);
        }

        /**
         * Returns a hash code for this edge.
         * 
         * @return a hash code value for this edge
         */
        @Override
        public int hashCode() {
            // Return a hash code based on the destination node and edge type
            return Arrays.hashCode(new Object[]{destinationNode, kind});
        }
    }
}
