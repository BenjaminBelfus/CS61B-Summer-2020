import java.lang.reflect.Array; //mmm
import java.util.*;

public class Graph implements Iterable<Integer> {

    private LinkedList<Edge>[] adjLists;
    private int vertexCount;

    /* Initializes a graph with NUMVERTICES vertices and no Edges. */
    public Graph(int numVertices) {
        adjLists = (LinkedList<Edge>[]) new LinkedList[numVertices];
        for (int k = 0; k < numVertices; k++) {
            adjLists[k] = new LinkedList<Edge>();
        }
        vertexCount = numVertices;
    }

    /* Adds a directed Edge (V1, V2) to the graph. */
    public void addEdge(int v1, int v2) {
        addEdge(v1, v2, 0);
    }

    /* Adds an undirected Edge (V1, V2) to the graph. */
    public void addUndirectedEdge(int v1, int v2) {
        addUndirectedEdge(v1, v2, 0);
    }

    /* Adds a directed Edge (V1, V2) to the graph with weight WEIGHT. If the
       Edge already exists, replaces the current Edge with a new Edge with
       weight WEIGHT. */
    public void addEdge(int v1, int v2, int weight) {
        // TODO: YOUR CODE HERE
        if (isAdjacent(v1, v2)) {
            LinkedList<Edge> T = adjLists[v1];
            for (Edge e : T) {
                if (e.to == v2) {
                    e.weight = weight;
                }
            }

        } else {
            Edge newEdge = new Edge(v1, v2, weight);
            adjLists[v1].add(newEdge);
        }
    }

    /* Adds an undirected Edge (V1, V2) to the graph with weight WEIGHT. If the
       Edge already exists, replaces the current Edge with a new Edge with
       weight WEIGHT. */
    public void addUndirectedEdge(int v1, int v2, int weight) {
        // TODO: YOUR CODE HERE
        if (isAdjacent(v1, v2)) {
            LinkedList<Edge> T = adjLists[v1];
            Edge thisEdge = T.get(v2);
            thisEdge.weight = weight;
        } else {
            Edge newEdge = new Edge(v1, v2, weight);
            adjLists[v1].add(newEdge);
            adjLists[v2].add(newEdge);
        }
    }

    /* Returns true if there exists an Edge from vertex FROM to vertex TO.
       Returns false otherwise. */
    public boolean isAdjacent(int from, int to) {
        // TODO: YOUR CODE HERE
        LinkedList<Edge> T = adjLists[from];
        for (Edge e : T) {
            if (e.to == to) {
                return true;
            }
        }
        return false;
    }

    /* Returns a list of all the vertices u such that the Edge (V, u)
       exists in the graph. */
    public List<Integer> neighbors(int v) {
        // TODO: YOUR CODE HERE
        List<Integer> T = new ArrayList<>();
        LinkedList<Edge> V = adjLists[v];
        for (Edge e : V) {
            T.add(e.to);
        }
        return T;
    }
    /* Returns the number of incoming Edges for vertex V. */
    public int inDegree(int v) {
        // TODO: YOUR CODE HERE
        int counter = 0;
        for (int i = 0; i < adjLists.length; i++) {
            LinkedList<Edge> t = adjLists[i];
            for (Edge e : t) {
                if (isAdjacent(e.to, v)) {
                    counter += 1;
                }
            }
        }
        return  counter;
    }

    /* Returns an Iterator that outputs the vertices of the graph in topological
       sorted order. */
    public Iterator<Integer> iterator() {
        return new TopologicalIterator();
    }

    /**
     *  A class that iterates through the vertices of this graph,
     *  starting with a given vertex. Does not necessarily iterate
     *  through all vertices in the graph: if the iteration starts
     *  at a vertex v, and there is no path from v to a vertex w,
     *  then the iteration will not include w.
     */
    private class DFSIterator implements Iterator<Integer> {

        private Stack<Integer> fringe;
        private HashSet<Integer> visited;

        public DFSIterator(Integer start) {
            fringe = new Stack<>();
            visited = new HashSet<>();
            fringe.push(start);
        }

        public boolean hasNext() {
            if (!fringe.isEmpty()) {
                int i = fringe.pop();
                while (visited.contains(i)) {
                    if (fringe.isEmpty()) {
                        return false;
                    }
                    i = fringe.pop();
                }
                fringe.push(i);
                return true;
            }
            return false;
        }

        public Integer next() {
            int curr = fringe.pop();
            ArrayList<Integer> lst = new ArrayList<>();
            for (int i : neighbors(curr)) {
                lst.add(i);
            }
            lst.sort((Integer i1, Integer i2) -> -(i1 - i2));
            for (Integer e : lst) {
                fringe.push(e);
            }
            visited.add(curr);
            return curr;
        }

        //ignore this method
        public void remove() {
            throw new UnsupportedOperationException(
                    "vertex removal not implemented");
        }

    }

    /* Returns the collected result of performing a depth-first search on this
       graph's vertices starting from V. */
    public List<Integer> dfs(int v) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        Iterator<Integer> iter = new DFSIterator(v);

        while (iter.hasNext()) {
            result.add(iter.next());
        }
        return result;
    }

    /* Returns true iff there exists a path from START to STOP. Assumes both
       START and STOP are in this graph. If START == STOP, returns true. */
    public boolean pathExists(int start, int stop) {
        // TODO: YOUR CODE HERE
        if (start == stop) {
            return true;
        } else {
            LinkedList<Edge> t = adjLists[start];
            DFSIterator it = new DFSIterator(start);
            while (it.hasNext()) {
                int next = it.next();
                if (next == stop) {
                    return true;
                }
            }
        }
        return false;
    }


    /* Returns the path from START to STOP. If no path exists, returns an empty
       List. If START == STOP, returns a List with START. */
    public List<Integer> path(int start, int stop) {
        // TODO: YOUR CODE HERE
        ArrayList<Integer> visitNode = new ArrayList<>();
        LinkedList<Integer> lst = new LinkedList<>();
        if (start == stop) {
            lst.add(start);
            return  lst;
        }else if (pathExists(start, stop)) {
            visitNode.add(start);
            DFSIterator it = new DFSIterator(start);
            int current = start;
            while (it.hasNext() && current != stop) {
                int next = it.next();
                current = next;
                visitNode.add(next);
            }
            lst.addFirst(current);
            while (current != start) {
                for (Integer i : visitNode) {
                    if (isAdjacent(i, current)) {
                        lst.addFirst(i);
                        current = i;
                        break;
                    }
                }
            }
        }
        return lst;
    }

    int[] node = new int[vertexCount];
    double[] cost = new double[vertexCount];

    public List<Integer> shortestPath(int start, int stop) {
        node = new int[vertexCount];
        cost = new double[vertexCount];
        // TODO: YOUR CODE HERE
        PriorityQueue<Integer> queue = new PriorityQueue(vertexCount, new CompareVertex());
        List<Integer> toReturn = new ArrayList<>();
        List<Integer> visited = new ArrayList<>();

        // adding the first one to the priorityTrack
        for (int i = 0; i < cost.length; i++) {
            cost[i] = Integer.MAX_VALUE;
        }
        cost[start] = 0;
        queue.add(start);
        node[start] = -1;

        while (!queue.isEmpty()) {
            int holdinTo = queue.remove();
            if (holdinTo == stop) {
                break;
            }
            else {
                List<Integer> l = neighbors(holdinTo);
                for (Integer f : l) {
                    if (visited.contains(f)) {
                        continue;
                    } else if (!visited.contains(f)) {
                        double currentCost = cost[holdinTo] + getEdge(holdinTo, f).weight;
                        if (cost[f] > currentCost) {
                            cost[f] = currentCost;
                            node[f] = holdinTo;
                            //check for duplicates
                        }
                        queue.add(f);
                        visited.add(holdinTo);
                    }
                }
            }
        }
        toReturn.add(stop);
        int s = node[stop];
        while (s != start) {
            toReturn.add(s);
            s = node[s];
        }
        toReturn.add(start);
        Collections.reverse(toReturn);
        return toReturn;
    }

    public class CompareVertex implements Comparator<Integer> {

        @Override
        public int compare(Integer o1, Integer o2) {
            if (cost[o1] > cost[o2]) {
                return 1;
            } else if (cost[o1] == cost[o2]) {
                return 0;
            } else {
                return -1;
            }
        }
    }


    public Edge getEdge(int u, int v) {
        // TODO: YOUR CODE HERE
        if (pathExists(u, v)) {
            LinkedList<Edge> E = adjLists[u];
            for (Edge T : E) {
                if (T.to == v) {
                    return T;
                }
            }
        }
        return null;
    }





    public List<Integer> topologicalSort() {
        ArrayList<Integer> result = new ArrayList<Integer>();
        Iterator<Integer> iter = new TopologicalIterator();
        while (iter.hasNext()) {
            result.add(iter.next());
        }
        return result;
    }

    private class TopologicalIterator implements Iterator<Integer> {

        private Stack<Integer> fringe;

        // TODO: Instance variables here!

        TopologicalIterator() {
            fringe = new Stack<Integer>();
            // TODO: YOUR CODE HERE
        }

        public boolean hasNext() {
            return !fringe.isEmpty();
        }

        public Integer next() {
            // TODO: YOUR CODE HERE
            return 0;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    private class Edge {

        private int from;
        private int to;
        private int weight;

        Edge(int from, int to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        public String toString() {
            return "(" + from + ", " + to + ", weight = " + weight + ")";
        }

    }

    private void generateG1() {
        addEdge(0, 1);
        addEdge(0, 2);
        addEdge(0, 4);
        addEdge(1, 2);
        addEdge(2, 0);
        addEdge(2, 3);
        addEdge(4, 3);
    }

    private void generateG2() {
        addEdge(0, 1);
        addEdge(0, 2);
        addEdge(0, 4);
        addEdge(1, 2);
        addEdge(2, 3);
        addEdge(4, 3);
    }

    private void generateG3() {
        addUndirectedEdge(0, 2);
        addUndirectedEdge(0, 3);
        addUndirectedEdge(1, 4);
        addUndirectedEdge(1, 5);
        addUndirectedEdge(2, 3);
        addUndirectedEdge(2, 6);
        addUndirectedEdge(4, 5);
    }

    private void generateG4() {
        addEdge(0, 1);
        addEdge(1, 2);
        addEdge(2, 0);
        addEdge(2, 3);
        addEdge(4, 2);
    }

    private void printDFS(int start) {
        System.out.println("DFS traversal starting at " + start);
        List<Integer> result = dfs(start);
        Iterator<Integer> iter = result.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next() + " ");
        }
        System.out.println();
        System.out.println();
    }

    private void printPath(int start, int end) {
        System.out.println("Path from " + start + " to " + end);
        List<Integer> result = path(start, end);
        if (result.size() == 0) {
            System.out.println("No path from " + start + " to " + end);
            return;
        }
        Iterator<Integer> iter = result.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next() + " ");
        }
        System.out.println();
        System.out.println();
    }

    private void printTopologicalSort() {
        System.out.println("Topological sort");
        List<Integer> result = topologicalSort();
        Iterator<Integer> iter = result.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next() + " ");
        }
    }

    public static void main(String[] args) {
        Graph g1 = new Graph(5);
        g1.generateG1();
        g1.printDFS(0);
        g1.printDFS(2);
        g1.printDFS(3);
        g1.printDFS(4);

        g1.printPath(0, 3);
        g1.printPath(0, 4);
        g1.printPath(1, 3);
        g1.printPath(1, 4);
        g1.printPath(4, 0);

        Graph g2 = new Graph(5);
        g2.generateG2();
        g2.printTopologicalSort();
    }
}