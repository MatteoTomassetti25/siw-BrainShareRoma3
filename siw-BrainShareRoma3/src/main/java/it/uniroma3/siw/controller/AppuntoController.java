package it.uniroma3.siw.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.controller.validator.AppuntoValidator;
import it.uniroma3.siw.model.Appunto;
import it.uniroma3.siw.model.Credenziali;
import it.uniroma3.siw.model.Materia;
import it.uniroma3.siw.model.Recensione;
import it.uniroma3.siw.repository.AppuntoRepository;
import it.uniroma3.siw.repository.CredenzialiRepository;
import it.uniroma3.siw.repository.UtenteRepository;
import it.uniroma3.siw.service.AppuntoService;
import it.uniroma3.siw.service.MateriaService;
import it.uniroma3.siw.service.RecensioneService;
import jakarta.validation.Valid;

@Controller
public class AppuntoController {

	@Autowired
	protected AppuntoService appuntoService;

	@Autowired
	protected AppuntoRepository appuntoRepository;

	@Autowired
	protected MateriaService materiaService;

	@Autowired
	protected UtenteRepository utenteRepository;

	@Autowired
	protected AppuntoValidator appuntoValidator;
	
	@Autowired
	protected CredenzialiRepository credenzialiRepository;
	
	@Autowired
	protected RecensioneService recensioneService;
	


	@GetMapping("/formNewAppunto")
	public String formNewAppunto(Model model, @AuthenticationPrincipal UserDetails currentUser) {
		// Ottieni l'email dell'utente autenticato
		String username = currentUser.getUsername(); 

		// Recupera l'utente dal database usando l'email
		Credenziali credenziali = credenzialiRepository.findByUsername(username);

		if (credenziali == null) {
			throw new RuntimeException("Errore: Utente non trovato!");
		}

		model.addAttribute("appunto", new Appunto());
		model.addAttribute("materie", materiaService.findAll());
		model.addAttribute("utente", credenziali.getUtente()); // Passa l'utente alla view

		return "formNewAppunto";
	}


	@PostMapping("/salvaAppunto")
	public String saveAppunto(@Valid @ModelAttribute("appunto") Appunto appunto, BindingResult bindingResult, 
			@AuthenticationPrincipal UserDetails currentUser, Model model) {

		
		if (bindingResult.hasErrors()) {
			model.addAttribute("messaggioErrore", "Errore nella validazione del form!");
			model.addAttribute("materie", materiaService.findAll());
			return "formNewAppunto";
		}

		// Recupera l'utente autenticato dal database usando l'email
		String username = currentUser.getUsername();
		Credenziali credenziali = credenzialiRepository.findByUsername(username);

		if (credenziali.getUtente() == null) {
			throw new RuntimeException("Errore: Utente non trovato!");
		}

		appunto.setUtente(credenziali.getUtente());

		// Verifica che la materia sia stata selezionata
		if (appunto.getMateria() == null || appunto.getMateria().getId() == null) {
			model.addAttribute("messaggioErrore", "Seleziona una materia!");
			model.addAttribute("materie", materiaService.findAll());
			return "formNewAppunto";
		}

		// Imposta la data di creazione se non presente
		if (appunto.getDataCreazione() == null) {
			appunto.setDataCreazione(LocalDate.now());
		}
	

		appuntoService.save(appunto);

		model.addAttribute("messaggio", "Appunto salvato con successo!");
		return "confermaSalvataggio";
	}


	@GetMapping("/admin/operazioniAppunti")
	public String operazioniAppunti() {
		return "/admin/operazioniAppunti";
	}

	@GetMapping("/admin/listaAppuntiAdmin")
	public String getAppunti(Model model) {
		model.addAttribute("appunti", this.appuntoService.findAll());
		return "admin/listaAppuntiAdmin";
	}

	@GetMapping("/admin/listaAppuntiRecensione")
	public String listaAppuntiRecensione(Model model) {
		model.addAttribute("appunti", this.appuntoService.findAll());
		return "admin/listaRecensioneAppunti";
	}

	@GetMapping("/listaAppunti")
	public String listaAppunti(Model model) {
		model.addAttribute("appunti", this.appuntoService.findAll());
		return "listaAppunti";
	}

	@GetMapping("/appunti/{id}")
	public String visualizzaAppunto(@PathVariable Long id, Model model) {
		Appunto appunto = appuntoService.findById(id);
		if (appunto == null) {
			model.addAttribute("messaggioErrore", "Appunto non trovato!");
			return "error";
		}
		
		List<Recensione> listaRecensioni = this.recensioneService.findByAppunto(appunto);
		
		model.addAttribute("appunto", appunto);
		model.addAttribute("recensioni", listaRecensioni);
		return "visualizzaAppunto";
	}

	@GetMapping("/admin/listaAppuntiScarsi")
	public String getAppuntiScarsi(Model model) {
		model.addAttribute("appunti", this.appuntoService.appuntiScarsi());
		return "admin/listaAppuntiScarsi";
	}

	@GetMapping("/listaAppuntiPropri")
	public String getAppuntiPropri(@AuthenticationPrincipal UserDetails currentUser, Model model) {
		String username = currentUser.getUsername(); 

		// Recupera l'utente dal database usando l'email
		Credenziali credenziali = credenzialiRepository.findByUsername(username);

		if (credenziali == null) {
			throw new RuntimeException("Errore: Utente non trovato!");
		}
		model.addAttribute("appunti", this.appuntoService.findByUtente(credenziali.getUtente()));
		return "listaAppuntiPropri";
	}

	@GetMapping("/admin/eliminaAppunto/{id}")
	public String eliminaAppuntoAdmin(@PathVariable("id") Long id, Model model) {
		try {
			appuntoService.deleteById(id);
			model.addAttribute("messaggio", "Appunto eliminato con successo!");
		} catch (Exception e) {
			model.addAttribute("messaggioErrore", "Errore durante l'eliminazione dell'appunto.");
		}
		return "/admin/confermaOperazioneAppunto";
	}
	
	@GetMapping("/eliminaAppunto/{id}")
	public String eliminaAppunto(@PathVariable("id") Long id, Model model) {
		try {
			appuntoService.deleteById(id);
			model.addAttribute("messaggio", "Appunto eliminato con successo!");
		} catch (Exception e) {
			model.addAttribute("messaggioErrore", "Errore durante l'eliminazione dell'appunto.");
		}
		return "confermaOperazioneAppunto";
	}
	
	
	@GetMapping("/formSearchAppunti")
	public String formSearchAppunti() {
	    return "formSearchAppunti";
	}
	
	
	@GetMapping("/searchAppunti")
	public String searchAppunti(Model model, @RequestParam String keyword) {
	    model.addAttribute("appunti", this.appuntoService.searchByKeyword(keyword));
	    return "foundAppunti";
	}

	@GetMapping("/appuntiMateria")
	public String appuntiMateria(Model model, @RequestParam(value = "nome", required = false) String nome) {
	    if (nome == null || nome.isBlank()) {
	        model.addAttribute("messaggioErrore", "Parametro 'nome' mancante!");
	        return "appuntiMateria";
	    }
	    
	    Materia materia = materiaService.findByNome(nome);
	    if (materia == null) {
	        model.addAttribute("messaggioErrore", "Materia non trovata!");
	        return "appuntiMateria";
	    }

	    List<Appunto> appunti = materia.getAppunti();
	    if (appunti == null) {
	        appunti = new ArrayList<>(); // Evita errori se la lista è null
	    }

	    model.addAttribute("appunti", appunti);
	    return "appuntiMateria";
	}

	 @GetMapping("modificaAppunto/{id}")
	    public String mostraFormModificaAppunto(@PathVariable("id") Long id, Model model,@AuthenticationPrincipal UserDetails userDetails) {
	        // Recupera la materia dal database tramite l'ID
	        Appunto appunto = appuntoService.findById(id);
	        Credenziali credenzili = this.credenzialiRepository.findByUsername(userDetails.getUsername());
	        
	        
	        if(!credenzili.getUtente().getId().equals(appunto.getUtente().getId())) {
	        	return "error";
	        }
	        
	        if (appunto == null) {
	            // Se la materia non esiste, reindirizza a una pagina di errore o alla lista delle materie
	            return "listaAppuntiPropri";
	        }
	        // Passa la materia al modello per popolare il form di modifica
	        model.addAttribute("appunto", appunto);
	        model.addAttribute("materie", this.materiaService.findAll());
	        return "formModificaAppunto"; // Nome del template HTML per il form di modifica
	    }
	    
	 @PostMapping("salvaModificaAppunto/{id}")
	 public String salvaModificaMateria(@PathVariable("id") Long id, @Valid @ModelAttribute("appunto") Appunto appunto, 
	         BindingResult bindingResult, Model model) {
		 
		 this.appuntoValidator.validate(appunto, bindingResult);
		 
	     // Verifica se ci sono errori di validazione
	     if (bindingResult.hasErrors()) {
	         List<String> errorMessages = bindingResult.getFieldErrors()
	             .stream()
	             .map(error -> error.getField() + ": " + error.getDefaultMessage())
	             .toList();
	         model.addAttribute("errorMessages", errorMessages); // Aggiungi messaggi di errore dettagliati
	         return "formModificaAppunto";
	     }

	     // Recupera l'appunto esistente dal database
	     Appunto appuntoEsistente = appuntoService.findById(id);
	     if (appuntoEsistente == null) {
	         model.addAttribute("messaggioErrore", "Appunto non trovato!");
	         return "listaAppuntiPropri";
	     }

	     // Aggiorna i campi dell'appunto esistente con i nuovi valori
	     appuntoEsistente.setNome(appunto.getNome());
	     appuntoEsistente.setContenuto(appunto.getContenuto());
	     appuntoEsistente.setDataCreazione(appunto.getDataCreazione());

	     // Salva l'appunto aggiornato
	     appuntoService.save(appuntoEsistente);

	     // Aggiunge un messaggio di conferma al modello
	     model.addAttribute("messaggio", "Appunto modificato con successo!");

	     // Reindirizza alla lista degli appunti
	     return "confermaOperazioneAppunto";
	 }
	
	
}
