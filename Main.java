public class Main {
    public static void main(String[] args) {//kasutades runnable interface saame mitu threadi(lõime) korraga kasutada
        Laadung laadung = new Laadung();
        Thread thread = new Thread(laadung);//wrapib n-ö ära ja siis alustab tööd samaaegselt teise meetodiga
        Kliendid kliendid=new Kliendid();
        Thread thread2 =new Thread(kliendid);
        thread.start();
        thread2.start();
    }
}
