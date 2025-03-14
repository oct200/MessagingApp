package domain;

import java.time.LocalDateTime;

public class Mesaj extends Entity<Long>{
    String mesaj;
    Long from;
    Long to;
    LocalDateTime dateTime;
    Mesaj replyTo;


    public Mesaj(String mesaj, Long from, Long to, LocalDateTime dateTime,Mesaj replyTo) {
        this.mesaj = mesaj;
        this.from = from;
        this.replyTo = replyTo;
        this.to = to;
        this.dateTime = dateTime;
    }

    public Mesaj(String mesaj, Long from, Long to, LocalDateTime dateTime) {
        this.mesaj = mesaj;
        this.from = from;
        this.to = to;
        this.dateTime = dateTime;
    }

    public String getMesaj() {
        return mesaj;
    }

    public void setMesaj(String mesaj) {
        this.mesaj = mesaj;
    }

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public Long getTo() {
        return to;
    }

    public void setTo(Long to) {
        this.to = to;
    }

    public Mesaj getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(Mesaj replyTo) {
        this.replyTo = replyTo;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
