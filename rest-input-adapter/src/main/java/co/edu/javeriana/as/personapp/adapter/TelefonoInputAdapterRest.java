package co.edu.javeriana.as.personapp.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PhoneInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PhoneUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mapper.TelefonoMapperRest;
import co.edu.javeriana.as.personapp.model.request.TelefonoRequest;
import co.edu.javeriana.as.personapp.model.response.TelefonoResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class TelefonoInputAdapterRest {

	@Autowired
	@Qualifier("phoneOutputAdapterMaria")
	private PhoneOutputPort phoneOutputPortMaria;

	@Autowired
	@Qualifier("phoneOutputAdapterMongo")
	private PhoneOutputPort phoneOutputPortMongo;

	@Autowired
	private TelefonoMapperRest telefonoMapperRest;

	PhoneInputPort phoneInputPort;

	private String setPhoneOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			phoneInputPort = new PhoneUseCase(phoneOutputPortMaria);
			return DatabaseOption.MARIA.toString();
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			phoneInputPort = new PhoneUseCase(phoneOutputPortMongo);
			return DatabaseOption.MONGO.toString();
		} else {
			throw new InvalidOptionException("Invalid database option: " + dbOption);
		}
	}

	public List<TelefonoResponse> historial(String database) {
		log.info("Into historial TelefonoEntity in Input Adapter");
		try {
			String db = setPhoneOutputPortInjection(database);
			return phoneInputPort.findAll().stream()
					.map(db.equalsIgnoreCase(DatabaseOption.MARIA.toString())
							? telefonoMapperRest::fromDomainToAdapterRestMaria
							: telefonoMapperRest::fromDomainToAdapterRestMongo)
					.collect(Collectors.toList());
		} catch (InvalidOptionException e) {
			log.warn(e.getMessage());
			return new ArrayList<>();
		}
	}

	public TelefonoResponse crearTelefono(TelefonoRequest request) {
		try {
			String db = setPhoneOutputPortInjection(request.getDatabase());
			Phone phone = phoneInputPort.create(telefonoMapperRest.fromAdapterToDomain(request));
			return db.equalsIgnoreCase(DatabaseOption.MARIA.toString())
					? telefonoMapperRest.fromDomainToAdapterRestMaria(phone)
					: telefonoMapperRest.fromDomainToAdapterRestMongo(phone);
		} catch (Exception e) {
			log.error("Error al crear teléfono: " + e.getMessage());
			return null;
		}
	}

	public TelefonoResponse editarTelefono(String numero, TelefonoRequest request) {
		try {
			String db = setPhoneOutputPortInjection(request.getDatabase());
			Phone phone = telefonoMapperRest.fromAdapterToDomain(request);
			phone.setNumber(numero);
			Phone edited = phoneInputPort.edit(numero, phone);
			return db.equalsIgnoreCase(DatabaseOption.MARIA.toString())
					? telefonoMapperRest.fromDomainToAdapterRestMaria(edited)
					: telefonoMapperRest.fromDomainToAdapterRestMongo(edited);
		} catch (Exception e) {
			log.error("Error al editar teléfono: " + e.getMessage());
			return null;
		}
	}

	public Boolean eliminarTelefono(String numero, String database) {
		try {
			setPhoneOutputPortInjection(database);
			return phoneInputPort.drop(numero);
		} catch (Exception e) {
			log.error("Error al eliminar teléfono: " + e.getMessage());
			return false;
		}
	}

	public TelefonoResponse buscarTelefonoPorNumero(String numero, String database) {
		try {
			String db = setPhoneOutputPortInjection(database);
			Phone phone = phoneInputPort.findOne(numero);
			return db.equalsIgnoreCase(DatabaseOption.MARIA.toString())
					? telefonoMapperRest.fromDomainToAdapterRestMaria(phone)
					: telefonoMapperRest.fromDomainToAdapterRestMongo(phone);
		} catch (Exception e) {
			log.error("Error al buscar teléfono: " + e.getMessage());
			return null;
		}
	}

	public Integer contarTelefonos(String database) {
		try {
			setPhoneOutputPortInjection(database);
			return phoneInputPort.count();
		} catch (Exception e) {
			log.error("Error al contar teléfonos: " + e.getMessage());
			return 0;
		}
	}
}

