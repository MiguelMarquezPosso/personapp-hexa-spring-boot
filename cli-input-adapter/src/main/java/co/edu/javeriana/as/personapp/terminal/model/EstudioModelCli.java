package co.edu.javeriana.as.personapp.terminal.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstudioModelCli {
  private Integer idProfesion;
  private Integer ccPersona;
  private LocalDate fecha;
  private String universidad;
}
