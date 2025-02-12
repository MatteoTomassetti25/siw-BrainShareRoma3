package it.uniroma3.siw.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Materia {

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@SequenceGenerator(name = "materia_generator", sequenceName = "materia_seq", allocationSize = 1)
	private Long id;
	
	@Column(unique = true, nullable = false, length = 20)
	@NotBlank(message = "la materia non puo essere vuota")
	private String nome;
	
	@Column(nullable = false, length = 50)
	@NotBlank(message = "il tipo non può essere vuoto")
	private String tipo; //campo di interesse della materia

	@OneToMany(mappedBy = "materia", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appunto> appunti = new ArrayList<>(); // Inizializziamo la lista per evitare NullPointerException

	public Long getId() {
		return id;
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

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public List<Appunto> getAppunti() {
		return appunti;
	}

	public void setAppunti(List<Appunto> appunti) {
		this.appunti = appunti;
	}

	@Override
	public int hashCode() {
		return Objects.hash(appunti, id, nome, tipo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Materia other = (Materia) obj;
		return Objects.equals(appunti, other.appunti) && Objects.equals(id, other.id)
				&& Objects.equals(nome, other.nome) && Objects.equals(tipo, other.tipo);
	}
	
	
	
	
	
	
}
