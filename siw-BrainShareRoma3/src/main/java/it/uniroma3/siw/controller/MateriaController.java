package it.uniroma3.siw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.controller.validator.MateriaValidator;
import it.uniroma3.siw.model.Materia;
import it.uniroma3.siw.repository.MateriaRepository;
import it.uniroma3.siw.service.MateriaService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class MateriaController {

	@Autowired
	protected MateriaRepository materiaRepository;

	@Autowired
	protected MateriaValidator materiaValidator;

	@Autowired(required = true)
	protected MateriaService materiaService;

	// Mostra il form per l'inserimento di una nuova materia
	@GetMapping(value = "/admin/formNewMateria")
	public String formNewMateria(Model model) {
		model.addAttribute("materia", new Materia()); // Passa un oggetto Materia vuoto al form
		return "admin/formNewMateria"; // Nome del template HTML (senza estensione .html)
	}

	// Gestisce il salvataggio della materia
	@PostMapping(value = "/admin/saveMateria")
	public String saveMateria(@Valid @ModelAttribute("materia") Materia materia, BindingResult bindingResult, Model model) {


		if (bindingResult.hasErrors()) {
			return "admin/formNewMateria";
		}

		// Salva la materia utilizzando il service
		materiaService.save(materia);

		// Aggiunge un messaggio di conferma al modello
		model.addAttribute("messaggio", "Materia salvata con successo!");

		return "admin/confermaMateria"; // Pagina di conferma
	}


	@GetMapping("/admin/listaMaterie")
	public String getMaterie(Model model) {
		model.addAttribute("materie", this.materiaService.findAll());
		return "admin/listaMaterie";
	}


	@GetMapping(value="/admin/operazioniMaterie")
	public String operazioniMaterie() {
		return "/admin/operazioniMaterie";
	}

	@GetMapping("/admin/eliminaMateria")
	public String mostraMateriePerEliminazione(Model model) {
		// Recupera tutte le materie dal database
		model.addAttribute("materie", materiaService.findAll());
		return "/admin/listaEliminaMateria";
	}

	@GetMapping("/admin/eliminaMateria/{id}")
	public String eliminaMateria(@PathVariable("id") Long id, Model model) {
		try {
			// Elimina la materia (e tutti gli appunti associati grazie a CascadeType.ALL)
			materiaService.deleteById(id);
			model.addAttribute("messaggio", "Materia eliminata con successo!");
		} catch (Exception e) {
			model.addAttribute("messaggioErrore", "Errore durante l'eliminazione della materia. Verifica che non sia utilizzata altrove.");
		}
		// Ricarica la lista aggiornata delle materie
		model.addAttribute("materie", materiaService.findAll());
		return "/admin/confermaOperazioneMateria";
	}


	@GetMapping("/admin/modificaMateria")
	public String mostraMateriePerModifica(Model model) {
		model.addAttribute("materie", materiaService.findAll());
		return "admin/listaModificaMateria";
	}


	@GetMapping("/admin/modificaMateria/{id}")
	public String mostraFormModificaMateria(@PathVariable("id") Long id, Model model) {
		// Recupera la materia dal database tramite l'ID
		Materia materia = materiaService.findById(id);
		if (materia == null) {
			// Se la materia non esiste, reindirizza a una pagina di errore o alla lista delle materie
			return "redirect:/admin/listaMaterie";
		}
		// Passa la materia al modello per popolare il form di modifica
		model.addAttribute("materia", materia);
		return "admin/formModificaMateria"; // Nome del template HTML per il form di modifica
	}

	@PostMapping("/admin/salvaModificaMateria/{id}")
	public String salvaModificaMateria(@PathVariable("id") Long id,
	                                   @Validated @ModelAttribute("materia") Materia materia,
	                                   BindingResult bindingResult,
	                                   Model model) {
	    // Recupera la materia esistente dal database
	    Materia materiaEsistente = materiaService.findById(id);
	    if (materiaEsistente == null) {
	        return "redirect:/admin/listaMaterie";
	    }


	    // Se ci sono errori, rimanda al form con i messaggi
	    if (bindingResult.hasErrors()) {
	        model.addAttribute("materia", materia); // Mantieni i valori inseriti
	        return "admin/formModificaMateria";
	    }

	    // Solo se la validazione è superata, aggiorna i dati
	    materiaEsistente.setNome(materia.getNome());
	    materiaEsistente.setTipo(materia.getTipo());

	    // Salva le modifiche nel database
	    materiaService.save(materiaEsistente);

	    // Aggiungi messaggio di conferma
	    model.addAttribute("messaggio", "Materia modificata con successo!");
	    return "admin/confermaOperazioneMateria";
	}



	@GetMapping("/formSearchMaterie")
	public String formSearchMaterie() {
		return "formSearchMateria";
	}

	@GetMapping("/foundMaterie")
	public String foundMaterie(@RequestParam("nome") String nome, Model model) {
		model.addAttribute("materie", materiaService.findByNome(nome));
		return "foundMaterie";
	}


	@GetMapping("/materia/{id}")
	public String visualizzaMateria(@RequestParam("nome") String nome, Model model) {
		Materia materia = materiaService.findByNome(nome);
		if (materia == null) {
			model.addAttribute("messaggioErrore", "Materia non trovata!");
			return "error";
		}
		model.addAttribute("materia", materia);
		model.addAttribute("appunti", materia.getAppunti()); // Mostra gli appunti della materia
		return "dettagliMateria";
	}

}