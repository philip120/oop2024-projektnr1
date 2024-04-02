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
        Scanner scanner = new Scanner(System.in);
        System.out.println("Sisesta nimi: ");
        String nimi = scanner.nextLine();

        System.out.println("Sisesta raha: ");
        int raha = scanner.nextInt();

        Laadung laadung = new Laadung(0);
        Thread thread = new Thread(laadung);//wrapib n-ö ära ja siis alustab tööd samaaegselt teise meetodiga
        Kliendid kliendid=new Kliendid(0);
        Thread thread2 =new Thread(kliendid);
        thread.start();
        thread2.start();

        Kasutaja kasutaja = new Kasutaja(nimi,raha);
        while (true){
            System.out.println("Kas soovid poodi minna? (y/n) ");
            String vastus = scanner.nextLine();

            if("y".equalsIgnoreCase(vastus)){
                List<String> saadaolevadTooted  = loeTooted("inventaar.txt");
                System.out.println("Mida soovid osta? Saadaolevad tooted: " + saadaolevadTooted);
                String toode = scanner.nextLine();
                if (saadaolevadTooted.contains(toode)) {
                    kasutaja.lisaTooted(toode);
                    System.out.println(toode + " lisatud!");
                    System.out.println(kasutaja.getTooted());
                }else{
                    System.out.println("Toodet ei ole saadaval või on otsas.");
                }
            } else if ("n".equalsIgnoreCase(vastus)) {
                System.out.println("Järgmise päevani!");
            } else {
                System.out.println("palun vasta y või n");
            }
        }


    }
}
