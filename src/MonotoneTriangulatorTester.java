import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Tests the class MonotoneTriangulator
 * @author Ronan Venkat
 * @version 8/13/2021
 */
public class MonotoneTriangulatorTester
{
    /**
     * Read vertices from a file and initialize input in MonotoneTriangulator using the methods set and addHole
     * @param m The MonotoneTriangulator object that will have its vertices initialized
     */
    private void readVerts(MonotoneTriangulator m)
    {
        List<Double> points = new ArrayList<Double>();
        boolean useSet = true;

        try
        {
            File holes = new File("holes.txt");
            Scanner sc = new Scanner(holes);
            while (sc.hasNextLine())
            {
                String line = sc.nextLine();

                if (line.equals("---")) // There's a --- at the end of the file as well
                {
                    if (useSet)
                    {
                        m.set(points.stream().mapToDouble(Double::doubleValue).toArray());
                        useSet = false;
                    }
                    else
                    {
                        m.addHole(points.stream().mapToDouble(Double::doubleValue).toArray());
                    }
                    points.clear();
                }
                else
                {
                    String[] lineArray = line.split(" ");
                    double x = Double.parseDouble(lineArray[0].substring(1, lineArray[0].length() - 1));
                    double y = Double.parseDouble(lineArray[1].substring(0, lineArray[1].length() - 1));
                    points.add(x);
                    points.add(y);
                }
            }
            sc.close();
        }
        catch (FileNotFoundException e)
        {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * Main method
     */
    public static void main(String[] args)
    {
        MonotoneTriangulator m = new MonotoneTriangulator();
        MonotoneTriangulatorTester mt = new MonotoneTriangulatorTester();

        mt.readVerts(m);
        m.calculate();
    }
}
