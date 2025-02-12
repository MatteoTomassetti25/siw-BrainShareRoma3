package it.uniroma3.siw.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Materia;
import it.uniroma3.siw.repository.MateriaRepository;

@Component
public class MateriaValidator implements Validator {

    @Autowired
    protected MateriaRepository materiaRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return Materia.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Materia materia = (Materia) target;

        // Validazione del campo "nome"
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nome", "materia.nome.required", "Il nome della materia è obbligatorio.");

        if (materia.getNome() != null && materia.getNome().length() > 20) {
            errors.rejectValue("nome", "materia.nome.length", "Il nome della materia non può superare i 20 caratteri.");
        }

        // Verifica che il nome della materia sia univoco
        if (materia.getNome() != null && materiaRepository.findByNome(materia.getNome()) != null) {
            errors.rejectValue("nome", "materia.nome.unique", "Esiste già una materia con questo nome.");
        }

        // Validazione del campo "tipo"
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "tipo", "materia.tipo.required", "Il tipo della materia è obbligatorio.");

        if (materia.getTipo() != null && materia.getTipo().length() > 50) {
            errors.rejectValue("tipo", "materia.tipo.length", "Il tipo della materia non può superare i 50 caratteri.");
        }
    }
}