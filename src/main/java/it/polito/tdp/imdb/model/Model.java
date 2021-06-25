package it.polito.tdp.imdb.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {

	private Graph<Director, DefaultWeightedEdge> grafo;
	private ImdbDAO dao;
	private Map<Integer, Director> directorMap;
	private List<Director> soluzioneMigliore;
	private int attoriCondivisi;
	
	
	public Model() {
		dao = new ImdbDAO();
		directorMap = new HashMap<>();
		dao.listAllDirectors(directorMap);
		
	}
	
	public String creaGrafo(Integer anno) {
		
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//aggiungo vertici
		Graphs.addAllVertices(this.grafo, dao.getVertexDirector(directorMap, anno));
		
		//aggiungo archi
		for(Arco a : dao.getArchi(directorMap, anno)) {
			Graphs.addEdge(this.grafo, a.getDirettoreUno(), a.getDirettoreDue(), a.getPeso());
		}
		
		return String.format("Numero di vertici: %d \nNumero di archi: %d\n\n", this.grafo.vertexSet().size(), this.grafo.edgeSet().size());
	}

	public Set<Director> getRegisti() {
		
		return this.grafo.vertexSet();
	}

	public List<Arco> getAdiacenti(Director direttore) {
		
		List<Director> elenco = Graphs.neighborListOf(this.grafo, direttore);
		List<Arco> result = new LinkedList<>();
	
		for(Director d : elenco) {
			DefaultWeightedEdge a = this.grafo.getEdge(direttore, d);
			Arco aa = new Arco(direttore, d, (int)this.grafo.getEdgeWeight(a));
			result.add(aa);
		}
		Collections.sort(result);
		
		
		return result;
	}
	
	
	public List<Director> doRicorsione(Integer c, Director partenza) {
		
		soluzioneMigliore = new LinkedList<>();
		List<Director> parziale = new LinkedList<>();
		parziale.add(partenza);
		cerca(parziale, c, 0);
		
		return this.soluzioneMigliore;
		
	}

	private void cerca(List<Director> parziale, Integer c, int pesoCumulato) {
		
		if(pesoCumulato>c) {
			return;
		}else {
			
			if(parziale.size()>this.soluzioneMigliore.size()) {
				this.soluzioneMigliore = new LinkedList<>(parziale);
				this.attoriCondivisi = pesoCumulato;
			}
			
			Director ultimo = parziale.get(parziale.size()-1);
			List<Director> adiacenti = Graphs.neighborListOf(this.grafo, ultimo);
			
			//controllo gli adiacenti
			for(Director vicino : adiacenti) {
				if(!parziale.contains(vicino)) {
				//aggiungo regista
				parziale.add(vicino);
				
				//calcolo il peso cumulato                                    ultimo						penultimo
				DefaultWeightedEdge arco = this.grafo.getEdge(parziale.get(parziale.size()-1), parziale.get(parziale.size()-2));
				
				
					int peso = (int)this.grafo.getEdgeWeight(arco);
					pesoCumulato += peso;
				
					//faccio ricorsione
					cerca(parziale, c, pesoCumulato);
				
					//backtracking
					pesoCumulato = pesoCumulato-peso; //riporto il peso precendente
					parziale.remove(parziale.get(parziale.size()-1)); //elimino il vicino aggiunto
				}
			}
		}
		
		
	}
	
	public int attoriCondivisi() {
		return this.attoriCondivisi;
	}

	
	
	
	
}
