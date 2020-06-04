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
	
	/*public List<Player> listAllPlayers(Map<Integer,Player> idMap){
		String sql = "SELECT * FROM Players";
		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
                if(!idMap.containsKey(res.getInt("PlayerID"))) {
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
	}*/
	
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
	public List<Player> getGiocatori(Double goalX, Map<Integer,Player> idMap){
		
		String sql = "SELECT p1.PlayerID, p1.Name " + 
				"FROM  players p1, actions a1 " + 
				"WHERE p1.PlayerID= a1.PlayerID " + 
				"GROUP BY a1.PlayerID   " + 
				"HAVING  AVG(a1.Goals) >?";
		List<Player> giocatori = new ArrayList<>();
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setDouble(1, goalX);
			ResultSet res = st.executeQuery();
			
			while(res.next()) {
				 
				if(!idMap.containsKey(res.getInt("PlayerID"))) {
					Player p = new Player(res.getInt("p1.PlayerID"), res.getString("p1.Name"));
					idMap.put(p.getPlayerID(), p);
					giocatori.add(p);
					
	               }else {
	            	   giocatori.add(idMap.get(res.getInt("PlayerID")));
	               }
					
				    
			}
			conn.close();
			return giocatori;
			
			
		}catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	public List <Adiacenza> getAdiacenze(Map<Integer,Player> idMap){
		String sql = "SELECT a1.PlayerID, a2.PlayerID, SUM(a1.TimePlayed)-SUM(a2.TimePlayed)  AS differenza " + 
				"FROM   actions a1, actions a2  " + 
				"WHERE  a1.MatchID = a2.MatchID AND a1.`Starts`=1 AND a2.`Starts`=1 AND a1.TeamID != a2.TeamID " + 
				"GROUP BY a1.PlayerID, a2.PlayerID " + 
				"HAVING  differenza>0  " + 
				"ORDER BY differenza DESC";
		List<Adiacenza> adiacenze = new ArrayList<>();
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			
			while(res.next()) {
			
				adiacenze.add(new Adiacenza(idMap.get(res.getInt("a1.PlayerID")),idMap.get(res.getInt("a2.PlayerID")), res.getInt("differenza")));
				
				
				
				
			}
			conn.close();
			return adiacenze;	
			}catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
			
	}
}
