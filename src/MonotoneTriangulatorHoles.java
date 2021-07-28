import java.util.*;

/**
 * Uses monotone triangulation to split a polygon, possibly with holes, into triangles
 * @author Ronan Venkat
 * @version 7/23/2021
 */
public class MonotoneTriangulatorHoles //TODO rename
{
    List<Vert> vertices = new ArrayList<Vert>(); //combined data structure, erase on reset()

    //TODO _input field containing all vertices, array containing all the positions of the holes, _output array of integers (mono_triangulate uses an ArrayList and copies to the output array), boolean saying if the calculation has been run (reset on clear() or reset())

    /**
     * Reads vertices from file
     * @param path The path of the file containing the polygon
     * @return A List representing the polygon
     */
    public List<Vert> readVerts(String path) // I think that it returns a list, but I'm not sure

    /*
    There was a parse method in the Python code, but this can probably be achieved with the args parameter in the main method
     */

    /**
     * Displays the triangulation
     * @param args The command-line arguments including the speed and scale of the drawing tool
     * @param verts The List of vertices
     * @return
     */
    public idk display(String[] args, List<Vert> verts) //Not sure if the parameters are right, and I don't know if something should be returned

    /**
     * Probably draws something, not really sure
     */
    public void drawCats(/* Some stuff */)

    /**
     * Probably draws something, not really sure
     */
    public void drawTris(/* Some stuff */)

    /**
     * Probably draws diagonals, not really sure
     */
    public void drawDiags(/* Some stuff */)

    /**
     * Probably draws the separate sections or something, not really sure
     */
    public void drawParts(/* Some stuff */)

    /**
     * Pauses and asks for the user to press any key
     */
    public void pause()

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
    public List<something> diagonalize(Queue<Vert> queue, List<Vert> verts)

    /**
     * Partitions the polygon after the diagonals are drawn (I think)
     * @param verts The List of vertices
     * @return Something, not really sure
     */
    public List<something> partition (List<Vert> verts)

    /**
     * Does something, not sure
     * @param index idk
     * @param top idk
     * @param bot idk
     */
    public Boolean isLeft(idk index, idk top, idk bot)

    /**
     * Triangulates the monotone partitions of the polygon
     * @param poly I think the polygon, but I'm not sure how it's represented
     * @return The final triangles of the triangulated polygon as an int array
     */
    public int[] monoTriangulate(Something poly)

    /**
     * Main method
     */
    public static void main(String[] args)

    /**
     * Represents a vertex
     */
    private class Vert

    /**
     * Represents an edge
     */
    private class Edge
}