package co.edu.javeriana.as.personapp.model.response;

import co.edu.javeriana.as.personapp.model.request.EstudioRequest;

public class EstudioResponse extends EstudioRequest {
	
	private String status;
	
	public EstudioResponse(String idProfesion, String ccPersona, String fecha, String universidad, String database, String status) {
		super(idProfesion, ccPersona, fecha, universidad, database);
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}

