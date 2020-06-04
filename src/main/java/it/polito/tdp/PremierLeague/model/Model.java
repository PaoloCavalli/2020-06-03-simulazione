package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {

	private PremierLeagueDAO dao;
	private Map<Integer,Player> idMap;
	private Graph <Player,DefaultWeightedEdge> grafo;
	
	
	public Model() {
		dao = new PremierLeagueDAO();
		idMap = new HashMap<>();
	}
	
	public void creaGrafo(Double goalX) {
		
		grafo = new SimpleDirectedWeightedGraph<Player, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
	        List<Player> giocatori = dao.getGiocatori(goalX, idMap);
			for(Player p: giocatori) {
				this.grafo.addVertex(p);
			}
		
	   List <Adiacenza> adiacenze =dao.getAdiacenze(idMap);
	        for (Adiacenza a: adiacenze ) {
	        	//cara if mi sei costata 4 ore .... Grazie!
	        	if(this.grafo.containsVertex(a.getP1()) && this.grafo.containsVertex(a.getP2())){
					
					Graphs.addEdge(this.grafo, a.getP1(), a.getP2() , a.getPeso());
	        	}			
				
			}
	}
	//PUNTO 1D
	public Player getBoomer()  {
		
		Player bestBoomer= null ;
		Integer b = 0;
		for(Player p : this.grafo.vertexSet()) {
			if(this.grafo.outDegreeOf(p)> b) {
		    b = this.grafo.outDegreeOf(p);
			bestBoomer = p;
		   }
		}
		return bestBoomer;
	}
	
	public List<PlayerAndPeso> getLoosers(Player boomer){
		
		 List<PlayerAndPeso> loosers = new ArrayList<>();
		 
	  for(Player p:  Graphs.successorListOf(this.grafo, boomer)) {
		  //ottengo il peso dell'arco
		  DefaultWeightedEdge e = this.grafo.getEdge(boomer, p);
		  Integer punteggio =  (int) this.grafo.getEdgeWeight(e);
		  loosers.add(new PlayerAndPeso(p,punteggio));
		  
	  }
	  return loosers;
	}
	//
	public int nVertici() {
		return	this.grafo.vertexSet().size();
		}
		public int nArchi() {
			return this.grafo.edgeSet().size();
		}
}
