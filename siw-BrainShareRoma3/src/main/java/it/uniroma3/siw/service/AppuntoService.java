package it.uniroma3.siw.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Appunto;
import it.uniroma3.siw.model.Materia;
import it.uniroma3.siw.model.Recensione;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.repository.AppuntoRepository;

@Service
public class AppuntoService {
	
	@Autowired
	protected AppuntoRepository appuntoRepository;
	
	public Appunto findByNome(String nome) {
		
		Optional<Appunto> appunto = this.appuntoRepository.findByNome(nome);
		return appunto.orElse(null);
	}
	
	
	public Appunto findById(Long Id) {
		
		Optional<Appunto> appunto= this.appuntoRepository.findById(Id);
		return appunto.orElse(null);
	}
	
	
	public Appunto findByMateria(Materia materia) {
		
		Optional<Appunto> appunto = this.appuntoRepository.findByMateria(materia);
		return appunto.orElse(null);
	}
	
	public List<Appunto> findAll(){
		
		return this.appuntoRepository.findAll();
	}
	
	public Appunto save(Appunto appunto) {
		return this.appuntoRepository.save(appunto);
	}
	
	public List<Appunto> appuntiScarsi(){
		List<Appunto> result = new ArrayList<Appunto>();
		
		for(Appunto elemento : this.appuntoRepository.findAll()) {
			if(elemento.getVoto()<3) {
				result.add(elemento);
			}
				
		}
		
		return result;
	}
 
	
	public void deleteById(Long id) {
        // Controlla se la materia esiste prima di eliminarla
        if (appuntoRepository.existsById(id)) {
            appuntoRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("L'Appunto con ID " + id + " non esiste.");
        }
    }
	
	
	public List<Appunto> findByUtente(Utente utente) {
		return appuntoRepository.findByUtente(utente);
	}
	
	public List<Appunto> searchByKeyword(String keyword) {
        return appuntoRepository.findByKeyword(keyword, keyword);
    }
	
}
