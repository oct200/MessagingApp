package repository.database;

import domain.Friendship;
import domain.Tuple;
import domain.Utilizator;
import domain.validators.Validator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FriendshipDatabaseRepository extends AbstractDatabaseRepository<Tuple<Long,Long>,Friendship> {
    public FriendshipDatabaseRepository(Validator<Friendship> validator, String tabelDat) {
        super(validator, tabelDat);
    }
    @Override
    public Friendship insertQuery(Friendship entity, Connection conn) {
        String sqlStat =  "INSERT INTO " + super.getTabel() + "(userId1,userId2,dateTime,status) VALUES (?,?,?,?)";
        try(PreparedStatement ps = conn.prepareStatement(sqlStat,Statement.RETURN_GENERATED_KEYS)){
            ps.setLong(1,entity.getId().getFirst());
            ps.setLong(2,entity.getId().getSecond());
            ps.setTimestamp(3,Timestamp.valueOf(entity.getDataPrietenie()));
            ps.setInt(4,entity.getStatus());
            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                entity.setId(new Tuple<Long,Long>(generatedKeys.getLong(1),generatedKeys.getLong(2)));
            } else {
                throw new SQLException("Sending message failed, no ID obtained.");
            }
            return entity;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public int deleteQuery(Tuple<Long, Long> longLongTuple,Connection conn) {
        String sqlStat = "DELETE FROM " + super.getTabel() + " WHERE (userId1 = ? AND userId2 = ?) OR (userId2 = ? AND userId1 = ?)";
        try(PreparedStatement ps = conn.prepareStatement(sqlStat)){
            ps.setLong(1,longLongTuple.getFirst());
            ps.setLong(2,longLongTuple.getSecond());
            ps.setLong(3,longLongTuple.getFirst());
            ps.setLong(4,longLongTuple.getSecond());

            return ps.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public int updateQuery(Friendship entity,Connection conn) {
        String sqlStat = "UPDATE  " + super.getTabel() + " SET userId1 = ?, userId2 = ?, dateTime = ?, status = ?  WHERE (userId1 = ? AND userId2 = ?) OR (userId2 = ? AND userId1 = ?)";
        try(PreparedStatement ps = conn.prepareStatement(sqlStat)){
            ps.setLong(1,entity.getId().getFirst());
            ps.setLong(2,entity.getId().getSecond());
            ps.setTimestamp(3,Timestamp.valueOf(entity.getDataPrietenie()));
            ps.setInt(4,entity.getStatus());
            ps.setLong(5,entity.getId().getFirst());
            ps.setLong(6,entity.getId().getSecond());
            ps.setLong(7,entity.getId().getFirst());
            ps.setLong(8,entity.getId().getSecond());

            return ps.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Friendship createEntity(ResultSet rs) throws SQLException {
        Friendship f  = new Friendship(rs.getTimestamp("dateTime").toLocalDateTime(),rs.getInt("status"));
        f.setId(new Tuple<>(rs.getLong("userId1"), rs.getLong("userId2")));
        return f;
    }

    /*public ArrayList<Friendship> getSentRequests(Long userId){
        ArrayList<Friendship> friendships = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sqlStat = "SELECT * FROM " + super.getTabel() + " WHERE userId1 = ? and status = 0";
            PreparedStatement ps = conn.prepareStatement(sqlStat);
            ps.setLong(1,userId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                friendships.add(createEntity(rs));
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        return friendships;
    }

    public ArrayList<Friendship> getReceivedRequests(Long userId){
        ArrayList<Friendship> friendships = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sqlStat = "SELECT * FROM " + super.getTabel() + " WHERE userId2 = ? and status = 0";
            PreparedStatement ps = conn.prepareStatement(sqlStat);
            ps.setLong(1,userId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                friendships.add(createEntity(rs));
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        return friendships;
    }

    public ArrayList<Friendship> getFriends(Long userId){
        ArrayList<Friendship> friendships = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sqlStat = "SELECT * FROM " + super.getTabel() + " WHERE (userId1 = ? OR userId2 = ?) AND status = 1";
            PreparedStatement ps = conn.prepareStatement(sqlStat);
            ps.setLong(1,userId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                friendships.add(createEntity(rs));
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        return friendships;
    }*/
}
