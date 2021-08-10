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
    private List<Vert> _input = new ArrayList<Vert>(); // Contains all vertices inputted by the user in the format [boundary, hole_1, ..., hole_n] (no subarrays)
    private List<Integer> holePositions = new ArrayList<Integer>(); // Contains the position of each hole in _input and vertices; NOT SURE IF THIS SHOULD BE AN ARRAYLIST
    private List<Integer> _output = new ArrayList<Integer>(); // Contains output of the calculation in the format [t1_1, t1_2, t1_3, ..., tn_1, tn_2, tn_3] (size isn't known because there are holes so an ArrayList is used)
    private boolean calculationRun = false; // Indicates whether the calculation has been run; resets on clear() or reset()

    /**
     * Creates a new MonotoneTriangulator
     */
    public MonotoneTriangulator()

    /**
     * Adds the outer boundary to the polygon, clearing all current holes (and any current outer boundary)
     * @param points The array of points to add to the polygon, in the format [x1, y1, x2, y2, ...]
     * Precondition: points is in counterclockwise order
     * Precondition: points.length is a multiple of 2
     * @throws IllegalArgumentException points is in clockwise order
     * @throws IllegalArgumentException points.length is not a multiple of 2
     */
    public void set(double[] points) throws IllegalArgumentException

    /**
     * Adds the outer boundary to the polygon
     *
     * Can be called multiple times
     *
     * @param points The array of points to add to the polygon, in the format [x1, y1, x2, y2, ...]
     * Precondition: points is in clockwise order
     * Precondition: points.length is a multiple of 2
     * @throws IllegalArgumentException points is in counterclockwise order
     * @throws IllegalArgumentException points.length is not a multiple of 2
     */
    public void addHole(double[] points) throws IllegalArgumentException

    public void reset()

    public void clear()

    /**
     * Categorizes each vertex into one of 5 types: Start, Split, End, Merge, Regular
     * @param verts The List of vertices
     * @return A queue (A priority queue in the textbook, but no elements are added so a queue works) containing the vertices in vertical order
     */
    public Queue<Vert> categorize(List<Vert> verts)
    // Vertices with larger y-coordinate have higher priority
    // For vertices with the same y-coordinate, the one with smaller x-coordinate has higher priority

    /**
     * Adds a diagonal between two vertices
     * @param src The source vertex
     * @param dst The destination vertex
     * @param help The HashMap of helper vertices (edge index to vertex index)
     * @param tree The tree of edges
     * @param verts The List of vertices
     * @return The copy of the source vertex (used later)
     */
    public Vert addDiagonal(int src, int dst, HashMap<Integer, Integer> help, TreeSet<Edge> tree, List<Vert> verts)
    // The helper HashMap might not be from Integer to Integer like in the Python code; Java seems to not support int->int, only Integer->Integer
    // Use IntIntMap from LibGDX (copy code from LibGDX into a new class in this project)

    /**
     * Diagonalizes the polygon into monotone partitions
     * @param queue The queue of vertices, arranged vertically
     * @param verts The List of vertices
     * @return A list called result in the Python code, maybe for debugging
     */
    public void diagonalize(Queue<Vert> queue, List<Vert> verts)

    /**
     * Partitions the polygon after the diagonals are drawn
     * @param verts The List of vertices
     * @return Something, not really sure
     */
    public List<Integer> partition (List<Vert> verts)

    /**
     * Does something, not sure
     * @param index idk
     * @param top idk
     * @param bot idk
     */
    public boolean isLeft(Vert index, Vert top, Vert bot) // Used to find the left and right branches of the monotone polygon

    /**
     * Triangulates the monotone partitions of the polygon
     * @param poly I think the polygon, but I'm not sure how it's represented
     * @return The final triangles of the triangulated polygon as an int array
     */
    public int[] monoTriangulate(/* some stuff */)

    public void calculate()

    public double[] getTriangles()

    /**
     * Represents a vertex
     */
    private class Vert

    /**
     * Represents an edge
     */
    private class Edge
}