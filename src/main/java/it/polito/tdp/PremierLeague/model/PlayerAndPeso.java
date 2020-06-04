package it.polito.tdp.PremierLeague.model;

public class PlayerAndPeso implements Comparable<PlayerAndPeso>{
 Player p;
 Integer peso;
 
 
public PlayerAndPeso(Player p, Integer peso) {
	super();
	this.p = p;
	this.peso = peso;
}


public Player getP() {
	return p;
}


public void setP(Player p) {
	this.p = p;
}


public Integer getPeso() {
	return peso;
}


public void setPeso(Integer peso) {
	this.peso = peso;
}


@Override
public int compareTo(PlayerAndPeso o) {
	
	return  -this.peso.compareTo(o.getPeso());
}


@Override
public String toString() {
	return   p+" "+"|" +peso ;
}
 

 
}
