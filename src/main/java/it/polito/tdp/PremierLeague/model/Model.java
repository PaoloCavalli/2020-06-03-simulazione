package it.polito.tdp.PremierLeague.model;

import java.util.HashMap;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {

	private PremierLeagueDAO dao;
	private Map<Integer,Player> idMap;
	private Graph <Integer,DefaultWeightedEdge> grafo;
	
	
	public Model() {
		dao = new PremierLeagueDAO();
		idMap = new HashMap<>();
	}
	
	public void creaGrafo(Double goalX) {
		
		grafo = new SimpleDirectedWeightedGraph<Integer, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
	
			
			Graphs.addAllVertices(this.grafo,this.dao.getGiocatori(goalX, idMap) );
			
		
		

		
		for (Adiacenza a: dao.getAdiacenze()) {
			if(a.getPeso() > 0) {
				
				
				Graphs.addEdge(this.grafo, a.getP1(), a.getP2() , a.getPeso());
			}
		}
		
	}
	public int nVertici() {
		return	this.grafo.vertexSet().size();
		}
		public int nArchi() {
			return this.grafo.edgeSet().size();
		}
}
