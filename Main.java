import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static List<String> loeTooted(String fail) throws Exception{
        List<String> tulemus = new ArrayList<>();
        try (Scanner lugeja = new Scanner(new File(fail))){
            while ((lugeja.hasNextLine())){
                String rida = lugeja.nextLine();
                String[] osad = rida.split(", ");
                String toode = osad[0];
                int kogus = Integer.parseInt(osad[1]);
                if (kogus>0){
                    tulemus.add(toode);
                }
            }
        }
        return tulemus;
    }

    public static void main(String[] args) throws Exception{//kasutades runnable interface saame mitu threadi(lõime) korraga kasutada
        System.out.println("Poe simulatsioon.");
        System.out.println("Tegime projekti, kus omanik peab enda poodi ülal pidama samal ajal kui inimesed ostlevad");
        System.out.println("Kasutaja ülesanne on teha mõned otsused, et pood kasumlikuks teha.");
        System.out.println("Alguses küsitakse poe algkapitali, mis ei tohi olla väiksem kui poe kasum");
        System.out.println("Et poodi juhtida on omanikul iga hetk võimalik otsustada, et teha reklaami või tõsta hindu");
        System.out.println("Alustuseks vali algkapital ning alusta simulatsiooni!");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Vali algkapital.(kirjuta number)");
        int algkapital = scanner.nextInt();
        Omanik omanik = new Omanik(algkapital);



        Laadung laadung = new Laadung(0);
        Thread thread = new Thread(laadung);//wrapib n-ö ära ja siis alustab tööd samaaegselt teise meetodiga

        Kliendid kliendid=new Kliendid(10, 1);
        Thread thread2 =new Thread(kliendid);
        thread.start();
        Thread.sleep(5000);
        thread2.start();

        while (true){
            double kogukasum = kliendid.getKoguKasum();
            int algkapital2 = omanik.getRaha();
            if (kogukasum + algkapital2 < 0) { // kontrollib kas on algkapital on läinud
                System.out.println("Pood läks pankrotti");
                System.exit(0);
            }
            System.out.println("Mida soovid teha? sisestaga r(reklaami) või h(Hinda tõsta) PS. hooliv poeomanik tegeleb iga päev oma poega.");
            String  vastus = scanner.nextLine();
            if ("r".equalsIgnoreCase(vastus)) {
                int esialgne = kliendid.getEsialgneKlientideArv();
                int uus = esialgne + 10;
                kliendid.setEsialgneKlientideArv(uus);
                //EI TOOOOTA!
                double summa = kliendid.getKoguKasum();
                kliendid.setKoguKasum(summa-50);
                // System.out.println(kliendid.getEsialgneKlientideArv());
            } else if ("h".equalsIgnoreCase(vastus)) {
                double endineKordaja = kliendid.getTooteHind(); //edasimüümise kordaja tõstmine 0.1 võrra
                double uusKordaja = endineKordaja + 0.1;
                kliendid.setTooteHind(uusKordaja);
                //System.out.println(kliendid.getTooteHind());
                int esialgne = kliendid.getEsialgneKlientideArv(); //hinna tõstmine vähendab potentsiaalsete klientide arvu
                int uus = esialgne -2;
                kliendid.setEsialgneKlientideArv(uus);
                //System.out.println(kliendid.getEsialgneKlientideArv());
            }



        }


    }
}
