package co.edu.javeriana.as.personapp.mapper;

import java.time.LocalDate;
import java.util.function.Function;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.model.request.EstudioRequest;
import co.edu.javeriana.as.personapp.model.response.EstudioResponse;

@Mapper
public class EstudioMapperRest {
	
	private static final Function<Study, String> DATABASE_MARIA = s -> "MariaDB";
	private static final Function<Study, String> DATABASE_MONGO = s -> "MongoDB";
	
	public EstudioResponse fromDomainToAdapterRestMaria(Study study) {
		return fromDomainToAdapterRest(study, DATABASE_MARIA.apply(study));
	}
	
	public EstudioResponse fromDomainToAdapterRestMongo(Study study) {
		return fromDomainToAdapterRest(study, DATABASE_MONGO.apply(study));
	}
	
	public EstudioResponse fromDomainToAdapterRest(Study study, String database) {
		return new EstudioResponse(
				study.getProfession() != null && study.getProfession().getIdentification() != null
					? study.getProfession().getIdentification().toString() : "",
				study.getPerson() != null && study.getPerson().getIdentification() != null
					? study.getPerson().getIdentification().toString() : "",
				study.getGraduationDate() != null ? study.getGraduationDate().toString() : "",
				study.getUniversityName() != null ? study.getUniversityName() : "",
				database,
				"OK");
	}

	public Study fromAdapterToDomain(EstudioRequest request) {
		Study study = new Study();
		
		if (request.getCcPersona() != null && !request.getCcPersona().isEmpty()) {
			Person person = new Person();
			person.setIdentification(Integer.parseInt(request.getCcPersona()));
			study.setPerson(person);
		}
		
		if (request.getIdProfesion() != null && !request.getIdProfesion().isEmpty()) {
			Profession profession = new Profession();
			profession.setIdentification(Integer.parseInt(request.getIdProfesion()));
			study.setProfession(profession);
		}
		
		if (request.getFecha() != null && !request.getFecha().isEmpty()) {
			study.setGraduationDate(LocalDate.parse(request.getFecha()));
		}
		
		study.setUniversityName(request.getUniversidad());
		return study;
	}
}

