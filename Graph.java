import java.util.*;

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



    private Map<Node, List<Edge>> adjacencyList;

    public Graph() {
        adjacencyList = new HashMap<>();
    }


    public void addNode(String name) {
        if (name == null || name.isEmpty()) {
            System.out.println("Invalid node name");
        }
        Node node = new Node(name);
        adjacencyList.putIfAbsent(node, new ArrayList<>());
    }


    public void removeNode(String name) {
        Node node = new Node(name);
        adjacencyList.remove(node);
        adjacencyList.values().forEach(edges -> edges.removeIf(edge -> edge.getDestination().equals(node)));

    }


    public void addEdge(String name1, String name2, String type) {
        if (name1 == null || name2 == null || type == null || name1.equals(name2)) {
            System.out.println("Invalid edge");
        }

        Node node1 = new Node(name1);
        Node node2 = new Node(name2);

        adjacencyList.putIfAbsent(node1, new ArrayList<>());
        adjacencyList.putIfAbsent(node2, new ArrayList<>());

        Edge edge = new Edge(node2, type);
        Edge reverseEdge = new Edge(node1, type);

        if (!adjacencyList.get(node1).contains(edge)) {
            adjacencyList.get(node1).add(edge);
        }
        if (!adjacencyList.get(node2).contains(reverseEdge)) {  // Bidirectional edge
            adjacencyList.get(node2).add(reverseEdge);
        }


    }


    public void removeEdge(String name1, String name2, String type) {
        Node node1 = new Node(name1);
        Node node2 = new Node(name2);

        adjacencyList.getOrDefault(node1, new ArrayList<>()).removeIf(edge -> edge.getDestination().equals(node2) && edge.getType().equals(type));
        adjacencyList.getOrDefault(node2, new ArrayList<>()).removeIf(edge -> edge.getDestination().equals(node1) && edge.getType().equals(type));
    }


    public boolean hasEdge(String name1, String name2, String type) {
        Node node1 = new Node(name1);
        Node node2 = new Node(name2);

        return adjacencyList.getOrDefault(node1, new ArrayList<>())
                .stream()
                .anyMatch(edge -> edge.getDestination().equals(node2) && edge.getType().equals(type));
    }


    public String getEdgesOfType(String type) {
        Set<String> edges = new TreeSet<>();
        for (Map.Entry<Node, List<Edge>> entry : adjacencyList.entrySet()) {
            Node source = entry.getKey();
            for (Edge edge : entry.getValue()) {
                if (edge.getType().equals(type)) {
                    String formattedEdge = String.format("(%s, %s)", source, edge.getDestination());
                    edges.add(formattedEdge);
                }
            }
        }
        return String.join(" ", edges);
    }


    public void print() {
        TreeSet<Node> sortedNodes = new TreeSet<>(Comparator.comparing(Node::getName));
        sortedNodes.addAll(adjacencyList.keySet());

        for (Node node : sortedNodes) {
            System.out.print(node.getName() + ": ");
            List<Edge> edges = adjacencyList.getOrDefault(node, new ArrayList<>());
            edges.sort(Comparator.comparing(e -> e.getDestination().getName())); // Alphabetical order

            for (Edge edge : edges) {
                System.out.print(String.format("(%s, %s) ", edge.getDestination().getName(), edge.getType()));
            }
            System.out.println();
        }
    }


    private static class Edge {
        private final Node destination;
        private final String type;

        public Edge(Node destination, String type) {
            this.destination = destination;
            this.type = type;
        }

        public Node getDestination() {
            return destination;
        }

        public String getType() {
            return type;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Edge)) return false;
            Edge other = (Edge) obj;
            return destination.equals(other.destination) && type.equals(other.type);
        }

        @Override
        public int hashCode() {
            return Objects.hash(destination, type);
        }
    }
}
