package it.polito.tdp.imdb.model;

public class Arco implements Comparable<Arco>{
	
	private Director direttoreUno;
	private Director direttoreDue;
	private Integer peso;
	
	public Arco(Director direttoreUno, Director direttoreDue, Integer peso) {
		super();
		this.direttoreUno = direttoreUno;
		this.direttoreDue = direttoreDue;
		this.peso = peso;
	}

	public Director getDirettoreUno() {
		return direttoreUno;
	}

	public Director getDirettoreDue() {
		return direttoreDue;
	}

	public Integer getPeso() {
		return peso;
	}


	@Override
	public int compareTo(Arco other) {
		if(this.peso>other.peso)
		return -1;
		else if(this.peso<other.peso)
			return 1;
		else return 0;
	}
	
	

}
