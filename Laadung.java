import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class Laadung implements Runnable {


    private String inventar = "inventaar.txt";
    private String[] tooted = {"Piim", "Sai", "Juust", "Sink"};
    private int[] kogused = new int[tooted.length];
    private int maksimaalne = 500;
    private Random random = new Random();
    private static double ostetudKogus = 0;

    public Laadung(double ostetudKogus) {
        this.ostetudKogus = ostetudKogus;
    }

    public static double getOstetudKogus() {
        return ostetudKogus;
    }

    @Override
    public void run() {
        int loendur = 0;
        boolean onNeg = false;
        try {

            while (true) {
                FileWriter kirjuta = new FileWriter(inventar, false);
                PrintWriter prindiFaili = new PrintWriter(kirjuta);
                double uuedTooted = 0; // Variable to store total bought products for each 10-second interval
                for (int i = 0; i < tooted.length; i++) {
                    int ostukogus = random.nextInt(maksimaalne) + 1;
                    double sisseostmisehind = ostukogus * 0.5;
                    uuedTooted += sisseostmisehind; // Increment the count of newly added products
                    kogused[i] += ostukogus;
                    ostetudKogus += sisseostmisehind;
                    prindiFaili.println(tooted[i] + ", " + kogused[i]);

                    //System.out.println(tooted[i] + ", " + kogused[i]);
                }
                prindiFaili.close();
                Thread.sleep(10000);
                loendur++;
                System.out.println("Päevi möödas:" + loendur);
                System.out.println("Tänase päeva kulud: " + uuedTooted); // Print the count of newly added products
                System.out.println("Kogukulu pärast " + loendur + " päeva: " + ostetudKogus); // Print total bought products during each 10-second interval
                for (int i = 0; i < kogused.length; i++) {
                    if (kogused[i] < 0) {
                        onNeg = true;
                        break;
                    }
                }
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
