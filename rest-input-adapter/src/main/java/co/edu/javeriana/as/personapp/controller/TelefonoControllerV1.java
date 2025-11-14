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

import co.edu.javeriana.as.personapp.adapter.TelefonoInputAdapterRest;
import co.edu.javeriana.as.personapp.model.request.TelefonoRequest;
import co.edu.javeriana.as.personapp.model.response.TelefonoResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/telefono")
public class TelefonoControllerV1 {
	
	@Autowired
	private TelefonoInputAdapterRest telefonoInputAdapterRest;
	
	@ResponseBody
	@GetMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<TelefonoResponse>> obtenerTodosLosTelefonos(@PathVariable String database) {
		log.info("GET /api/v1/telefono/{} - Obtener todos los teléfonos", database);
		List<TelefonoResponse> telefonos = telefonoInputAdapterRest.historial(database.toUpperCase());
		return ResponseEntity.ok(telefonos);
	}
	
	@ResponseBody
	@GetMapping(path = "/{database}/{numero}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TelefonoResponse> obtenerTelefonoPorNumero(
			@PathVariable String database, 
			@PathVariable String numero) {
		log.info("GET /api/v1/telefono/{}/{} - Obtener teléfono por número", database, numero);
		TelefonoResponse telefono = telefonoInputAdapterRest.buscarTelefonoPorNumero(numero, database.toUpperCase());
		if (telefono != null) {
			return ResponseEntity.ok(telefono);
		}
		return ResponseEntity.notFound().build();
	}
	
	@ResponseBody
	@PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TelefonoResponse> crearTelefono(@RequestBody TelefonoRequest request) {
		log.info("POST /api/v1/telefono - Crear teléfono");
		TelefonoResponse telefono = telefonoInputAdapterRest.crearTelefono(request);
		if (telefono != null) {
			return ResponseEntity.status(HttpStatus.CREATED).body(telefono);
		}
		return ResponseEntity.badRequest().build();
	}
	
	@ResponseBody
	@PutMapping(path = "/{database}/{numero}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TelefonoResponse> actualizarTelefono(
			@PathVariable String database,
			@PathVariable String numero,
			@RequestBody TelefonoRequest request) {
		log.info("PUT /api/v1/telefono/{}/{} - Actualizar teléfono", database, numero);
		request.setDatabase(database.toUpperCase());
		TelefonoResponse telefono = telefonoInputAdapterRest.editarTelefono(numero, request);
		if (telefono != null) {
			return ResponseEntity.ok(telefono);
		}
		return ResponseEntity.notFound().build();
	}
	
	@ResponseBody
	@DeleteMapping(path = "/{database}/{numero}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> eliminarTelefono(
			@PathVariable String database,
			@PathVariable String numero) {
		log.info("DELETE /api/v1/telefono/{}/{} - Eliminar teléfono", database, numero);
		Boolean eliminado = telefonoInputAdapterRest.eliminarTelefono(numero, database.toUpperCase());
		if (eliminado) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}
	
	@ResponseBody
	@GetMapping(path = "/{database}/count", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Integer> contarTelefonos(@PathVariable String database) {
		log.info("GET /api/v1/telefono/{}/count - Contar teléfonos", database);
		Integer count = telefonoInputAdapterRest.contarTelefonos(database.toUpperCase());
		return ResponseEntity.ok(count);
	}
}

