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
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Utente {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "utente_generator")
	@SequenceGenerator(name = "utente_generator", sequenceName = "utente_seq", allocationSize = 1)
	private Long id;

    @NotBlank(message = "Il nome non può essere vuoto")
    @Size(min = 2, max = 50, message = "Il nome deve avere tra 2 e 50 caratteri")
	private String nome;
	
    @NotBlank(message = "Il cognome non può essere vuoto")
    @Size(min = 2, max = 50, message = "Il cognome deve avere tra 2 e 50 caratteri")
	private String cognome;

	@Column(unique = true)
	@NotBlank(message = "L'email non può essere vuota")
    @Email(message = "L'email deve essere valida")
	private String email;

	@OneToMany(mappedBy = "utente", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Recensione> recensioni = new ArrayList<Recensione>(); //collegamento tabella utenti (id_utente)
	
	@OneToOne(mappedBy = "utente")
	private Credenziali credenziali;
	
	@OneToMany(mappedBy = "utente", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Appunto> appunti = new ArrayList<Appunto>();

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

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Recensione> getRecensioni() {
		return recensioni;
	}

	public void setRecensioni(List<Recensione> recensioni) {
		this.recensioni = recensioni;
	}

	public Credenziali getCredenziali() {
		return credenziali;
	}

	public void setCredenziali(Credenziali credenziali) {
		this.credenziali = credenziali;
	}
	
	

	public List<Appunto> getAppunti() {
		return appunti;
	}

	public void setAppunti(List<Appunto> appunti) {
		this.appunti = appunti;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cognome, credenziali, email, id, nome, recensioni);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Utente other = (Utente) obj;
		return Objects.equals(cognome, other.cognome) && Objects.equals(credenziali, other.credenziali)
				&& Objects.equals(email, other.email) && Objects.equals(id, other.id)
				&& Objects.equals(nome, other.nome) && Objects.equals(recensioni, other.recensioni);
	}
	
	
	
}
