package repository.database;

import domain.Friendship;
import domain.Mesaj;
import domain.Utilizator;
import domain.validators.Validator;

import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;

public class MesajeDatabaseRepository extends AbstractDatabaseRepository<Long, Mesaj> {
    public MesajeDatabaseRepository(Validator<Mesaj> validator, String tabelDat) {
        super(validator, tabelDat);
    }

    @Override
    public Mesaj insertQuery(Mesaj mesaj, Connection conn) {
        String sqlStat = "INSERT INTO " + super.getTabel() + " (mesaj,fromUser,toUser,replyTo,datetime) VALUES (?,?,?,?,?)";
        try(PreparedStatement ps = conn.prepareStatement(sqlStat,Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1,mesaj.getMesaj());
            ps.setLong(2,mesaj.getFrom());
            ps.setLong(3,mesaj.getTo());
            Mesaj replyto = mesaj.getReplyTo();
            if (replyto != null) {
                ps.setLong(4,replyto.getId());
            }
            else{
                ps.setNull(4, java.sql.Types.BIGINT);
            }
            ps.setTimestamp(5,Timestamp.valueOf(mesaj.getDateTime()));
            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                mesaj.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("Sending message failed, no ID obtained.");
            }
            return mesaj;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public int deleteQuery(Long aLong, Connection conn) {
        String sqlStat = "DELETE FROM " + super.getTabel() + " WHERE mesId = ?";
        try(PreparedStatement ps = conn.prepareStatement(sqlStat)){
            ps.setLong(1,aLong);
            return ps.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public int updateQuery(Mesaj mesaj, Connection conn) {
        String sqlStat = "UPDATE " + super.getTabel() + " SET mesaj = ?, fromUser = ?, toUser = ?, replyTo = ?, datetime = ? WHERE mesId = ?";
        try(PreparedStatement ps = conn.prepareStatement(sqlStat)){
            ps.setString(1,mesaj.getMesaj());
            ps.setLong(2,mesaj.getFrom());
            ps.setLong(3,mesaj.getTo());
            Mesaj reply = mesaj.getReplyTo();
            if (reply != null) {
                ps.setLong(4, mesaj.getReplyTo().getId());
            }
            else{
                ps.setNull(4, java.sql.Types.BIGINT);
            }
            ps.setTimestamp(5,Timestamp.valueOf(mesaj.getDateTime()));
            return ps.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Mesaj createEntity(ResultSet rs) throws SQLException {
        Long replyTo = rs.getLong("replyTo");
        Mesaj mesNou;
        if(!rs.wasNull()){
            Optional<Mesaj> mesReply = super.findOne(replyTo);
            if (mesReply.isPresent()) {
                mesNou = new Mesaj(rs.getString("mesaj"), rs.getLong("fromUser"), rs.getLong("toUser"),rs.getTimestamp("dateTime").toLocalDateTime(),mesReply.get());
                mesNou.setId(rs.getLong("mesid"));
                return mesNou;
            }
        }
        mesNou = new Mesaj(rs.getString("mesaj"),rs.getLong("fromUser"),rs.getLong("toUser"),rs.getTimestamp("dateTime").toLocalDateTime());
        mesNou.setId(rs.getLong("mesid"));
        return mesNou;
    }

    public ArrayList<Mesaj> getMessagesByUsers(Long userId1, Long userId2){
        ArrayList<Mesaj> mesaje = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sqlStat = "SELECT * FROM " + super.getTabel() + " WHERE (toUser = ? AND fromUser = ?) OR (fromUser = ? AND toUser = ?)";
            PreparedStatement ps = conn.prepareStatement(sqlStat);
            ps.setLong(1,userId1);
            ps.setLong(2,userId2);
            ps.setLong(3,userId1);
            ps.setLong(4,userId2);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                mesaje.add(createEntity(rs));
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        return mesaje;
    }
}
