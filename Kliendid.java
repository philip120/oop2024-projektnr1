import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Kliendid implements Runnable {
    // Failide teekondade ja muude konstantide määratlemine
    private String inventar = "inventaar.txt"; // Inventarifail
    private String logiFail = "tehingud.txt"; // Tehingute logifail
    private String[] tooted = {"Piim", "Sai", "Juust", "Sink"}; // Olemasolevad tooted
    private int maksimum = 3; // Maksimaalne kogus, mida iga klient saab osta iga toote kohta
    private Random suvaline = new Random(); // Juhusliku arvu generaator
    private SimpleDateFormat kuupäevaVormindaja = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Kuupäeva-vormindaja
    private int klientideLoendur = 1; // Klientide loendur, alustab esimesest kliendist

    @Override
    public void run() {
        try {
            while (true) {
                int klientideArv = suvaline.nextInt(10) + 1; // Juhuslik klientide arv (1 kuni 10)
                for (int i = 0; i < klientideArv; i++) {
                    uuendaInventari();
                }
                Thread.sleep(10000); // Ootamine enne järgmist tsüklit
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Meetod inventari uuendamiseks vastavalt klientide ostudele
    private void uuendaInventari() throws IOException {
        // Lae inventarifailist praegune inventar
        Map<String, Integer> inventariKaart = loeInventar();

        // Ava logifail kirjutamiseks
        FileWriter logiKirjutaja = new FileWriter(logiFail, true);
        PrintWriter logiPrintija = new PrintWriter(logiKirjutaja);
        logiPrintija.println(kuupäevaVormindaja.format(new Date()) + " Toimusid tehingud: ");
        // Käi läbi iga toode
        for (String toode : tooted) {
            // Juhuslik kogus, mida iga klient ostab (0 kuni maksimum)
            int ostetudKogus = suvaline.nextInt(maksimum + 1);

            // Kirjuta tehingu info logifaili
            logiPrintija.println(klientideLoendur + ". Klient ostis " + ostetudKogus + " " + toode);

            // Võta praegune kogus inventarifailist
            int praeguneKogus = inventariKaart.getOrDefault(toode, 0);
            // Arvuta uus kogus, võttes arvesse klientide ostusid
            int uusKogus = Math.max(praeguneKogus - ostetudKogus, 0);
            // Uuenda kaarti uue kogusega
            inventariKaart.put(toode, uusKogus);
        }

        // Sulge logifail
        logiPrintija.close();

        // Ava inventarifail lisamiseks
        FileWriter kirjuta = new FileWriter(inventar, false);
        PrintWriter prindiFaili = new PrintWriter(kirjuta);

        // Kirjuta uuendatud inventar inventarifaili
        for (Map.Entry<String, Integer> sissekanne : inventariKaart.entrySet()) {
            prindiFaili.println(sissekanne.getKey() + ", " + sissekanne.getValue());
        }

        // Sulge fail
        prindiFaili.close();

        // Suurenda klientide loendurit järgmise ostu jaoks
        klientideLoendur++;
    }

    // Meetod inventarifailist inventari lugemiseks ja kaardi tagastamiseks
    private Map<String, Integer> loeInventar() throws IOException {
        Map<String, Integer> inventariKaart = new HashMap<>();

        // Ava inventarifail lugemiseks
        BufferedReader lugeja = new BufferedReader(new FileReader(inventar));
        String rida;
        while ((rida = lugeja.readLine()) != null) {
            String[] osad = rida.split(", ");
            if (osad.length == 2) {
                String toode = osad[0];
                int kogus = Integer.parseInt(osad[1]);
                inventariKaart.put(toode, kogus);
            }
        }
        // Sulge fail
        lugeja.close();

        // Tagasta kaart
        return inventariKaart;
    }

    // Meetod praeguse kuupäeva ja kellaaja vormindamiseks
    private String kuupäevaVormindus() {
        return kuupäevaVormindaja.format(new Date());
    }
}
