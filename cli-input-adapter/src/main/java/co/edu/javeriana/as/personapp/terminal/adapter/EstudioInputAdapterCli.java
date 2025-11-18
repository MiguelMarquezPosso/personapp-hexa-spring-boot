package co.edu.javeriana.as.personapp.terminal.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.StudyInputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.StudyUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.terminal.mapper.EstudioMapperCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class EstudioInputAdapterCli {

  @Autowired
  @Qualifier("studyOutputAdapterMaria")
  private StudyOutputPort studyOutputPortMaria;

  @Autowired
  @Qualifier("studyOutputAdapterMongo")
  private StudyOutputPort studyOutputPortMongo;

  @Autowired
  private EstudioMapperCli estudioMapperCli;

  StudyInputPort studyInputPort;

  public void setStudyOutputPortInjection(String dbOption) throws InvalidOptionException {
    if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
      studyInputPort = new StudyUseCase(studyOutputPortMaria);
    } else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
      studyInputPort = new StudyUseCase(studyOutputPortMongo);
    } else {
      throw new InvalidOptionException("Invalid database option: " + dbOption);
    }
  }

  public void historial() {
    log.info("Into historial EstudioEntity in Input Adapter");
    studyInputPort.findAll().stream()
        .map(estudioMapperCli::fromDomainToAdapterCli)
        .forEach(System.out::println);
  }

  public void crear(Study study) {
    log.info("Into crear EstudioEntity in Input Adapter");
    Study created = studyInputPort.create(study);
    System.out.println("Estudio creado: " + estudioMapperCli.fromDomainToAdapterCli(created));
  }

  public void editar(Integer idProf, Integer ccPer, Study study) {
    log.info("Into editar EstudioEntity in Input Adapter");
    try {
      Study edited = studyInputPort.edit(idProf, ccPer, study);
      System.out.println("Estudio editado: " + estudioMapperCli.fromDomainToAdapterCli(edited));
    } catch (Exception e) {
      log.error("Error al editar estudio: " + e.getMessage());
      System.out.println("Error: " + e.getMessage());
    }
  }

  public void eliminar(Integer idProf, Integer ccPer) {
    log.info("Into eliminar EstudioEntity in Input Adapter");
    try {
      Boolean deleted = studyInputPort.drop(idProf, ccPer);
      if (deleted) {
        System.out.println("Estudio eliminado exitosamente");
      }
    } catch (Exception e) {
      log.error("Error al eliminar estudio: " + e.getMessage());
      System.out.println("Error: " + e.getMessage());
    }
  }

  public void buscarPorId(Integer idProf, Integer ccPer) {
    log.info("Into buscarPorId EstudioEntity in Input Adapter");
    try {
      Study study = studyInputPort.findOne(idProf, ccPer);
      System.out.println(estudioMapperCli.fromDomainToAdapterCli(study));
    } catch (Exception e) {
      log.error("Error al buscar estudio: " + e.getMessage());
      System.out.println("Error: " + e.getMessage());
    }
  }

  public void contar() {
    log.info("Into contar EstudioEntity in Input Adapter");
    Integer count = studyInputPort.count();
    System.out.println("Total de estudios: " + count);
  }
}
