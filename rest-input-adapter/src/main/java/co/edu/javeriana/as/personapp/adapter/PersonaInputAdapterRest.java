package co.edu.javeriana.as.personapp.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PersonUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.mapper.PersonaMapperRest;
import co.edu.javeriana.as.personapp.model.request.PersonaRequest;
import co.edu.javeriana.as.personapp.model.response.PersonaResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class PersonaInputAdapterRest {

	@Autowired
	@Qualifier("personOutputAdapterMaria")
	private PersonOutputPort personOutputPortMaria;

	@Autowired
	@Qualifier("personOutputAdapterMongo")
	private PersonOutputPort personOutputPortMongo;

	@Autowired
	private PersonaMapperRest personaMapperRest;

	PersonInputPort personInputPort;

	private String setPersonOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			personInputPort = new PersonUseCase(personOutputPortMaria);
			return DatabaseOption.MARIA.toString();
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			personInputPort = new PersonUseCase(personOutputPortMongo);
			return  DatabaseOption.MONGO.toString();
		} else {
			throw new InvalidOptionException("Invalid database option: " + dbOption);
		}
	}

	public List<PersonaResponse> historial(String database) {
		log.info("Into historial PersonaEntity in Input Adapter");
		try {
			if(setPersonOutputPortInjection(database).equalsIgnoreCase(DatabaseOption.MARIA.toString())){
				return personInputPort.findAll().stream().map(personaMapperRest::fromDomainToAdapterRestMaria)
						.collect(Collectors.toList());
			}else {
				return personInputPort.findAll().stream().map(personaMapperRest::fromDomainToAdapterRestMongo)
						.collect(Collectors.toList());
			}
			
		} catch (InvalidOptionException e) {
			log.warn(e.getMessage());
			return new ArrayList<PersonaResponse>();
		}
	}

	public PersonaResponse crearPersona(PersonaRequest request) {
		try {
			String db = setPersonOutputPortInjection(request.getDatabase());
			Person person = personInputPort.create(personaMapperRest.fromAdapterToDomain(request));
			return db.equalsIgnoreCase(DatabaseOption.MARIA.toString()) 
					? personaMapperRest.fromDomainToAdapterRestMaria(person)
					: personaMapperRest.fromDomainToAdapterRestMongo(person);
		} catch (InvalidOptionException e) {
			log.warn(e.getMessage());
			return null;
		} catch (Exception e) {
			log.error("Error al crear persona: " + e.getMessage());
			return null;
		}
	}

	public PersonaResponse editarPersona(Integer cc, PersonaRequest request) {
		try {
			String db = setPersonOutputPortInjection(request.getDatabase());
			Person person = personaMapperRest.fromAdapterToDomain(request);
			person.setIdentification(cc);
			Person edited = personInputPort.edit(cc, person);
			return db.equalsIgnoreCase(DatabaseOption.MARIA.toString())
					? personaMapperRest.fromDomainToAdapterRestMaria(edited)
					: personaMapperRest.fromDomainToAdapterRestMongo(edited);
		} catch (Exception e) {
			log.error("Error al editar persona: " + e.getMessage());
			return null;
		}
	}

	public Boolean eliminarPersona(Integer cc, String database) {
		try {
			setPersonOutputPortInjection(database);
			return personInputPort.drop(cc);
		} catch (Exception e) {
			log.error("Error al eliminar persona: " + e.getMessage());
			return false;
		}
	}

	public PersonaResponse buscarPersonaPorId(Integer cc, String database) {
		try {
			String db = setPersonOutputPortInjection(database);
			Person person = personInputPort.findOne(cc);
			return db.equalsIgnoreCase(DatabaseOption.MARIA.toString())
					? personaMapperRest.fromDomainToAdapterRestMaria(person)
					: personaMapperRest.fromDomainToAdapterRestMongo(person);
		} catch (Exception e) {
			log.error("Error al buscar persona: " + e.getMessage());
			return null;
		}
	}

	public Integer contarPersonas(String database) {
		try {
			setPersonOutputPortInjection(database);
			return personInputPort.count();
		} catch (Exception e) {
			log.error("Error al contar personas: " + e.getMessage());
			return 0;
		}
	}

}
