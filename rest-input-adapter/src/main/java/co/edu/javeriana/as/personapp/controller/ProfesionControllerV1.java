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

import co.edu.javeriana.as.personapp.adapter.ProfesionInputAdapterRest;
import co.edu.javeriana.as.personapp.model.request.ProfesionRequest;
import co.edu.javeriana.as.personapp.model.response.ProfesionResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/profesion")
public class ProfesionControllerV1 {
	
	@Autowired
	private ProfesionInputAdapterRest profesionInputAdapterRest;
	
	@ResponseBody
	@GetMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ProfesionResponse>> obtenerTodasLasProfesiones(@PathVariable String database) {
		log.info("GET /api/v1/profesion/{} - Obtener todas las profesiones", database);
		List<ProfesionResponse> profesiones = profesionInputAdapterRest.historial(database.toUpperCase());
		return ResponseEntity.ok(profesiones);
	}
	
	@ResponseBody
	@GetMapping(path = "/{database}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProfesionResponse> obtenerProfesionPorId(
			@PathVariable String database, 
			@PathVariable Integer id) {
		log.info("GET /api/v1/profesion/{}/{} - Obtener profesión por ID", database, id);
		ProfesionResponse profesion = profesionInputAdapterRest.buscarProfesionPorId(id, database.toUpperCase());
		if (profesion != null) {
			return ResponseEntity.ok(profesion);
		}
		return ResponseEntity.notFound().build();
	}
	
	@ResponseBody
	@PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProfesionResponse> crearProfesion(@RequestBody ProfesionRequest request) {
		log.info("POST /api/v1/profesion - Crear profesión");
		ProfesionResponse profesion = profesionInputAdapterRest.crearProfesion(request);
		if (profesion != null) {
			return ResponseEntity.status(HttpStatus.CREATED).body(profesion);
		}
		return ResponseEntity.badRequest().build();
	}
	
	@ResponseBody
	@PutMapping(path = "/{database}/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProfesionResponse> actualizarProfesion(
			@PathVariable String database,
			@PathVariable Integer id,
			@RequestBody ProfesionRequest request) {
		log.info("PUT /api/v1/profesion/{}/{} - Actualizar profesión", database, id);
		request.setDatabase(database.toUpperCase());
		ProfesionResponse profesion = profesionInputAdapterRest.editarProfesion(id, request);
		if (profesion != null) {
			return ResponseEntity.ok(profesion);
		}
		return ResponseEntity.notFound().build();
	}
	
	@ResponseBody
	@DeleteMapping(path = "/{database}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> eliminarProfesion(
			@PathVariable String database,
			@PathVariable Integer id) {
		log.info("DELETE /api/v1/profesion/{}/{} - Eliminar profesión", database, id);
		Boolean eliminado = profesionInputAdapterRest.eliminarProfesion(id, database.toUpperCase());
		if (eliminado) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}
	
	@ResponseBody
	@GetMapping(path = "/{database}/count", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Integer> contarProfesiones(@PathVariable String database) {
		log.info("GET /api/v1/profesion/{}/count - Contar profesiones", database);
		Integer count = profesionInputAdapterRest.contarProfesiones(database.toUpperCase());
		return ResponseEntity.ok(count);
	}
}

