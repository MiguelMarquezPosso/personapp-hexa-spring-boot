package co.edu.javeriana.as.personapp.terminal.adapter;

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
import co.edu.javeriana.as.personapp.terminal.mapper.PersonaMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.PersonaModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class PersonaInputAdapterCli {

	@Autowired
	@Qualifier("personOutputAdapterMaria")
	private PersonOutputPort personOutputPortMaria;

	@Autowired
	@Qualifier("personOutputAdapterMongo")
	private PersonOutputPort personOutputPortMongo;

	@Autowired
	private PersonaMapperCli personaMapperCli;

	PersonInputPort personInputPort;

	public void setPersonOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			personInputPort = new PersonUseCase(personOutputPortMaria);
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			personInputPort = new PersonUseCase(personOutputPortMongo);
		} else {
			throw new InvalidOptionException("Invalid database option: " + dbOption);
		}
	}

	public void historial1() {
		log.info("Into historial PersonaEntity in Input Adapter");
		List<PersonaModelCli> persona = personInputPort.findAll().stream().map(personaMapperCli::fromDomainToAdapterCli)
				.collect(Collectors.toList());
		persona.forEach(p -> System.out.println(p.toString()));
	}

	public void historial() {
		log.info("Into historial PersonaEntity in Input Adapter");
		personInputPort.findAll().stream()
				.map(personaMapperCli::fromDomainToAdapterCli)
				.forEach(System.out::println);
	}

	public void crear(Person person) {
		log.info("Into crear PersonaEntity in Input Adapter");
		Person created = personInputPort.create(person);
		System.out.println("Persona creada: " + personaMapperCli.fromDomainToAdapterCli(created));
	}

	public void editar(Integer cc, Person person) {
		log.info("Into editar PersonaEntity in Input Adapter");
		try {
			Person edited = personInputPort.edit(cc, person);
			System.out.println("Persona editada: " + personaMapperCli.fromDomainToAdapterCli(edited));
		} catch (Exception e) {
			log.error("Error al editar persona: " + e.getMessage());
			System.out.println("Error: " + e.getMessage());
		}
	}

	public void eliminar(Integer cc) {
		log.info("Into eliminar PersonaEntity in Input Adapter");
		try {
			Boolean deleted = personInputPort.drop(cc);
			if (deleted) {
				System.out.println("Persona eliminada exitosamente");
			}
		} catch (Exception e) {
			log.error("Error al eliminar persona: " + e.getMessage());
			System.out.println("Error: " + e.getMessage());
		}
	}

	public void buscarPorId(Integer cc) {
		log.info("Into buscarPorId PersonaEntity in Input Adapter");
		try {
			Person person = personInputPort.findOne(cc);
			System.out.println(personaMapperCli.fromDomainToAdapterCli(person));
		} catch (Exception e) {
			log.error("Error al buscar persona: " + e.getMessage());
			System.out.println("Error: " + e.getMessage());
		}
	}

	public void contar() {
		log.info("Into contar PersonaEntity in Input Adapter");
		Integer count = personInputPort.count();
		System.out.println("Total de personas: " + count);
	}

}
