package co.edu.javeriana.as.personapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import co.edu.javeriana.as.personapp.adapter.EstudioInputAdapterRest;
import co.edu.javeriana.as.personapp.model.request.EstudioRequest;
import co.edu.javeriana.as.personapp.model.response.EstudioResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/estudio")
public class EstudioControllerV1 {
	
	@Autowired
	private EstudioInputAdapterRest estudioInputAdapterRest;
	
	@ResponseBody
	@GetMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<EstudioResponse>> obtenerTodosLosEstudios(@PathVariable String database) {
		log.info("GET /api/v1/estudio/{} - Obtener todos los estudios", database);
		List<EstudioResponse> estudios = estudioInputAdapterRest.historial(database.toUpperCase());
		return ResponseEntity.ok(estudios);
	}
	
	@ResponseBody
	@GetMapping(path = "/{database}/{idProf}/{ccPer}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<EstudioResponse> obtenerEstudioPorId(
			@PathVariable String database, 
			@PathVariable Integer idProf,
			@PathVariable Integer ccPer) {
		log.info("GET /api/v1/estudio/{}/{}/{} - Obtener estudio por ID", database, idProf, ccPer);
		EstudioResponse estudio = estudioInputAdapterRest.buscarEstudioPorId(idProf, ccPer, database.toUpperCase());
		if (estudio != null) {
			return ResponseEntity.ok(estudio);
		}
		return ResponseEntity.notFound().build();
	}
	
	@ResponseBody
	@PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<EstudioResponse> crearEstudio(@RequestBody EstudioRequest request) {
		log.info("POST /api/v1/estudio - Crear estudio");
		EstudioResponse estudio = estudioInputAdapterRest.crearEstudio(request);
		if (estudio != null) {
			return ResponseEntity.status(HttpStatus.CREATED).body(estudio);
		}
		return ResponseEntity.badRequest().build();
	}
	
	@ResponseBody
	@PutMapping(path = "/{database}/{idProf}/{ccPer}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<EstudioResponse> actualizarEstudio(
			@PathVariable String database,
			@PathVariable Integer idProf,
			@PathVariable Integer ccPer,
			@RequestBody EstudioRequest request) {
		log.info("PUT /api/v1/estudio/{}/{}/{} - Actualizar estudio", database, idProf, ccPer);
		request.setDatabase(database.toUpperCase());
		EstudioResponse estudio = estudioInputAdapterRest.editarEstudio(idProf, ccPer, request);
		if (estudio != null) {
			return ResponseEntity.ok(estudio);
		}
		return ResponseEntity.notFound().build();
	}
	
	@ResponseBody
	@DeleteMapping(path = "/{database}/{idProf}/{ccPer}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> eliminarEstudio(
			@PathVariable String database,
			@PathVariable Integer idProf,
			@PathVariable Integer ccPer) {
		log.info("DELETE /api/v1/estudio/{}/{}/{} - Eliminar estudio", database, idProf, ccPer);
		Boolean eliminado = estudioInputAdapterRest.eliminarEstudio(idProf, ccPer, database.toUpperCase());
		if (eliminado) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}
	
	@ResponseBody
	@GetMapping(path = "/{database}/count", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Integer> contarEstudios(@PathVariable String database) {
		log.info("GET /api/v1/estudio/{}/count - Contar estudios", database);
		Integer count = estudioInputAdapterRest.contarEstudios(database.toUpperCase());
		return ResponseEntity.ok(count);
	}
}

