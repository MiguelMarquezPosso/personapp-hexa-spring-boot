package co.edu.javeriana.as.personapp.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.model.request.PersonaRequest;
import co.edu.javeriana.as.personapp.model.response.PersonaResponse;

@Mapper
public class PersonaMapperRest {
	
	public PersonaResponse fromDomainToAdapterRestMaria(Person person) {
		return fromDomainToAdapterRest(person, "MariaDB");
	}
	public PersonaResponse fromDomainToAdapterRestMongo(Person person) {
		return fromDomainToAdapterRest(person, "MongoDB");
	}
	
	public PersonaResponse fromDomainToAdapterRest(Person person, String database) {
		return new PersonaResponse(
				person.getIdentification()+"", 
				person.getFirstName(), 
				person.getLastName(), 
				person.getAge()+"", 
				person.getGender().toString(), 
				database,
				"OK");
	}

	public Person fromAdapterToDomain(PersonaRequest request) {
		Person person = new Person();
		if (request.getDni() != null && !request.getDni().isEmpty()) {
			person.setIdentification(Integer.parseInt(request.getDni()));
		}
		person.setFirstName(request.getFirstName());
		person.setLastName(request.getLastName());
		if (request.getAge() != null && !request.getAge().isEmpty()) {
			person.setAge(Integer.parseInt(request.getAge()));
		}
		if (request.getSex() != null) {
			String sex = request.getSex().toUpperCase();
			person.setGender(sex.equals("M") || sex.equals("MALE") ? co.edu.javeriana.as.personapp.domain.Gender.MALE :
					sex.equals("F") || sex.equals("FEMALE") ? co.edu.javeriana.as.personapp.domain.Gender.FEMALE :
					co.edu.javeriana.as.personapp.domain.Gender.OTHER);
		}
		return person;
	}
		
}
