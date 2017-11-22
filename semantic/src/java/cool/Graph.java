package cool;

import java.util.*;

class Edge {
    public Vertex v;

    public Edge(Vertex d) {
        this.v = d;
    }

}

class Vertex {
    public String name;
    public List<Edge> adj;
    public double dist;
    public Vertex prev;
    public int scratch;

    public Vertex(String nm) {
        this.name = nm;
        this.adj = new LinkedList<Edge>();
        reset();

    }

    public void reset() {
        this.dist = Double.POSITIVE_INFINITY;
        this.prev = null;
        this.scratch = 0;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Vertex) {
            Vertex otherV = (Vertex) other;
            return this.name.equals(otherV.name);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    public String toString() {
        return name;
    }
}

public class Graph {
    public Map<String, Vertex> nameVertexMap;

    public Graph() {
        nameVertexMap = new HashMap<String, Vertex>();
    }

    public void addEdge(String sourceName, String destName) {
        Vertex v = getVertex(sourceName);
        Vertex w = getVertex(destName);
        v.adj.add(new Edge(w));
        nameVertexMap.put(v.name, v);
        nameVertexMap.put(w.name, w);
    }

    private Vertex getVertex(String vertexName) {
        if (vertexName == null) {
            return null;
        }

        Vertex v = vertexMap.get(vertexName);
        if (v == null) {
            v = new Vertex(vertexName);
            vertexMap.put(vertexName, v);
        }
        return v;
    }

    private void clearAll() {
        for (Vertex v : vertexMap.values()) {
            v.reset();
        }
    }

    public boolean hasCycle() {
        return getCycles().keySet().size() > 0;
    }

    public Map<String, String> getCycles() {
        clearAll(); // initialize graph
        Map<String, String> cycles = new HashMap<String, String>();
        Set<Vertex> visitedSet = new HashSet<Vertex>();
        Set<Vertex> recursionStackSet = new HashSet<Vertex>();
        boolean cycle = false;
        for (Vertex v : vertexMap.values()) {
            if (!visitedSet.contains(v)) {
                cycleCheckerHelper(v, visitedSet, recursionStackSet, cycles);
            }
        }
        return cycles;
    }

    private void cycleCheckerHelper(Vertex v, Set<Vertex> visitedSet, Set<Vertex> recursionStackSet,
            Map<String, String> cycles) {
        visitedSet.add(v);
        recursionStackSet.add(v);
        for (Edge e : v.adj) {
            if (!visitedSet.contains(e.v)) {
                cycleCheckerHelper(e.v, visitedSet, recursionStackSet, cycles);
            } else if (recursionStackSet.contains(e.v)) {
                cycles.put(e.v.name, v.name);
            }
        }
        recursionStackSet.remove(v);
    }
    /*
      The following function generates a hash map, representing the subtree rooted at root.
      Here, key: Node Name.
            value: Parent Name.
            NULL at root.
    */
    private Map<String, String> toNodeParentHashMap(Vertex root) {
        Map<String, String> output = new HashMap<String, String>();
        Stack<Vertex> stack = new Stack<Vertex>();
        stack.push(root);
        output.put(root.name, null);
        while (!stack.isEmpty()) {
            Vertex node = stack.pop();

            for (Edge e : node.adj) {
                output.put(e.v.name, node.name);
                stack.push(e.v);
            }
        }

        return output;
    }

    public Map<String, String> getNodeParentHashMap(String rootName) {
        return toNodeParentHashMap(nameVertexMap.get(rootName));
    }
    /*
      The following function assumes graph is a tree, and that root node is "Object"
      It returns:
          - true:   If class1 is a child of class2
          - false:  Otherwise, or if either class1, class2, or rootName are not in tree.
    */
    public boolean conforms(String class1, String class2, String rootName) {
        Map<String, String> nodeParentMap = toNodeParentHashMap(nameVertexMap.get(rootName));
        String c1 = class1;
        String c2 = class2;
        if (c1.equals("no_type"))
            return true;
        if (c2 == null)
            return false;
        while (c1 != null) {
            if (c1.equals(class2)) {
                return true;
            }
            c1 = nodeParentMap.get(c1);
        }
        return false;
    }

    public String lub(String class1, String class2, String rootName) {
        Map<String, String> nodeParentMap = toNodeParentHashMap(nameVertexMap.get(rootName));
        Stack<String> c1AncestorStack = new Stack<String>(); //This includes class1 itself.
        Stack<String> c2AncestorStack = new Stack<String>(); //This includes class2 itself.
        String c1 = class1;
        String c2 = class2;

        if (nameVertexMap.get(c1) == null || nameVertexMap.get(c2) == null) {
            return null;
        }
        while (c1 != null) {
            c1AncestorStack.push(c1);
            c1 = nodeParentMap.get(c1);
        }
        while (c2 != null) {
            c2AncestorStack.push(c2);
            c2 = nodeParentMap.get(c2);
        }
        if (c1AncestorStack.empty() || c2AncestorStack.empty()) {
            return null;
        }
        String lowestCommonAncestorSoFar = null;
        while (!c1AncestorStack.empty() && !c2AncestorStack.empty()) {
            String c1Ancestor = c1AncestorStack.pop();
            String c2Ancestor = c2AncestorStack.pop();
            if (c1Ancestor.equals(c2Ancestor))
                lowestCommonAncestorSoFar = c1Ancestor;
            else
                break;
        }
        return lowestCommonAncestorSoFar;

    }

    private Map<String, Vertex> vertexMap = new HashMap<String, Vertex>();
}
