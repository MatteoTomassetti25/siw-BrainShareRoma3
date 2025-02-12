package it.uniroma3.siw.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Materia;
import it.uniroma3.siw.repository.MateriaRepository;

@Service
public class MateriaService {
	
	@Autowired
	protected MateriaRepository materiaRepository;
	
	public Materia findByNome(String nome) {
		Optional<Materia> materia = this.materiaRepository.findByNome(nome);
		return materia.orElse(null);
	}
	
	public Materia findById(Long Id) {
		
		Optional<Materia> materia = this.materiaRepository.findById(Id);
		return materia.orElse(null);	
	}
	
	public Materia findByTipo(String tipo) {
		Optional<Materia> materia = this.materiaRepository.findByTipo(tipo);
		return materia.orElse(null);
	}
	
	public List<Materia> findAll(){
		return materiaRepository.findAll();
	}
	
	public Materia save(Materia materia) {
		return this.materiaRepository.save(materia);
	}
	
	public void deleteById(Long id) {
        // Controlla se la materia esiste prima di eliminarla
        if (materiaRepository.existsById(id)) {
            materiaRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("La materia con ID " + id + " non esiste.");
        }
    }

}
