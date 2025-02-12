package it.uniroma3.siw.model;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

@Entity
public class Recensione {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recensione_generator")
	@SequenceGenerator(name = "recensione_generator", sequenceName = "recensione_seq", allocationSize = 1)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_Appunto", nullable = false)
	private Appunto appunto;
	
	@ManyToOne
	@JoinColumn(name = "id_Utente", nullable = false)
	private Utente utente;
	
	@Column(nullable = false)
	@Min(value=1, message="il valore non può essere minore di 1")
	@Max(value=5, message= "Il valore non può superare 5")
	private Integer stelle;
	
	@Column(unique = false, nullable = true)
	@Size(max=1000, message = "il commento non puo superare i 1000 caratteri")
	private String commento;
	
	@Column
	private LocalDate dataCommento;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Appunto getAppunto() {
		return appunto;
	}

	public void setAppunto(Appunto appunto) {
		this.appunto = appunto;
	}

	public Utente getUtente() {
		return utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	public Integer getStelle() {
		return stelle;
	}

	public void setStelle(Integer stelle) {
		this.stelle = stelle;
	}

	public String getCommento() {
		return commento;
	}

	public void setCommento(String commento) {
		this.commento = commento;
	}

	@Override
	public int hashCode() {
		return Objects.hash(appunto, commento, dataCommento, id, stelle, utente);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Recensione other = (Recensione) obj;
		return Objects.equals(appunto, other.appunto) && Objects.equals(commento, other.commento)
				&& Objects.equals(dataCommento, other.dataCommento) && Objects.equals(id, other.id)
				&& Objects.equals(stelle, other.stelle) && Objects.equals(utente, other.utente);
	}

	public LocalDate getDataCommento() {
		return dataCommento;
	}

	public void setDataCommento(LocalDate dataCommento) {
		this.dataCommento = dataCommento;
	}

	
	
	
	
	
	
	
}
