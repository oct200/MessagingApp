package domain;

import java.awt.*;
import java.util.List;
import java.util.Objects;

public class Utilizator extends Entity<Long>{
    private String username;
    private String parola;
    private Image imagineProfil;

    public Utilizator(String username, String parola) {
        this.username = username;
        this.parola = parola;
    }

    public Utilizator(String username, String parola, Image imagineProfil) {
        this.username = username;
        this.parola = parola;
        this.imagineProfil = imagineProfil;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getParola() {
        return parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

    public Image getImagineProfil() {
        return imagineProfil;
    }

    public void setImagineProfil(Image imagineProfil) {
        this.imagineProfil = imagineProfil;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Utilizator that = (Utilizator) o;
        return Objects.equals(username, that.username) && Objects.equals(parola, that.parola);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, parola);
    }

    @Override
    public String toString() {
        return "Utilizator{" +
                "id" + getId().toString() +
                "username='" + username + '\'' +
                ", parola='" + parola + '\'' +
                '}';
    }
}