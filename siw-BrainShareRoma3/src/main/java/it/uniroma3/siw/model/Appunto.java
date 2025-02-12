package it.uniroma3.siw.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Appunto {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@SequenceGenerator(name = "appunto_generator", sequenceName = "appunto_seq", allocationSize = 1)
	private Long id;
	
	@Column(unique = true, nullable = false, length = 200)
	@NotBlank(message = "devi inserire obbligatoriamente un titolo")
	private String nome;
	
	@NotBlank(message = "devi inserire obbligatoriamente un testo")
	private String contenuto;
	
	@Column
	private LocalDate dataCreazione;
	
	@ManyToOne
	@JoinColumn(name = "id_Utente", nullable = false)
	private Utente utente;

    @ManyToOne
    @JoinColumn(name = "id_Materia", nullable = false)
    private Materia materia;

    @OneToMany(mappedBy = "appunto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Recensione> recensioni = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public LocalDate getDataCreazione() {
		return dataCreazione;
	}

	public void setDataCreazione(LocalDate dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getContenuto() {
		return contenuto;
	}

	public void setContenuto(String contenuto) {
		this.contenuto = contenuto;
	}
//
//	public Long getIdMateria() {
//		return idMateria;
//	}
//
//	public void setIdMateria(Long idMateria) {
//		this.idMateria = idMateria;
//	}

	public List<Recensione> getRecensioni() {
		return recensioni;
	}

	public void setRecensioni(List<Recensione> recensioni) {
		this.recensioni = recensioni;
	}

	public Materia getMateria() {
		return materia;
	}

	public void setMateria(Materia materia) {
		this.materia = materia;
	}
	
	
	
	public Utente getUtente() {
		return utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	public double getVoto() {
		
		double somma = 0;
		
		List<Integer> listaStelle = new ArrayList<Integer>();
		
		for(Recensione elemento : this.getRecensioni()) {
			listaStelle.add(elemento.getStelle());
		}
		
		for(Integer stella : listaStelle) {
			somma += stella;
		}
		
		double mediaStelle = (double) somma / listaStelle.size();
		
		return mediaStelle;
	}
	

	@Override
	public int hashCode() {
		return Objects.hash(contenuto, id, materia, nome, recensioni);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Appunto other = (Appunto) obj;
		return Objects.equals(contenuto, other.contenuto) && Objects.equals(id, other.id)
				 && Objects.equals(materia, other.materia)
				&& Objects.equals(nome, other.nome) && Objects.equals(recensioni, other.recensioni);
	}
	
	
	
}
