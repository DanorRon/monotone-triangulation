import java.util.*;

/**
 * Uses monotone triangulation to split a polygon, possibly with holes, into triangles
 * @author Ronan Venkat
 * @version 7/23/2021
 */
public class MonotoneTriangulator
{
    // Use IntelliJ History to find stuff for driver class

    List<Vert> vertices = new ArrayList<Vert>(); //combined data structure, erase on reset()

    //TODO _input field containing all vertices, array containing all the positions of the holes, _output ArrayList of integers (size isn't known), boolean saying if the calculation has been run (reset on clear() or reset())

    public MonotoneTriangulator()

    public void set(double[] points)

    public void addHole(double[] points)

    public void reset()

    public void clear()

    public void calculate()

    public double[] getTriangles()

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

    /**
     * Represents a vertex
     */
    private class Vert

    /**
     * Represents an edge
     */
    private class Edge
}