package it.uniroma3.siw.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;

import it.uniroma3.siw.model.Appunto;
import it.uniroma3.siw.model.Materia;
import it.uniroma3.siw.model.Utente;

public interface AppuntoRepository extends CrudRepository<Appunto, Long>{
	
	public boolean existsByNome(String nome);
	
	
//	public Iterable<Appunto> findAppuntiScarsi(long id);
	
	@Query("SELECT a FROM Appunto a ORDER BY a.nome ASC")
	public List<Appunto> findAll();
	
	
	public Optional<Appunto>  findByNome(String nome);
	
	
	
	public Optional<Appunto>  findById(Long id);
	
	
	public Optional<Appunto>  findByMateria(Materia materia);
	
	public List<Appunto>   findByUtente(Utente utente);
	
	@Query("SELECT a FROM Appunto a WHERE LOWER(a.nome) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
	           "OR LOWER(a.utente.nome) LIKE LOWER(CONCAT('%', :keyword, '%'))")
	public List<Appunto> findByKeyword(@Param("keyword") String keyword, @Param("keyword") String keyword2);
	
	public void deleteById(Long id);

}
