package it.polito.tdp.PremierLeague.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.PremierLeague.model.Action;
import it.polito.tdp.PremierLeague.model.Adiacenza;
import it.polito.tdp.PremierLeague.model.Player;

public class PremierLeagueDAO {
	
	public List<Player> listAllPlayers(Map<Integer,Player> idMap){
		String sql = "SELECT * FROM Players";
		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
                if(idMap.get(res.getInt("PlayerID"))== null) {
				Player p = new Player(res.getInt("PlayerID"), res.getString("Name"));
				
				idMap.put(p.getPlayerID(), p);
				result.add(p);
			}else {
				result.add(idMap.get(res.getInt("PlayerID")));
			}
                }
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Action> listAllActions(){
		String sql = "SELECT * FROM Actions";
		List<Action> result = new ArrayList<Action>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Action action = new Action(res.getInt("PlayerID"),res.getInt("MatchID"),res.getInt("TeamID"),res.getInt("Starts"),res.getInt("Goals"),
						res.getInt("TimePlayed"),res.getInt("RedCards"),res.getInt("YellowCards"),res.getInt("TotalSuccessfulPassesAll"),res.getInt("totalUnsuccessfulPassesAll"),
						res.getInt("Assists"),res.getInt("TotalFoulsConceded"),res.getInt("Offsides"));
				
				result.add(action);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	public List<Integer> getGiocatori(Double goalX,Map<Integer,Player> idMap){
		
		String sql = "SELECT p1.PlayerID, AVG(p1.Goals) AS mediaG " + 
				"FROM actions AS p1 " + 
				"GROUP BY p1.PlayerID " + 
				"HAVING mediaG> ? ";
		List<Integer> giocatori = new ArrayList<>();
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setDouble(1, goalX);
			ResultSet res = st.executeQuery();
			
			while(res.next()) {
				
					
					
					
					giocatori.add(res.getInt("p1.PlayerID"));
					
				
			}
			conn.close();
			return giocatori;
			
			
		}catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	public List <Adiacenza> getAdiacenze(){
		String sql = "SELECT p1.PlayerID as p1, p2.PlayerID as p2, SUM(p1.TimePlayed) AS minutip1, SUM(p2.TimePlayed) AS minutip2 " + 
				"FROM actions p1, actions p2 " + 
				"WHERE p1.TeamID != p2.TeamID " + 
				"AND p1.MatchID = p2.MatchID " + 
				"AND p1.`Starts`=1 AND p2.`Starts`=1 " + 
				"GROUP BY p1.PlayerID, p2.PlayerID " + 
				"HAVING minutip1 > minutip2";
		List<Adiacenza> adiacenze = new ArrayList<>();
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			
			while(res.next()) {
				Integer p1= res.getInt("minutip1");
				Integer p2 = res.getInt("minutip2");
				
				Integer peso = p1-p2;
				
				adiacenze.add(new Adiacenza(res.getInt("p1"), res.getInt("p2"), peso));
				
				
				
				
			}
			conn.close();
			return adiacenze;	
			}catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
			
	}
}
