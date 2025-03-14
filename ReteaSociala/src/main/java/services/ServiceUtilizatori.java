package services;

import domain.Utilizator;
import jdk.jshell.execution.Util;
import repository.database.UtilizatoriDatabaseRepository;

import java.util.Optional;

public class ServiceUtilizatori {
    private UtilizatoriDatabaseRepository repo;


    public ServiceUtilizatori(UtilizatoriDatabaseRepository repo) {
        this.repo = repo;
    }

    public Utilizator getUtilizator(Long userId){
        Optional<Utilizator> u = repo.findOne(userId);
        return u.isPresent() ? u.get() : null;
    }

    public void addUtilizator(String username, String password) {
        Optional<Utilizator> rez = repo.existaUsername(username);
        if(rez.isPresent()){
            throw new RuntimeException("Username already exists");
        }
        Utilizator u = new Utilizator(username, password);
        Optional<Utilizator> opt = repo.save(u);
        if (opt.isPresent()) {
            throw new RuntimeException("Error saving the user");
        }
    }

    public Optional<Utilizator> existaUtilizator(String username, String password) {
        Utilizator utilizator = new Utilizator(username, password);
        return repo.existaUtilizator(utilizator);
    }

    public Optional<Utilizator> existaUsername(String username) {
        return repo.existaUsername(username);
    }

    public void deleteAccount(Long userId){
        repo.delete(userId);
    }

}
