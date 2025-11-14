package co.edu.javeriana.as.personapp.terminal.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.ProfessionUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.terminal.mapper.ProfesionMapperCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class ProfesionInputAdapterCli {

  @Autowired
  @Qualifier("professionOutputAdapterMaria")
  private ProfessionOutputPort professionOutputPortMaria;

  @Autowired
  @Qualifier("professionOutputAdapterMongo")
  private ProfessionOutputPort professionOutputPortMongo;

  @Autowired
  private ProfesionMapperCli profesionMapperCli;

  ProfessionInputPort professionInputPort;

  public void setProfessionOutputPortInjection(String dbOption) throws InvalidOptionException {
    if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
      professionInputPort = new ProfessionUseCase(professionOutputPortMaria);
    } else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
      professionInputPort = new ProfessionUseCase(professionOutputPortMongo);
    } else {
      throw new InvalidOptionException("Invalid database option: " + dbOption);
    }
  }

  public void historial() {
    log.info("Into historial ProfesionEntity in Input Adapter");
    professionInputPort.findAll().stream()
        .map(profesionMapperCli::fromDomainToAdapterCli)
        .forEach(System.out::println);
  }

  public void crear(Profession profession) {
    log.info("Into crear ProfesionEntity in Input Adapter");
    Profession created = professionInputPort.create(profession);
    System.out.println("Profesión creada: " + profesionMapperCli.fromDomainToAdapterCli(created));
  }

  public void editar(Integer id, Profession profession) {
    log.info("Into editar ProfesionEntity in Input Adapter");
    try {
      Profession edited = professionInputPort.edit(id, profession);
      System.out.println("Profesión editada: " + profesionMapperCli.fromDomainToAdapterCli(edited));
    } catch (Exception e) {
      log.error("Error al editar profesión: " + e.getMessage());
      System.out.println("Error: " + e.getMessage());
    }
  }

  public void eliminar(Integer id) {
    log.info("Into eliminar ProfesionEntity in Input Adapter");
    try {
      Boolean deleted = professionInputPort.drop(id);
      if (deleted) {
        System.out.println("Profesión eliminada exitosamente");
      }
    } catch (Exception e) {
      log.error("Error al eliminar profesión: " + e.getMessage());
      System.out.println("Error: " + e.getMessage());
    }
  }

  public void buscarPorId(Integer id) {
    log.info("Into buscarPorId ProfesionEntity in Input Adapter");
    try {
      Profession profession = professionInputPort.findOne(id);
      System.out.println(profesionMapperCli.fromDomainToAdapterCli(profession));
    } catch (Exception e) {
      log.error("Error al buscar profesión: " + e.getMessage());
      System.out.println("Error: " + e.getMessage());
    }
  }

  public void contar() {
    log.info("Into contar ProfesionEntity in Input Adapter");
    Integer count = professionInputPort.count();
    System.out.println("Total de profesiones: " + count);
  }
}
