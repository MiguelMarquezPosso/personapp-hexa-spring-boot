package co.edu.javeriana.as.personapp.mapper;

import java.util.function.Function;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.model.request.ProfesionRequest;
import co.edu.javeriana.as.personapp.model.response.ProfesionResponse;

@Mapper
public class ProfesionMapperRest {
	
	private static final Function<Profession, String> DATABASE_MARIA = p -> "MariaDB";
	private static final Function<Profession, String> DATABASE_MONGO = p -> "MongoDB";
	
	public ProfesionResponse fromDomainToAdapterRestMaria(Profession profession) {
		return fromDomainToAdapterRest(profession, DATABASE_MARIA.apply(profession));
	}
	
	public ProfesionResponse fromDomainToAdapterRestMongo(Profession profession) {
		return fromDomainToAdapterRest(profession, DATABASE_MONGO.apply(profession));
	}
	
	public ProfesionResponse fromDomainToAdapterRest(Profession profession, String database) {
		return new ProfesionResponse(
				profession.getIdentification() != null ? profession.getIdentification().toString() : "",
				profession.getName() != null ? profession.getName() : "",
				profession.getDescription() != null ? profession.getDescription() : "",
				database,
				"OK");
	}

	public Profession fromAdapterToDomain(ProfesionRequest request) {
		Profession profession = new Profession();
		if (request.getId() != null && !request.getId().isEmpty()) {
			profession.setIdentification(Integer.parseInt(request.getId()));
		}
		profession.setName(request.getNombre());
		profession.setDescription(request.getDescripcion());
		return profession;
	}
}

