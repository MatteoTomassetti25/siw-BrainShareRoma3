package it.uniroma3.siw.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import it.uniroma3.siw.model.Appunto;
import it.uniroma3.siw.repository.AppuntoRepository;

@Component
public class AppuntoValidator implements Validator {

    @Autowired
    private AppuntoRepository appuntoRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return Appunto.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Appunto appunto = (Appunto) target;

        // Validazione del campo "nome"
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nome", "appunto.nome.required", "Il nome dell'appunto è obbligatorio.");
        if (appunto.getNome() != null && appunto.getNome().length() > 200) {
            errors.rejectValue("nome", "appunto.nome.length", "Il nome dell'appunto non può superare i 200 caratteri.");
        }
        if (appunto.getNome() != null && appuntoRepository.existsByNome(appunto.getNome())) {
            errors.rejectValue("nome", "appunto.nome.unique", "Esiste già un appunto con questo nome.");
        }

        // Validazione del campo "contenuto"
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "contenuto", "appunto.contenuto.required", "Il contenuto dell'appunto è obbligatorio.");
        if (appunto.getContenuto() != null && appunto.getContenuto().length() > 1000) { // Assumiamo un limite di 1000 caratteri
            errors.rejectValue("contenuto", "appunto.contenuto.length", "Il contenuto dell'appunto non può superare i 1000 caratteri.");
        }

        // Validazione del campo "materia"
        if (appunto.getMateria() == null) {
            errors.rejectValue("materia", "appunto.materia.required", "La materia è obbligatoria.");
        }
    }
}