package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.function.Supplier;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.terminal.adapter.ProfesionInputAdapterCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProfesionMenu {

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

  public void iniciarMenu(ProfesionInputAdapterCli profesionInputAdapterCli, Scanner keyboard) {
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
            profesionInputAdapterCli.setProfessionOutputPortInjection("MARIA");
            menuOpciones(profesionInputAdapterCli, keyboard);
            break;
          case PERSISTENCIA_MONGODB:
            profesionInputAdapterCli.setProfessionOutputPortInjection("MONGO");
            menuOpciones(profesionInputAdapterCli, keyboard);
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

  private void menuOpciones(ProfesionInputAdapterCli profesionInputAdapterCli, Scanner keyboard) {
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
            profesionInputAdapterCli.historial();
            break;
          case OPCION_CREAR:
            crearProfesion(profesionInputAdapterCli, keyboard);
            break;
          case OPCION_EDITAR:
            editarProfesion(profesionInputAdapterCli, keyboard);
            break;
          case OPCION_ELIMINAR:
            eliminarProfesion(profesionInputAdapterCli, keyboard);
            break;
          case OPCION_BUSCAR_POR_ID:
            buscarProfesionPorId(profesionInputAdapterCli, keyboard);
            break;
          case OPCION_CONTAR:
            profesionInputAdapterCli.contar();
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

  private void crearProfesion(ProfesionInputAdapterCli adapter, Scanner keyboard) {
    Supplier<Integer> leerId = () -> {
      System.out.print("Ingrese el ID de la profesión: ");
      return keyboard.nextInt();
    };
    Supplier<String> leerNombre = () -> {
      keyboard.nextLine();
      System.out.print("Ingrese el nombre: ");
      return keyboard.nextLine();
    };
    Supplier<String> leerDescripcion = () -> {
      System.out.print("Ingrese la descripción (opcional): ");
      return keyboard.nextLine();
    };

    Integer id = leerId.get();
    String nombre = leerNombre.get();
    String descripcion = leerDescripcion.get();

    Profession profession = new Profession();
    profession.setIdentification(id);
    profession.setName(nombre);
    profession.setDescription(descripcion);
    adapter.crear(profession);
  }

  private void editarProfesion(ProfesionInputAdapterCli adapter, Scanner keyboard) {
    System.out.print("Ingrese el ID de la profesión a editar: ");
    Integer id = keyboard.nextInt();
    keyboard.nextLine();
    System.out.print("Ingrese el nuevo nombre: ");
    String nombre = keyboard.nextLine();
    System.out.print("Ingrese la nueva descripción (opcional): ");
    String descripcion = keyboard.nextLine();

    Profession profession = new Profession();
    profession.setIdentification(id);
    profession.setName(nombre);
    profession.setDescription(descripcion);
    adapter.editar(id, profession);
  }

  private void eliminarProfesion(ProfesionInputAdapterCli adapter, Scanner keyboard) {
    System.out.print("Ingrese el ID de la profesión a eliminar: ");
    Integer id = keyboard.nextInt();
    adapter.eliminar(id);
  }

  private void buscarProfesionPorId(ProfesionInputAdapterCli adapter, Scanner keyboard) {
    System.out.print("Ingrese el ID de la profesión a buscar: ");
    Integer id = keyboard.nextInt();
    adapter.buscarPorId(id);
  }

  private void mostrarMenuOpciones() {
    System.out.println("----------------------");
    System.out.println(OPCION_VER_TODO + " para ver todas las profesiones");
    System.out.println(OPCION_CREAR + " para crear una profesión");
    System.out.println(OPCION_EDITAR + " para editar una profesión");
    System.out.println(OPCION_ELIMINAR + " para eliminar una profesión");
    System.out.println(OPCION_BUSCAR_POR_ID + " para buscar una profesión por ID");
    System.out.println(OPCION_CONTAR + " para contar profesiones");
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
