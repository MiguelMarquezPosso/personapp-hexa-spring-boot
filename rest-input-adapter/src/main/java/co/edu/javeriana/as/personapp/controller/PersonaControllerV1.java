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

import co.edu.javeriana.as.personapp.adapter.PersonaInputAdapterRest;
import co.edu.javeriana.as.personapp.model.request.PersonaRequest;
import co.edu.javeriana.as.personapp.model.response.PersonaResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/persona")
public class PersonaControllerV1 {
	
	@Autowired
	private PersonaInputAdapterRest personaInputAdapterRest;
	
	@ResponseBody
	@GetMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<PersonaResponse>> obtenerTodasLasPersonas(@PathVariable String database) {
		log.info("GET /api/v1/persona/{} - Obtener todas las personas", database);
		List<PersonaResponse> personas = personaInputAdapterRest.historial(database.toUpperCase());
		return ResponseEntity.ok(personas);
	}
	
	@ResponseBody
	@GetMapping(path = "/{database}/{cc}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PersonaResponse> obtenerPersonaPorId(
			@PathVariable String database, 
			@PathVariable Integer cc) {
		log.info("GET /api/v1/persona/{}/{} - Obtener persona por ID", database, cc);
		PersonaResponse persona = personaInputAdapterRest.buscarPersonaPorId(cc, database.toUpperCase());
		if (persona != null) {
			return ResponseEntity.ok(persona);
		}
		return ResponseEntity.notFound().build();
	}
	
	@ResponseBody
	@PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PersonaResponse> crearPersona(@RequestBody PersonaRequest request) {
		log.info("POST /api/v1/persona - Crear persona");
		PersonaResponse persona = personaInputAdapterRest.crearPersona(request);
		if (persona != null) {
			return ResponseEntity.status(HttpStatus.CREATED).body(persona);
		}
		return ResponseEntity.badRequest().build();
	}
	
	@ResponseBody
	@PutMapping(path = "/{database}/{cc}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PersonaResponse> actualizarPersona(
			@PathVariable String database,
			@PathVariable Integer cc,
			@RequestBody PersonaRequest request) {
		log.info("PUT /api/v1/persona/{}/{} - Actualizar persona", database, cc);
		request.setDatabase(database.toUpperCase());
		PersonaResponse persona = personaInputAdapterRest.editarPersona(cc, request);
		if (persona != null) {
			return ResponseEntity.ok(persona);
		}
		return ResponseEntity.notFound().build();
	}
	
	@ResponseBody
	@DeleteMapping(path = "/{database}/{cc}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> eliminarPersona(
			@PathVariable String database,
			@PathVariable Integer cc) {
		log.info("DELETE /api/v1/persona/{}/{} - Eliminar persona", database, cc);
		Boolean eliminado = personaInputAdapterRest.eliminarPersona(cc, database.toUpperCase());
		if (eliminado) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}
	
	@ResponseBody
	@GetMapping(path = "/{database}/count", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Integer> contarPersonas(@PathVariable String database) {
		log.info("GET /api/v1/persona/{}/count - Contar personas", database);
		Integer count = personaInputAdapterRest.contarPersonas(database.toUpperCase());
		return ResponseEntity.ok(count);
	}
}
