package services;

import ObserverClasses.ActionType;
import ObserverClasses.Observable;
import ObserverClasses.Observer;
import ObserverClasses.ObserverAction;
import domain.Friendship;
import domain.Tuple;
import kotlin.jvm.internal.FunctionReferenceImpl;
import repository.database.FriendshipDatabaseRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class ServiceFriendships implements Observable{
    FriendshipDatabaseRepository repo;
    ArrayList<Observer> observers;

    public ServiceFriendships(FriendshipDatabaseRepository repo) {
        this.repo = repo;
        observers = new ArrayList<>();
    }

    public Optional<Friendship> getFriendship(Long from, Long to) {
        return repo.findOne(new Tuple<>(from, to));
    }

    public void deleteFriendship(Long from, Long to){
        repo.delete(new Tuple<Long,Long>(from, to));
        ObserverAction action = new ObserverAction(ActionType.DELETEFRIEND, from, to);
        notifyObservers(action);
    }

    public void updateFriendship(Long from, Long to){
        System.out.println(from + " " + to);
        Optional<Friendship> f = repo.findOne(new Tuple<Long,Long>(from, to));
        if (f.isPresent()){
            Friendship friendship = f.get();
            friendship.setDataPrietenie(LocalDateTime.now());
            friendship.setStatus(1);
            repo.update(friendship);
            notifyObservers(new ObserverAction(ActionType.NEWFRIEND, to, from));
        }else{
            throw new RuntimeException("Friendship not found in service friendship");
        }
    }

    public void addRequest(Long from, Long to){
        Friendship f = new Friendship(LocalDateTime.now(),0);
        f.setId(new Tuple<Long,Long>(from, to));
        repo.save(f);
        notifyObservers(new ObserverAction(ActionType.NEWREQUEST, from, to));
    }

    public List<Friendship> getSentRequests(Long userId){
        return repo.getEntities().values().stream().filter(friendship -> Objects.equals(friendship.getId().getFirst(), userId) && friendship.getStatus() == 0).collect(Collectors.toList());
    }

    public List<Friendship> getReceivedRequests(Long userId){
        return repo.getEntities().values().stream().filter(friendship -> Objects.equals(friendship.getId().getSecond(), userId) && friendship.getStatus() == 0).collect(Collectors.toList());
    }

    public List<Friendship> getFriends(Long userId){
        return repo.getEntities().values().stream().filter(friendship -> (Objects.equals(friendship.getId().getSecond(), userId) || Objects.equals(friendship.getId().getFirst(), userId)) && friendship.getStatus() == 1).collect(Collectors.toList());
    }


    @Override
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
