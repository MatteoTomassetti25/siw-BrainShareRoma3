package it.uniroma3.siw.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Materia;

public interface MateriaRepository extends CrudRepository<Materia, Long> {
	
	public boolean existsByTipo(String tipo);
	
	public Optional<Materia>  findByTipo(String tipo);
	
	public Optional<Materia>  findById(Long id);
	
	public boolean existsByNome(String nome);
	
	@Query("SELECT m FROM Materia m WHERE LOWER(m.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
	public Optional<Materia>  findByNome(String nome);
	
	public List<Materia> findAll();
	
	public void deleteById(Long id);

}
