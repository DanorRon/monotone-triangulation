import java.util.*;

/**
 * Uses monotone triangulation to split a polygon, possibly with holes, into triangles
 * @author Ronan Venkat
 * @version 7/23/2021
 */
public class MonotoneTriangulator
{
    // Use IntelliJ History to find stuff for driver class

    private List<Vert> vertices = new ArrayList<Vert>(); // Combined data structure, erase on reset()
    private double[] _input = new double[0]; // Contains all vertices inputted by the user in the format [boundary, hole_1, ..., hole_n] (no subarrays)
    private List<Integer> holePositions = new ArrayList<Integer>(); // Contains the position of each hole in _input and vertices; NOT SURE IF THIS SHOULD BE AN ARRAYLIST TODO create new class floatArray for _input and holePositions
    private List<Integer> _output = new ArrayList<Integer>(); // Contains output of the calculation in the format [t1_1, t1_2, t1_3, ..., tn_1, tn_2, tn_3] (size isn't known because there are holes so an ArrayList is used) TODO don't use floatArray class for the sake of higher precision with integers
    private boolean calculationRun = false; // Indicates whether the calculation has been run; resets on clear() or reset()

    // TODO Remove underscores for _input and _output, possibly

    // TODO Possibly create convenience constructor which calls set

    /**
     * Creates a new MonotoneTriangulator
     */
    public MonotoneTriangulator() {}

    /**
     * Adds the outer boundary to the polygon, clearing all current holes (and any current outer boundary)
     *
     * Clears any current holes
     *
     * @param points The array of points to add to the polygon, in the format [x1, y1, x2, y2, ...]
     * @throws IllegalArgumentException points is not in counterclockwise order
     * @throws IllegalArgumentException points.length is not a multiple of 2
     */
    public void set(double[] points) throws IllegalArgumentException
    {
        /*
        if (points is not in counterclockwise order) // TODO
        {
            throw new IllegalArgumentException("points is not in counterclockwise order");
        }
         */

        if (points.length % 2 != 0)
        {
            throw new IllegalArgumentException("points.length is not a multiple of 2");
        }

        clear();

        double[] destination = new double[_input.length + points.length];
        System.arraycopy(_input, 0, destination, 0, _input.length);
        System.arraycopy(points, 0, destination, _input.length, points.length);
        _input = destination;
    }

    /**
     * Adds the outer boundary to the polygon
     *
     * Can be called multiple times.
     *
     * If any boundaries intersect, behavior is undefined.
     *
     * @param points The array of points to add to the polygon, in the format [x1, y1, x2, y2, ...]
     * @throws IllegalArgumentException points is not in clockwise order
     * @throws IllegalArgumentException points.length is not a multiple of 2
     */
    public void addHole(double[] points) throws IllegalArgumentException
    {
        /*
        if (points is not in clockwise order) // TODO
        {
            throw new IllegalArgumentException("points is not in clockwise order");
        }
         */

        if (points.length % 2 != 0)
        {
            throw new IllegalArgumentException("points.length is not a multiple of 2");
        }

        holePositions.add(_input.length);

        double[] destination = new double[_input.length + points.length];
        System.arraycopy(_input, 0, destination, 0, _input.length);
        System.arraycopy(points, 0, destination, _input.length, points.length);
        _input = destination;
    }

    /**
     * Clears triangulation calculation data, but does not erase user input (boundary and holes)
     *
     * Reclaims memory allowing one to triangulate later. Used for extrusion.
     */
    public void reset()
    {
        vertices.clear();
        calculationRun = false;
    }

    /**
     * Clears the program, including user input, to reset the program for another triangulation
     */
    public void clear()
    {
        vertices.clear();
        _input = new double[0];
        holePositions.clear();
        _output.clear();
        calculationRun = false;
    }

    /**
     * Builds vertices from _input, and links elements of vertices together to make it a doubly linked list as well as an array
     */
    private void createCombinedDataStructure()
    {
        Vert head = new Vert(0, _input[0], _input[1]);
        vertices.add(head);
        Vert prev = head;

        for (int i = 1; i < _input.length / 2; i++) // TODO What if the boundary or hole isn't large enough
        {
            if (holePositions.contains(i * 2))
            {
                // Link back
                prev.next = head;
                head.prev = prev;

                // Recreate head
                head = new Vert(i, _input[2 * i], _input[2 * i + 1]);
                vertices.add(head);

                prev = head; // This line should fix the issue with linking the doubly linked list
            }
            else
            {
                Vert curr = new Vert(i, _input[2 * i], _input[2 * i + 1]);
                vertices.add(curr);
                prev.next = curr;
                curr.prev = prev;
                prev = curr;
            }
        }

        prev.next = head;
        head.prev = prev;
    }

    /**
     * Categorizes each vertex into one of 5 types: Start, Split, End, Merge, Regular
     * @param verts The List of vertices
     * @return A queue (A priority queue in the textbook, but no elements are added so a queue works) containing the vertices in vertical order
     */
    private List<Vert> categorize(List<Vert> verts)
    {
        // Vertices with larger y-coordinate have higher priority
        // For vertices with the same y-coordinate, the one with smaller x-coordinate has higher priority

        /*
        Queue stuff, namely: Create a copy of verts (that won't change the original) as a queue, sort it (if it isn't a priority queue), and reverse it (using the ordering in class Vert)
         */

        List<Vert> queue = new ArrayList<Vert>(verts);
        Collections.sort(queue);
        Collections.reverse(queue);

        Iterator<Vert> itr = queue.iterator();
        while (itr.hasNext()) // TODO What about the beginning and end of the queue? There might not be a prev or next
        {
            Vert item = itr.next();
            Vert neb1 = item.prev;
            Vert neb2 = item.next;

            if (neb1.compareTo(item) < 0 && neb2.compareTo(item) < 0)
            {
                if (item.ccw(neb1, neb2))
                {
                    item.type = "start"; // TODO Use enum
                }
                else
                {
                    item.type = "split";
                }
            }
            else if (neb1.compareTo(item) > 0 && neb2.compareTo(item) > 0)
            {
                if (item.ccw(neb1, neb2))
                {
                    item.type = "end";
                }
                else
                {
                    item.type = "merge";
                }
            }
            else
            {
                item.type = "regular";
            }
        }

        return queue;
    }

    /**
     * Adds a diagonal between two vertices
     * @param src The source vertex
     * @param dst The destination vertex
     * @param help The HashMap of helper vertices (edge index to vertex index)
     * @param tree The tree of edges
     * @param verts The List of vertices
     * @return The copy of the source vertex (used later)
     */
    private Vert addDiagonal(int src, int dst, HashMap<Integer, Integer> help, TreeSet<Edge> tree, List<Vert> verts)
    {
        // The helper HashMap might not be from Integer to Integer like in the Python code; Java seems to not support int->int, only Integer->Integer
        // Use IntIntMap from LibGDX (copy code from LibGDX into a new class in this project)

        //Find the originals
        Vert orig1 = verts.get(src);
        Vert orig2 = verts.get(dst);

        // Copy the nodes so we can split
        Vert copy1 = orig1.copy();
        copy1.index = verts.size();
        Vert copy2 = orig2.copy();
        copy2.index = verts.size() + 1;

        orig2.next.prev = copy2;
        orig1.next.prev = copy1;

        // This does the split
        orig1.next = copy2;
        copy2.prev = orig1;
        orig2.next = copy1;
        copy1.prev = orig2;

        // Update support structures
        if (help.containsKey(src))
        {
            help.put(copy1.index, help.get(src));
        }

        // TODO Should tree reference type be Set or TreeSet

        Edge edge = new Edge(src,verts);
        if (tree.contains(edge))
        {
            Edge orig = tree.floor(edge);
            orig.index = copy1.index;
        }

        if (help.containsKey(dst))
        {
            help.put(copy2.index, help.get(dst));
        }

        edge = new Edge(dst,verts);
        if (tree.contains(edge))
        {
            Edge orig = tree.floor(edge);
            orig.index = copy2.index;
        }

        verts.add(copy1);
        verts.add(copy2);

        // Returns the copy of the source vertex (we need it later)
        return copy1;
    }

    /**
     * Diagonalizes the polygon into monotone partitions
     * @param queue The queue of vertices, arranged vertically
     * @param verts The List of vertices
     * @return A list called result in the Python code, maybe for debugging
     */
    private void diagonalize(Queue<Vert> queue, List<Vert> verts) {}

    /**
     * Partitions the polygon after the diagonals are drawn
     * @param verts The List of vertices
     * @return Something, not really sure
     */
    private List<Integer> partition (List<Vert> verts) {return null;}

    /**
     * Does something, not sure
     * @param index idk
     * @param top idk
     * @param bot idk
     */
    private boolean isLeft(Vert index, Vert top, Vert bot) {return false;} // Used to find the left and right branches of the monotone polygon

    /**
     * Triangulates the monotone partitions of the polygon
     * /* @param poly I think the polygon, but I'm not sure how it's represented
     * @return The final triangles of the triangulated polygon as an int array
     */
    private int[] monoTriangulate(/* some stuff */) {return null;}

    /**
     * Calculates the monotone triangulation for the polygon
     *
     * Categorize, diagonalize (add_diagonal), partition, mono_triangulate (is_left)
     * // TODO throws exception if fails
     */
    public void calculate()
    {
        createCombinedDataStructure();
        List<Vert> queue = categorize(vertices);
        for (Vert v : queue)
        {
            System.out.println(v);
        }
    }

    /**
     * Returns the answer after calculation
     * @return an array representing the triangulated polygon // TODO Are there subarrays for each point?
     * // TODO Should this return something, or modify _output?
     */
    public double[] getTriangles() {return null;}

    /**
     * Represents a vertex
     */
    private class Vert implements Comparable<Vert> // TODO Add documentation
    {
        public int index;
        public double x;
        public double y;
        public String type;
        public Vert next;
        public Vert prev;

        public Vert(int index, double x, double y) // TODO What access should this be?
        {
            this.index = index;
            this.x = x;
            this.y = y;
            this.type = "";
            this.next = null;
            this.prev = null;
        }

        public int compareTo(Vert other) // Counterclockwise rotation of the polygon
        {
            if (this.y == other.y)
            {
                return Double.compare(this.x, other.x);
            }
            return Double.compare(this.y, other.y);
        }

        public boolean equals(Object other) // TODO Is this right?
        {
            if (other == this) return true;
            if (!(other instanceof Vert)) return false;
            Vert v = (Vert) other;
            return this.index == v.index;
        }

        public String toString()
        {
            String str = index + "-(" + x + ", " + y + ")";
            if (!type.equals("")) str += "-[" + type + "]";
            return str;
        }

        public boolean ccw(Vert a, Vert b)
        {
            double val  = (b.y - a.y) * (this.x - a.x);
            val -= (b.x - a.x) * (this.y - a.y);
            return val > 0;
        }

        public Vert copy()
        {
            Vert result = new Vert(index, x, y);
            result.type = type;
            result.prev = prev;
            result.next = next;
            return result;
        }
    }

    /**
     * Represents an edge
     */
    private class Edge implements Comparable<Edge>  // TODO Add documentation
    {
        // TODO CompareTo, equals, toString

        public int index;
        List<Vert> verts;

        public Edge(int index, List<Vert> verts) // TODO What access should this be?
        {
            this.index = index;
            this.verts = verts;
        }

        public int compareTo(Edge other)
        {
            if (this.equals(other)) return 0; // TODO is this correct?

            Vert shead = this.verts.get(this.index); // "this" isn't necessary here, but I think it helps clarify
            Vert stail = shead.next;
            Vert ohead = other.verts.get(other.index);
            Vert otail = ohead.next;

            if (ohead.y == otail.y)
            {
                if (shead.y == stail.y)
                {
                    return (shead.y < ohead.y) ? -1 : 1;
                }
                return stail.ccw(shead, ohead) ? -1 : 1;
            }
            else if (shead.y == stail.y)
            {
                return !otail.ccw(ohead, shead) ? -1 : 1;
            }
            else if (shead.y < ohead.y)
            {
                return !otail.ccw(ohead, shead) ? -1 : 1;
            }
            else
            {
                return stail.ccw(shead, ohead) ? -1 : 1;
            }
        }

        public boolean equals(Object other) // TODO Is this right?
        {
            if (other == this) return true;
            if (!(other instanceof Edge)) return false;
            Edge v = (Edge) other;
            return this.index == v.index;
        }

        public String toString()
        {
            Vert v = verts.get(index);
            return v.toString() + " to " + (v.next).toString();
        }
    }
}