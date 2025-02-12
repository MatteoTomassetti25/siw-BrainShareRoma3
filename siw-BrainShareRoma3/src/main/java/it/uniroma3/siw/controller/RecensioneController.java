package it.uniroma3.siw.controller;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.controller.validator.ValidatoreRuolo;
import it.uniroma3.siw.model.Appunto;
import it.uniroma3.siw.model.Credenziali;
import it.uniroma3.siw.model.Recensione;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.service.AppuntoService;
import it.uniroma3.siw.service.RecensioneService;
import it.uniroma3.siw.service.UtenteService;

@Controller
public class RecensioneController {


	@Autowired
	private RecensioneService recensioneService;

	@Autowired
	private AppuntoService appuntoService;

	@Autowired
	private UtenteService utenteService;

	


	@GetMapping(value="/admin/operazioniRecensione")
	public String indexRecensione() {
		return "admin/operazioniRecensioni";
	}

	@GetMapping("/recensione/{id}")
	public String getRecensione(@PathVariable("id") Long id, Model model) {
		model.addAttribute("recensione", this.recensioneService.findById(id));
		return "recensione.html";
	}

	@GetMapping("/recensione")
	public String getRecensioni(Model model) {
		model.addAttribute("recensioni", this.recensioneService.findAll());
		return "recensioni.html";
	}

	
	@GetMapping("/admin/recensioni/{id}")
	public String visualizzaRecenzioni(@PathVariable Long id, Model model) {
		Appunto appunto = appuntoService.findById(id);
		
		model.addAttribute("appunto",appunto);
		model.addAttribute("recensioni",appunto.getRecensioni());
		
		return "admin/recensioni";
	}
	
	
	@GetMapping("/formNewRecensione/{id}")
    public String formNuovaRecensione(@PathVariable Long id, Model model, UserDetails userDetails) {
        // Recupera l'appunto selezionato
        Appunto appunto = appuntoService.findById(id);
        
        Utente utenteRecensione = this.utenteService.findByUsername(userDetails.getUsername());
        
        if (appunto == null) {
            return "error";
        }
        
        
        if(appunto.getUtente().getId().equals(utenteRecensione.getId())) {
        	
        	model.addAttribute("appunti", this.appuntoService.findAll());
        	model.addAttribute("messaggioErrore", "Non puoi recensire i tuoi appunti");
        	return "listaAppunti";
        	
        }
        
        

        // Recupera l'utente attualmente loggato
        Utente utente = utenteService.findByUsername(userDetails.getUsername());

        // Prepara il modello per la vista
        model.addAttribute("appunto", appunto);
        model.addAttribute("recensione", new Recensione());
        model.addAttribute("utente", utente);

        return "formNewRecensione"; // Pagina per inserire la recensione
    }

    @PostMapping("/salva")
    public String salvaRecensione(@RequestParam Long appuntoId, @RequestParam int stelle, 
                                  @RequestParam String commento, UserDetails userDetails, Model model) throws AccessDeniedException {

        // Recupera l'utente attualmente loggato
        Utente utente = utenteService.findByUsername(userDetails.getUsername());

        // Verifica il ruolo dell'utente
        if (!ValidatoreRuolo.utenteDefault(utente)) {
            throw new AccessDeniedException("Gli amministratori non possono lasciare recensioni");
        }

        // Recupera l'appunto selezionato
        Appunto appunto = appuntoService.findById(appuntoId);
        if (appunto == null) {
            return "error"; 
        }

        // Crea e salva la nuova recensione
        Recensione recensione = new Recensione();
        recensione.setUtente(utente);
        recensione.setAppunto(appunto);
        recensione.setStelle(stelle);
        recensione.setCommento(commento);
        recensione.setDataCommento(LocalDate.now());

        recensioneService.createRecensione(recensione, utente);

     // Aggiunge un messaggio di conferma al modello
        model.addAttribute("messaggio", "Recensione creata con successo!");
        
        // Reindirizza alla lista delle materie
        return "confermaRecensione";
    }
	
    
     

}
