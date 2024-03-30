import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Laadung implements Runnable {
    private String inventar = "inventaar.txt"; // Inventari faili tee
    private String logiFail = "tehingud.txt"; // Logi faili tee
    private String[] tooted = {"Piim", "Sai", "Juust", "Sink"}; // Saadaval olevad tooted
    private int[] kogused = new int[tooted.length]; // Toodete kogused laos
    private int maksimaalne = 10; // Maksimaalne kogus, mida saab igat toodet lisada
    private Random suvaline = new Random(); // Juhusliku arvu generaator
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Kuupäeva-vormindaja

    @Override
    public void run() {
        try {
            while (true) {
                uuendaInventari(); // Uuendage inventari
                Thread.sleep(10000); // Oodake enne järgmist uuendust
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Meetod inventari uuendamiseks
    private void uuendaInventari() throws IOException {
        FileWriter kirjuta = new FileWriter(inventar, false); // Ava inventarifail kirjutamiseks
        PrintWriter prindiFaili = new PrintWriter(kirjuta);

        FileWriter logiKirjutaja = new FileWriter(logiFail, true); // Ava logifail kirjutamiseks
        PrintWriter logiPrintija = new PrintWriter(logiKirjutaja);

        // Kirjutage uute toodete kogused logifaili
        logiPrintija.println(getTimestamp() + " Uued tooted saabusid:");
        for (int i = 0; i < tooted.length; i++) {
            int uusKogus = suvaline.nextInt(maksimaalne) + 1;
            kogused[i] += uusKogus;
            logiPrintija.println(uusKogus + " " + tooted[i]);
        }

        // Kirjutage uuendatud inventar inventarifaili
        for (int i = 0; i < tooted.length; i++) {
            prindiFaili.println(tooted[i] + ", " + kogused[i]);
            System.out.println(tooted[i] + ", " + kogused[i]);
        }

        prindiFaili.close(); // Sulgege inventarifail
        logiPrintija.close(); // Sulgege logifail
    }

    // Meetod praeguse kuupäeva ja kellaaja vormindamiseks
    private String getTimestamp() {
        return dateFormat.format(new Date());
    }

    // Main meetod
    public static void main(String[] args) {
        Laadung laadung = new Laadung();
        Thread thread = new Thread(laadung);
        thread.start();
    }
}
