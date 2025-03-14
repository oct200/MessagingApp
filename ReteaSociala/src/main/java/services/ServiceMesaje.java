package services;

import ObserverClasses.ActionType;
import ObserverClasses.Observable;
import ObserverClasses.Observer;
import ObserverClasses.ObserverAction;
import domain.Mesaj;
import repository.database.MesajeDatabaseRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class ServiceMesaje implements Observable {
    MesajeDatabaseRepository repo;
    ArrayList<Observer> observers;

    public ServiceMesaje(MesajeDatabaseRepository mesajeRepository) {
        observers = new ArrayList<>();
        this.repo = mesajeRepository;
    }

    public List<Mesaj> getMesajeUseri(Long userId1, Long userId2){
        System.out.println("asdsee");
        return repo.getEntities().values().stream()
                .filter(mesaj->{
            return (Objects.equals(mesaj.getTo(), userId1) && Objects.equals(mesaj.getFrom(), userId2)) || (Objects.equals(mesaj.getTo(), userId2) && Objects.equals(mesaj.getFrom(), userId1));
        })
                .sorted(Comparator.comparing(Mesaj::getDateTime))
                .collect(Collectors.toList());
    }

    public Mesaj getMesajById(Long id){
        try {
            return repo.findOne(id).get();
        }
        catch(Exception e){
            throw new RuntimeException("mesajul cu id-ul dat nu s-a gasit");
        }
    }

    public void addMesaj(String mesaj, Long from, Long to, Long replyTo){
        Mesaj mes;
        if(replyTo == null) {
            mes = new Mesaj(mesaj, from, to, LocalDateTime.now());
        }
        else{
            Optional<Mesaj> reply = repo.findOne(replyTo);
            if (reply.isPresent()) {
                mes = new Mesaj(mesaj, from, to, LocalDateTime.now(), reply.get());
            }
            else{
                mes = new Mesaj(mesaj, from, to, LocalDateTime.now());
            }
        }
        repo.save(mes);
        notifyObservers(new ObserverAction(ActionType.NEWMESSAGE,from,to));
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(ObserverAction action) {
        observers.forEach(x -> x.update(action));
    }

}
