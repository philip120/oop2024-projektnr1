import java.util.ArrayList;
import java.util.List;

public class Kasutaja {
    private String nimi;
    private int raha;
    private List<String> tooted;

    public Kasutaja(String nimi, int raha) {
        this.nimi = nimi;
        this.raha = raha;
        this.tooted = new ArrayList<>();
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public int getRaha() {
        return raha;
    }

    public void setRaha(int raha) {
        this.raha = raha;
    }

    public List<String> getTooted() {
        return tooted;
    }

    public void setTooted(List<String> tooted) {
        this.tooted = tooted;
    }

    public void lisaTooted(String toode){
        tooted.add(toode);
    }
}
