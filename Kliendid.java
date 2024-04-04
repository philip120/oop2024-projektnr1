import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

public class Kliendid implements Runnable {
    private String inventar = "inventaar.txt";
    private String logiFail = "tehingud.txt";
    private String[] tooted = {"Piim", "Sai", "Juust", "Sink"};
    private int maksimum = 3; //maksimaalne toodete arv mida üks klient saab korraga osta
    private Random suvaline = new Random();
    private int esialgneKlientideArv;
    private SimpleDateFormat kuupäevaVormindaja = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private int klientideLoendur = 1;
    private int koguKülastajateArv = 0;
    private double koguToodeteRahasumma = 0; //kõik tooted, mida poest on ostetud

    private double tooteHind; //Kordaja, millega pood tooteid edasi müüb

    private double koguKasum; //Kui palju on pood kasumit teeninud

    public Kliendid(int esialgneKlientideArv,double tooteHind) {
        this.esialgneKlientideArv = esialgneKlientideArv;
        this.tooteHind = tooteHind;
        this.koguKasum = 0;
    }

    public double getKoguKasum() {
        return koguKasum;
    }

    public void setKoguKasum(double koguKasum) {
        this.koguKasum = koguKasum;
    }

    public double getTooteHind() {
        return tooteHind;
    }

    public void setTooteHind(double tooteHind) {
        this.tooteHind = tooteHind;
    }

    public int getEsialgneKlientideArv() {
        return esialgneKlientideArv;
    }

    public void setEsialgneKlientideArv(int esialgneKlientideArv) {
        this.esialgneKlientideArv = esialgneKlientideArv;
    }

    @Override
    public void run() {
        try {
            while (true) {

                int klientideArv = suvaline.nextInt(esialgneKlientideArv) + 1;
                koguKülastajateArv += klientideArv; // Uuendada kogu klientide arvu
                int ostetudToodeteArv = 0; // päevane poest ostetud toodete arv
                for (int i = 0; i < klientideArv; i++) {
                    int ostetudTooted = uuendaInventari();

                    ostetudToodeteArv += ostetudTooted;
                    koguToodeteRahasumma += ostetudTooted * tooteHind; // lisa need tooted kogu klientide ostetud arvule
                }
                double ostetudKogus = Laadung.getOstetudKogus(); //See kui palju on kauplus tooteid sisse ostnud
                //double koguKasum = koguToodeteRahasumma - ostetudKogus;
                setKoguKasum(koguToodeteRahasumma - ostetudKogus);
                System.out.println(kuupäevaVormindaja.format(new Date()) + " - Külastajate arv: " + klientideArv + ", Klientidelt teenitud tulu kokku: " + ostetudToodeteArv + " eurot.");
                System.out.println("Jooksev kasum: " + getKoguKasum() + " eurot.");

                Thread.sleep(10000);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @return
     * @throws IOException
     */
    private int uuendaInventari() throws IOException {
        FileWriter logiKirjutaja = new FileWriter(logiFail, true);
        PrintWriter logiPrintija = new PrintWriter(logiKirjutaja);
        logiPrintija.println(kuupäevaVormindaja.format(new Date()) + " Toimusid tehingud: ");

        FileWriter kirjuta = new FileWriter(inventar, false);
        PrintWriter prindiFaili = new PrintWriter(kirjuta);

        int ostetudToodeteArv = 0; // Ühe kliendi kogu ostetud toodete arv
        for (int i = 0; i < tooted.length; i++) { //Käib selle tsükli läbi iga toote kohta poes
            //System.out.println(Arrays.toString(tooted));

            int praeguneKogus = loeInventar(tooted[i]); //Loeb praeguse toodete arvu failist
            //System.out.println(tooted[i]);
            //System.out.println(praeguneKogus);
            int ostukogus = suvaline.nextInt(maksimum) + 1;  //Genereerib suvailse arvu 0-3, kui palju käesolev klient seda toodet ostab
            ostetudToodeteArv += ostukogus; // Lisab selle ühe toote koguse kogu ostetud toodete arvule


            int uusKogus = praeguneKogus - ostukogus; // Lahutab praegusest kogusest kliendi ostetud koguse
            prindiFaili.println(tooted[i] + ", " + uusKogus); //Lisab uue  koguse faili
            logiPrintija.println(klientideLoendur + ". Klient ostis " + ostukogus + " " + tooted[i]); //kirjutab tehingute faili, kui palju midagi see klient  ostis
        }

        logiPrintija.close();
        logiKirjutaja.close();
        prindiFaili.close();
        kirjuta.close();
        klientideLoendur++;
        return ostetudToodeteArv;
    }

    /**
     *
     * @param toode  , mille praeguse koguse meetod praegu failist loeb
     * @return tagastab selle toote koguse failis
     * @throws IOException
     */
    private int loeInventar(String toode) throws IOException {

        try (Scanner lugeja = new Scanner(new File("inventaar.txt"), "UTF-8")) {
            while (lugeja.hasNextLine()) {
                String rida = lugeja.nextLine();
                //System.out.println(rida);
                String[] osad = rida.split(", ");
                //System.out.println(osad[0]);
                //System.out.println(osad[1]);
                if (osad[0].equals(toode)) {
                    int praeguneKogus = Integer.parseInt(osad[1]);
                    return praeguneKogus;
                }
            }
        }
        return 0;
    }
}
