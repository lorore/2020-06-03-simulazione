package it.polito.tdp.PremierLeague.model;

public class Battuto implements Comparable<Battuto>{
	private Player p;
	private double peso;
	public Battuto(Player p, double peso) {
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
	public double getPeso() {
		return peso;
	}
	public void setPeso(double peso) {
		this.peso = peso;
	}
	@Override
	public String toString() {
		return "Battuto [p=" + p + ", peso=" + peso + "]";
	}
	@Override
	public int compareTo(Battuto o) {
		return -Double.compare(peso, o.peso);
	}
	
	
	
	

}
