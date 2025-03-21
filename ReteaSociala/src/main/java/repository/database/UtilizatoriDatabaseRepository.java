package repository.database;

import domain.Entity;
import domain.Utilizator;
import domain.validators.Validator;

import java.sql.*;
import java.util.HashMap;
import java.util.Optional;

public class UtilizatoriDatabaseRepository extends AbstractDatabaseRepository<Long,Utilizator> {
    public UtilizatoriDatabaseRepository(Validator<Utilizator> validator, String tabelDat) {
        super(validator, tabelDat);
    }

    @Override
    public int deleteQuery(Long aLong, Connection conn) {
        String sqlStat = "DELETE FROM " + super.getTabel() + " WHERE userId = ?";
        try(PreparedStatement ps = conn.prepareStatement(sqlStat)){
            ps.setLong(1,aLong);
            return ps.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Utilizator insertQuery(Utilizator utilizator,Connection conn) {
        String sqlStat = "INSERT INTO " + super.getTabel() + "(username,parola,imagine) VALUES (?,?,?)";
        try(PreparedStatement ps = conn.prepareStatement(sqlStat,Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1,utilizator.getUsername());
            ps.setString(2,utilizator.getParola());
            if(utilizator.getImagineProfil() != null){
                ps.setObject(3,utilizator.getImagineProfil());
            }
            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                utilizator.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }
            return utilizator;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public int updateQuery(Utilizator utilizator,Connection conn) {
        String sqlStat = "UPDATE " + super.getTabel() + " SET username = ? , parola = ?, image = ? WHERE userId = ?";
        try(PreparedStatement ps = conn.prepareStatement(sqlStat)){
            ps.setString(1,utilizator.getUsername());
            ps.setString(2,utilizator.getParola());
            ps.setLong(3,utilizator.getId());
            return ps.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Utilizator createEntity(ResultSet rs) throws SQLException {
        Utilizator u = new Utilizator(rs.getString("username"),rs.getString("parola"));
        u.setId(rs.getLong("userId"));
        return u;
    }

    public Optional<Utilizator> existaUtilizator(Utilizator utilizator){
        super.validator.validate(utilizator);
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sqlStat = "SELECT * FROM " + super.getTabel() + " WHERE username = ? AND parola = ?";
            PreparedStatement ps = conn.prepareStatement(sqlStat);
            ps.setString(1,utilizator.getUsername());
            ps.setString(2,utilizator.getParola());

            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                Utilizator u = new Utilizator(resultSet.getString("username"),resultSet.getString("parola"));
                u.setId(resultSet.getLong("userId"));
                return Optional.of(u);
            }
            else{
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Utilizator> existaUsername(String utilizator){
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sqlStat = "SELECT * FROM " + super.getTabel() + " WHERE username = ?";
            PreparedStatement ps = conn.prepareStatement(sqlStat);
            ps.setString(1,utilizator);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()){
                Utilizator rez = new Utilizator(resultSet.getString("username"),resultSet.getString("parola"));
                rez.setId(resultSet.getLong("userId"));
                return Optional.of(rez);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
