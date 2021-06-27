package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {

	private Graph<Player, DefaultWeightedEdge> graph;
	private PremierLeagueDAO dao;
	private Map<Integer, Player> idMap;
	private double max;
	private List<Player> soluzione;
	
	
	public Model() {
		dao=new PremierLeagueDAO();
	}
	
	public String creaGrafo(double x) {
		String s="";
		idMap=new HashMap<>();
		graph=new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		dao.getVertici(idMap, x);
		Graphs.addAllVertices(graph, idMap.values());
		s="Num vertici: "+this.graph.vertexSet().size();
		List<Adiacenza> archi=dao.getArchi(idMap, x);
		for(Adiacenza a: archi) {
			DefaultWeightedEdge e=graph.getEdge(a.getP1(), a.getP1());
			if(e==null) {
				Graphs.addEdge(graph, a.getP1(), a.getP2(), a.getPeso());
			}
		}
		s+=" num archi: "+this.graph.edgeSet().size();
		//System.out.println(s);
		return s;
		
	}
	
	public Player getMigliore() {
		int max=0;
		Player migliore=null;
		for(Player p: this.graph.vertexSet()) {
			int n=graph.outDegreeOf(p);
			if(n>max) {
				max=n;
				migliore=p;
			}
		}
		System.out.println(migliore);
		return migliore;
	}
	
	public List<Battuto> getBattuti(Player p){
		List<Battuto> result=new ArrayList<>();
		for(DefaultWeightedEdge e:graph.outgoingEdgesOf(p)) {
			double peso=graph.getEdgeWeight(e);
			result.add(new Battuto(Graphs.getOppositeVertex(graph, e, p), peso ));
		}
		Collections.sort(result);
		return result;
	}
	
	public List<Player> avviaRicorsione(int k){
		this.max=0.0;
		this.soluzione=null;
		List<Player> parziale=new ArrayList<Player>();
		this.doRicorsione(parziale, k, 0);
		//System.out.println(soluzione+" "+this.max);
		return soluzione;
		}
	
	private void doRicorsione(List<Player> parziale, int k, int livello) {
		if(parziale.size()==k) {
			//raggiunto la dimensione massima del dreamTeam
			double tot=this.calcolaMax(parziale);
			if(tot>this.max) {
				max=tot;
				this.soluzione=new ArrayList<>(parziale);
				return;
			}
			return;
		}
		
		for(Player p: this.graph.vertexSet()) {
			
			if(!parziale.contains(p) && !this.getMappaNera(parziale).containsKey(p.getPlayerID())) {
				List<Battuto> battuti=this.getBattuti(p);
				parziale.add(p);
				this.doRicorsione(parziale, k, livello+1);
				parziale.remove(parziale.size()-1);
				
			}
		}
	}
	
	private double calcolaMax(List<Player> lista) {
		double result=0;
		for(Player p: lista) {
			result+=this.calcolaGradoTitolarita(p);
		}
		return result;
	}
	
	private double calcolaGradoTitolarita(Player p) {
		double sommaUscenti=0.0;
		for(DefaultWeightedEdge e : graph.outgoingEdgesOf(p)) {
			sommaUscenti+=this.graph.getEdgeWeight(e);
		}
		double sommaEntranti=0.0;
		for(DefaultWeightedEdge e: graph.incomingEdgesOf(p)) {
			sommaEntranti+=this.graph.getEdgeWeight(e);
		}
		return (sommaUscenti-sommaEntranti);
	}
	
	public double getMaxDreamTeam() {
		return this.max;
	}
	
	private Map<Integer, Player> getMappaNera(List<Player> lista){
		Map<Integer, Player> mappaNera=new HashMap<>();
		for(Player p: lista) {
			List<Battuto> battuti=this.getBattuti(p);
			for(Battuto b: battuti) {
				if(!mappaNera.containsKey(b.getP().getPlayerID()))
					mappaNera.put(b.getP().getPlayerID(), b.getP());
			}
		}
		return mappaNera;
	}
}
