import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class Laadung implements Runnable {//peab runnable kasutama, et saaks threadi samal ajal jooksutada

    private String inventar = "inventaar.txt";//fail kuhu kirjutame
    private String[] tooted = {"Piim", "Sai", "Juust", "Sink"};//erinevad tooted, saab muuta
    private int[] kogused = new int[tooted.length];//iga toote kogus
    private int maksimaalne = 10;//maksimaalne toodete lisamine
    private Random random = new Random();


    @Override
    public void run() {//runnable classist voetud
        int loendur = 0;
        boolean onNeg = false;
        try {
            //alustan suvaliste arvudega 1-10(maksimaalne)
            for (int i = 0; i < tooted.length; i++) {
                kogused[i] = random.nextInt(maksimaalne) + 1;
            }
            // numbrite uuendamise loogika
            while (true) {
                FileWriter kirjuta = new FileWriter(inventar, false); //append mode false, kirjutab ule mitte ei lisa: https://stackoverflow.com/questions/1225146/java-filewriter-with-append-mode
                PrintWriter prindiFaili = new PrintWriter(kirjuta);//prindib faili
                for (int i = 0; i < tooted.length; i++) {//kaib iga toote labi, kirjutab toote nime toodetest[] ja siis uuendab numbrit
                    kogused[i] += random.nextInt(maksimaalne) + 1;
                    prindiFaili.println(tooted[i] + ", " + kogused[i]);
                    System.out.println(tooted[i] + ", " + kogused[i]);
                }
                prindiFaili.close();//salvestus
                Thread.sleep(10000);//paus
                loendur++;
                System.out.println("Päevi möödas:"+ loendur);

                //negatiivsuse check
                for (int i = 0; i < kogused.length; i++) {
                    if (kogused[i] < 0) {
                        onNeg = true;
                        break;
                    }
                }
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }//errori puudmine
    }
}
