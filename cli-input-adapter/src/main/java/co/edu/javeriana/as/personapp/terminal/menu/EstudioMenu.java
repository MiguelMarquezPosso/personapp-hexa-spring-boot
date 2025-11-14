package co.edu.javeriana.as.personapp.terminal.menu;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.function.Supplier;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.terminal.adapter.EstudioInputAdapterCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EstudioMenu {

  private static final int OPCION_REGRESAR_MODULOS = 0;
  private static final int PERSISTENCIA_MARIADB = 1;
  private static final int PERSISTENCIA_MONGODB = 2;

  private static final int OPCION_REGRESAR_MOTOR_PERSISTENCIA = 0;
  private static final int OPCION_VER_TODO = 1;
  private static final int OPCION_CREAR = 2;
  private static final int OPCION_EDITAR = 3;
  private static final int OPCION_ELIMINAR = 4;
  private static final int OPCION_BUSCAR_POR_ID = 5;
  private static final int OPCION_CONTAR = 6;

  public void iniciarMenu(EstudioInputAdapterCli estudioInputAdapterCli, Scanner keyboard) {
    boolean isValid = false;
    do {
      try {
        mostrarMenuMotorPersistencia();
        int opcion = leerOpcion(keyboard);
        switch (opcion) {
          case OPCION_REGRESAR_MODULOS:
            isValid = true;
            break;
          case PERSISTENCIA_MARIADB:
            estudioInputAdapterCli.setStudyOutputPortInjection("MARIA");
            menuOpciones(estudioInputAdapterCli, keyboard);
            break;
          case PERSISTENCIA_MONGODB:
            estudioInputAdapterCli.setStudyOutputPortInjection("MONGO");
            menuOpciones(estudioInputAdapterCli, keyboard);
            break;
          default:
            log.warn("La opción elegida no es válida.");
        }
      } catch (InvalidOptionException e) {
        log.warn(e.getMessage());
      } catch (RuntimeException e) {
        if (e.getMessage() != null && e.getMessage().contains("Entrada no disponible")) {
          log.error("No hay entrada disponible. Regresando al menú principal.");
          isValid = true;
        } else {
          throw e;
        }
      }
    } while (!isValid);
  }

  private void menuOpciones(EstudioInputAdapterCli estudioInputAdapterCli, Scanner keyboard) {
    boolean isValid = false;
    do {
      try {
        mostrarMenuOpciones();
        int opcion = leerOpcion(keyboard);
        switch (opcion) {
          case OPCION_REGRESAR_MOTOR_PERSISTENCIA:
            isValid = true;
            break;
          case OPCION_VER_TODO:
            estudioInputAdapterCli.historial();
            break;
          case OPCION_CREAR:
            crearEstudio(estudioInputAdapterCli, keyboard);
            break;
          case OPCION_EDITAR:
            editarEstudio(estudioInputAdapterCli, keyboard);
            break;
          case OPCION_ELIMINAR:
            eliminarEstudio(estudioInputAdapterCli, keyboard);
            break;
          case OPCION_BUSCAR_POR_ID:
            buscarEstudioPorId(estudioInputAdapterCli, keyboard);
            break;
          case OPCION_CONTAR:
            estudioInputAdapterCli.contar();
            break;
          default:
            log.warn("La opción elegida no es válida.");
        }
      } catch (InputMismatchException e) {
        log.warn("Solo se permiten números.");
        keyboard.nextLine();
      } catch (RuntimeException e) {
        if (e.getMessage() != null && e.getMessage().contains("Entrada no disponible")) {
          log.error("No hay entrada disponible. Regresando al menú de persistencia.");
          isValid = true;
        } else {
          throw e;
        }
      }
    } while (!isValid);
  }

  private void crearEstudio(EstudioInputAdapterCli adapter, Scanner keyboard) {
    Supplier<Integer> leerIdProf = () -> {
      System.out.print("Ingrese el ID de la profesión: ");
      return keyboard.nextInt();
    };
    Supplier<Integer> leerCcPer = () -> {
      System.out.print("Ingrese el CC de la persona: ");
      return keyboard.nextInt();
    };
    Supplier<LocalDate> leerFecha = () -> {
      keyboard.nextLine();
      System.out.print("Ingrese la fecha de graduación (YYYY-MM-DD) o presione Enter para omitir: ");
      String fechaStr = keyboard.nextLine();
      return fechaStr.isEmpty() ? null : LocalDate.parse(fechaStr);
    };
    Supplier<String> leerUniversidad = () -> {
      System.out.print("Ingrese el nombre de la universidad (opcional): ");
      return keyboard.nextLine();
    };

    Integer idProf = leerIdProf.get();
    Integer ccPer = leerCcPer.get();
    LocalDate fecha = leerFecha.get();
    String universidad = leerUniversidad.get();

    Person person = new Person();
    person.setIdentification(ccPer);
    Profession profession = new Profession();
    profession.setIdentification(idProf);
    Study study = new Study(person, profession, fecha, universidad);
    adapter.crear(study);
  }

  private void editarEstudio(EstudioInputAdapterCli adapter, Scanner keyboard) {
    System.out.print("Ingrese el ID de la profesión: ");
    Integer idProf = keyboard.nextInt();
    System.out.print("Ingrese el CC de la persona: ");
    Integer ccPer = keyboard.nextInt();
    keyboard.nextLine();
    System.out.print("Ingrese la nueva fecha de graduación (YYYY-MM-DD) o presione Enter para omitir: ");
    String fechaStr = keyboard.nextLine();
    LocalDate fecha = fechaStr.isEmpty() ? null : LocalDate.parse(fechaStr);
    System.out.print("Ingrese el nuevo nombre de la universidad (opcional): ");
    String universidad = keyboard.nextLine();

    Person person = new Person();
    person.setIdentification(ccPer);
    Profession profession = new Profession();
    profession.setIdentification(idProf);
    Study study = new Study(person, profession, fecha, universidad);
    adapter.editar(idProf, ccPer, study);
  }

  private void eliminarEstudio(EstudioInputAdapterCli adapter, Scanner keyboard) {
    System.out.print("Ingrese el ID de la profesión: ");
    Integer idProf = keyboard.nextInt();
    System.out.print("Ingrese el CC de la persona: ");
    Integer ccPer = keyboard.nextInt();
    adapter.eliminar(idProf, ccPer);
  }

  private void buscarEstudioPorId(EstudioInputAdapterCli adapter, Scanner keyboard) {
    System.out.print("Ingrese el ID de la profesión: ");
    Integer idProf = keyboard.nextInt();
    System.out.print("Ingrese el CC de la persona: ");
    Integer ccPer = keyboard.nextInt();
    adapter.buscarPorId(idProf, ccPer);
  }

  private void mostrarMenuOpciones() {
    System.out.println("----------------------");
    System.out.println(OPCION_VER_TODO + " para ver todos los estudios");
    System.out.println(OPCION_CREAR + " para crear un estudio");
    System.out.println(OPCION_EDITAR + " para editar un estudio");
    System.out.println(OPCION_ELIMINAR + " para eliminar un estudio");
    System.out.println(OPCION_BUSCAR_POR_ID + " para buscar un estudio por ID");
    System.out.println(OPCION_CONTAR + " para contar estudios");
    System.out.println(OPCION_REGRESAR_MOTOR_PERSISTENCIA + " para regresar");
  }

  private void mostrarMenuMotorPersistencia() {
    System.out.println("----------------------");
    System.out.println(PERSISTENCIA_MARIADB + " para MariaDB");
    System.out.println(PERSISTENCIA_MONGODB + " para MongoDB");
    System.out.println(OPCION_REGRESAR_MODULOS + " para regresar");
  }

  private int leerOpcion(Scanner keyboard) {
    try {
      System.out.print("Ingrese una opción: ");
      return keyboard.nextInt();
    } catch (InputMismatchException e) {
      log.warn("Solo se permiten números.");
      keyboard.nextLine();
      return leerOpcion(keyboard);
    } catch (NoSuchElementException e) {
      log.error("No hay entrada disponible. Regresando al menú principal.");
      throw new RuntimeException("Entrada no disponible", e);
    }
  }
}
