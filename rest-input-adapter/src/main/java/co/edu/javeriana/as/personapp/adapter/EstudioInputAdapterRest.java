package co.edu.javeriana.as.personapp.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.StudyInputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.StudyUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mapper.EstudioMapperRest;
import co.edu.javeriana.as.personapp.model.request.EstudioRequest;
import co.edu.javeriana.as.personapp.model.response.EstudioResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class EstudioInputAdapterRest {

  @Autowired
  @Qualifier("studyOutputAdapterMaria")
  private StudyOutputPort studyOutputPortMaria;

  @Autowired
  @Qualifier("studyOutputAdapterMongo")
  private StudyOutputPort studyOutputPortMongo;

  @Autowired
  private EstudioMapperRest estudioMapperRest;

  StudyInputPort studyInputPort;

  private String setStudyOutputPortInjection(String dbOption) throws InvalidOptionException {
    if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
      studyInputPort = new StudyUseCase(studyOutputPortMaria);
      return DatabaseOption.MARIA.toString();
    } else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
      studyInputPort = new StudyUseCase(studyOutputPortMongo);
      return DatabaseOption.MONGO.toString();
    } else {
      throw new InvalidOptionException("Invalid database option: " + dbOption);
    }
  }

  public List<EstudioResponse> historial(String database) {
    log.info("Into historial EstudioEntity in Input Adapter");
    try {
      String db = setStudyOutputPortInjection(database);
      return studyInputPort.findAll().stream()
          .map(db.equalsIgnoreCase(DatabaseOption.MARIA.toString())
              ? estudioMapperRest::fromDomainToAdapterRestMaria
              : estudioMapperRest::fromDomainToAdapterRestMongo)
          .collect(Collectors.toList());
    } catch (InvalidOptionException e) {
      log.warn(e.getMessage());
      return new ArrayList<>();
    }
  }

  public EstudioResponse crearEstudio(EstudioRequest request) {
    try {
      String db = setStudyOutputPortInjection(request.getDatabase());
      Study study = studyInputPort.create(estudioMapperRest.fromAdapterToDomain(request));
      return db.equalsIgnoreCase(DatabaseOption.MARIA.toString())
          ? estudioMapperRest.fromDomainToAdapterRestMaria(study)
          : estudioMapperRest.fromDomainToAdapterRestMongo(study);
    } catch (Exception e) {
      log.error("Error al crear estudio: " + e.getMessage());
      return null;
    }
  }

  public EstudioResponse editarEstudio(Integer idProf, Integer ccPer, EstudioRequest request) {
    try {
      String db = setStudyOutputPortInjection(request.getDatabase());
      Study study = estudioMapperRest.fromAdapterToDomain(request);
      study.getPerson().setIdentification(ccPer);
      study.getProfession().setIdentification(idProf);
      Study edited = studyInputPort.edit(idProf, ccPer, study);
      return db.equalsIgnoreCase(DatabaseOption.MARIA.toString())
          ? estudioMapperRest.fromDomainToAdapterRestMaria(edited)
          : estudioMapperRest.fromDomainToAdapterRestMongo(edited);
    } catch (Exception e) {
      log.error("Error al editar estudio: " + e.getMessage());
      return null;
    }
  }

  public Boolean eliminarEstudio(Integer idProf, Integer ccPer, String database) {
    try {
      setStudyOutputPortInjection(database);
      return studyInputPort.drop(idProf, ccPer);
    } catch (Exception e) {
      log.error("Error al eliminar estudio: " + e.getMessage());
      return false;
    }
  }

  public EstudioResponse buscarEstudioPorId(Integer idProf, Integer ccPer, String database) {
    try {
      String db = setStudyOutputPortInjection(database);
      Study study = studyInputPort.findOne(idProf, ccPer);
      return db.equalsIgnoreCase(DatabaseOption.MARIA.toString())
          ? estudioMapperRest.fromDomainToAdapterRestMaria(study)
          : estudioMapperRest.fromDomainToAdapterRestMongo(study);
    } catch (Exception e) {
      log.error("Error al buscar estudio: " + e.getMessage());
      return null;
    }
  }

  public Integer contarEstudios(String database) {
    try {
      setStudyOutputPortInjection(database);
      return studyInputPort.count();
    } catch (Exception e) {
      log.error("Error al contar estudios: " + e.getMessage());
      return 0;
    }
  }
}
