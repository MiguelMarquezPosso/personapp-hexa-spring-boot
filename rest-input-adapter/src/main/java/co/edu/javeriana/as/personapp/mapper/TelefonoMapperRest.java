package co.edu.javeriana.as.personapp.mapper;

import java.util.function.Function;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.model.request.TelefonoRequest;
import co.edu.javeriana.as.personapp.model.response.TelefonoResponse;

@Mapper
public class TelefonoMapperRest {
	
	private static final Function<Phone, String> DATABASE_MARIA = p -> "MariaDB";
	private static final Function<Phone, String> DATABASE_MONGO = p -> "MongoDB";
	
	public TelefonoResponse fromDomainToAdapterRestMaria(Phone phone) {
		return fromDomainToAdapterRest(phone, DATABASE_MARIA.apply(phone));
	}
	
	public TelefonoResponse fromDomainToAdapterRestMongo(Phone phone) {
		return fromDomainToAdapterRest(phone, DATABASE_MONGO.apply(phone));
	}
	
	public TelefonoResponse fromDomainToAdapterRest(Phone phone, String database) {
		return new TelefonoResponse(
				phone.getNumber() != null ? phone.getNumber() : "",
				phone.getCompany() != null ? phone.getCompany() : "",
				phone.getOwner() != null && phone.getOwner().getIdentification() != null 
					? phone.getOwner().getIdentification().toString() : "",
				database,
				"OK");
	}

	public Phone fromAdapterToDomain(TelefonoRequest request) {
		Phone phone = new Phone();
		phone.setNumber(request.getNumero());
		phone.setCompany(request.getOperadora());
		if (request.getDuenioCc() != null && !request.getDuenioCc().isEmpty()) {
			Person owner = new Person();
			owner.setIdentification(Integer.parseInt(request.getDuenioCc()));
			phone.setOwner(owner);
		}
		return phone;
	}
}

