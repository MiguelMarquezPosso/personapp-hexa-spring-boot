package co.edu.javeriana.as.personapp.terminal.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PhoneInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PhoneUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.terminal.mapper.TelefonoMapperCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class TelefonoInputAdapterCli {

  @Autowired
  @Qualifier("phoneOutputAdapterMaria")
  private PhoneOutputPort phoneOutputPortMaria;

  @Autowired
  @Qualifier("phoneOutputAdapterMongo")
  private PhoneOutputPort phoneOutputPortMongo;

  @Autowired
  private TelefonoMapperCli telefonoMapperCli;

  PhoneInputPort phoneInputPort;

  public void setPhoneOutputPortInjection(String dbOption) throws InvalidOptionException {
    if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
      phoneInputPort = new PhoneUseCase(phoneOutputPortMaria);
    } else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
      phoneInputPort = new PhoneUseCase(phoneOutputPortMongo);
    } else {
      throw new InvalidOptionException("Invalid database option: " + dbOption);
    }
  }

  public void historial() {
    log.info("Into historial TelefonoEntity in Input Adapter");
    phoneInputPort.findAll().stream()
        .map(telefonoMapperCli::fromDomainToAdapterCli)
        .forEach(System.out::println);
  }

  public void crear(Phone phone) {
    log.info("Into crear TelefonoEntity in Input Adapter");
    Phone created = phoneInputPort.create(phone);
    System.out.println("Teléfono creado: " + telefonoMapperCli.fromDomainToAdapterCli(created));
  }

  public void editar(String numero, Phone phone) {
    log.info("Into editar TelefonoEntity in Input Adapter");
    try {
      Phone edited = phoneInputPort.edit(numero, phone);
      System.out.println("Teléfono editado: " + telefonoMapperCli.fromDomainToAdapterCli(edited));
    } catch (Exception e) {
      log.error("Error al editar teléfono: " + e.getMessage());
      System.out.println("Error: " + e.getMessage());
    }
  }

  public void eliminar(String numero) {
    log.info("Into eliminar TelefonoEntity in Input Adapter");
    try {
      Boolean deleted = phoneInputPort.drop(numero);
      if (deleted) {
        System.out.println("Teléfono eliminado exitosamente");
      }
    } catch (Exception e) {
      log.error("Error al eliminar teléfono: " + e.getMessage());
      System.out.println("Error: " + e.getMessage());
    }
  }

  public void buscarPorNumero(String numero) {
    log.info("Into buscarPorNumero TelefonoEntity in Input Adapter");
    try {
      Phone phone = phoneInputPort.findOne(numero);
      System.out.println(telefonoMapperCli.fromDomainToAdapterCli(phone));
    } catch (Exception e) {
      log.error("Error al buscar teléfono: " + e.getMessage());
      System.out.println("Error: " + e.getMessage());
    }
  }

  public void contar() {
    log.info("Into contar TelefonoEntity in Input Adapter");
    Integer count = phoneInputPort.count();
    System.out.println("Total de teléfonos: " + count);
  }
}
