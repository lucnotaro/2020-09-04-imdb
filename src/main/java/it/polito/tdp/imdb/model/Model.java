package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	private ImdbDAO dao;
	private Graph<Movie,DefaultWeightedEdge> graph;
	private List<Movie> allNodes;
	private List<Movie> allMovies;
	
	public Model() {
		this.dao=new ImdbDAO();
		this.allNodes=new ArrayList<>();
		this.allMovies=new ArrayList<>();
	}
	
	private void loadNodes(Double r) {
		if(this.allMovies.isEmpty())
			this.allMovies.addAll(this.dao.listMovies());
		if(this.allNodes.isEmpty())
			this.allNodes.addAll(this.dao.listNodes(r));
	}
	
	public void buildGraph(Double r) {
		this.loadNodes(r);
		this.graph=new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.graph,this.allMovies);
		for(Movie m1:this.allNodes) {
			for(Movie m2:this.allNodes) {
				Integer peso=this.getWeight(m1.getId(),m2.getId());
				if(peso>0 && m1.getId()!=m2.getId() && this.graph.getEdge(m1,m2)==null) {
					Graphs.addEdge(this.graph,m1,m2,peso);
				}
			}
		}
//		System.out.println("v: "+this.graph.vertexSet().size());
//		System.out.println("e: "+this.graph.edgeSet().size());
	}
	
	private Integer getWeight(Integer m1,Integer m2) {
		return this.dao.commonActorsOf(m1,m2);
	}

	public Integer Vsize() {
		// TODO Auto-generated method stub
		return this.graph.vertexSet().size();
	}
	public Integer Esize() {
		// TODO Auto-generated method stub
		return this.graph.edgeSet().size();
	}
	
}