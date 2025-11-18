package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.function.Supplier;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.terminal.adapter.TelefonoInputAdapterCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TelefonoMenu {

  private static final int OPCION_REGRESAR_MODULOS = 0;
  private static final int PERSISTENCIA_MARIADB = 1;
  private static final int PERSISTENCIA_MONGODB = 2;

  private static final int OPCION_REGRESAR_MOTOR_PERSISTENCIA = 0;
  private static final int OPCION_VER_TODO = 1;
  private static final int OPCION_CREAR = 2;
  private static final int OPCION_EDITAR = 3;
  private static final int OPCION_ELIMINAR = 4;
  private static final int OPCION_BUSCAR_POR_NUMERO = 5;
  private static final int OPCION_CONTAR = 6;

  public void iniciarMenu(TelefonoInputAdapterCli telefonoInputAdapterCli, Scanner keyboard) {
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
            telefonoInputAdapterCli.setPhoneOutputPortInjection("MARIA");
            menuOpciones(telefonoInputAdapterCli, keyboard);
            break;
          case PERSISTENCIA_MONGODB:
            telefonoInputAdapterCli.setPhoneOutputPortInjection("MONGO");
            menuOpciones(telefonoInputAdapterCli, keyboard);
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

  private void menuOpciones(TelefonoInputAdapterCli telefonoInputAdapterCli, Scanner keyboard) {
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
            telefonoInputAdapterCli.historial();
            break;
          case OPCION_CREAR:
            crearTelefono(telefonoInputAdapterCli, keyboard);
            break;
          case OPCION_EDITAR:
            editarTelefono(telefonoInputAdapterCli, keyboard);
            break;
          case OPCION_ELIMINAR:
            eliminarTelefono(telefonoInputAdapterCli, keyboard);
            break;
          case OPCION_BUSCAR_POR_NUMERO:
            buscarTelefonoPorNumero(telefonoInputAdapterCli, keyboard);
            break;
          case OPCION_CONTAR:
            telefonoInputAdapterCli.contar();
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

  private void crearTelefono(TelefonoInputAdapterCli adapter, Scanner keyboard) {
    Supplier<String> leerNumero = () -> {
      keyboard.nextLine();
      System.out.print("Ingrese el número de teléfono: ");
      return keyboard.nextLine();
    };
    Supplier<String> leerOperadora = () -> {
      System.out.print("Ingrese la operadora: ");
      return keyboard.nextLine();
    };
    Supplier<Person> leerDuenio = () -> {
      System.out.print("Ingrese el CC del dueño: ");
      Integer cc = keyboard.nextInt();
      Person owner = new Person();
      owner.setIdentification(cc);
      return owner;
    };

    String numero = leerNumero.get();
    String operadora = leerOperadora.get();
    Person duenio = leerDuenio.get();

    Phone phone = new Phone(numero, operadora, duenio);
    adapter.crear(phone);
  }

  private void editarTelefono(TelefonoInputAdapterCli adapter, Scanner keyboard) {
    keyboard.nextLine();
    System.out.print("Ingrese el número de teléfono a editar: ");
    String numero = keyboard.nextLine();
    System.out.print("Ingrese la nueva operadora: ");
    String operadora = keyboard.nextLine();
    System.out.print("Ingrese el CC del dueño: ");
    Integer cc = keyboard.nextInt();

    Person owner = new Person();
    owner.setIdentification(cc);
    Phone phone = new Phone(numero, operadora, owner);
    adapter.editar(numero, phone);
  }

  private void eliminarTelefono(TelefonoInputAdapterCli adapter, Scanner keyboard) {
    keyboard.nextLine();
    System.out.print("Ingrese el número de teléfono a eliminar: ");
    String numero = keyboard.nextLine();
    adapter.eliminar(numero);
  }

  private void buscarTelefonoPorNumero(TelefonoInputAdapterCli adapter, Scanner keyboard) {
    keyboard.nextLine();
    System.out.print("Ingrese el número de teléfono a buscar: ");
    String numero = keyboard.nextLine();
    adapter.buscarPorNumero(numero);
  }

  private void mostrarMenuOpciones() {
    System.out.println("----------------------");
    System.out.println(OPCION_VER_TODO + " para ver todos los teléfonos");
    System.out.println(OPCION_CREAR + " para crear un teléfono");
    System.out.println(OPCION_EDITAR + " para editar un teléfono");
    System.out.println(OPCION_ELIMINAR + " para eliminar un teléfono");
    System.out.println(OPCION_BUSCAR_POR_NUMERO + " para buscar un teléfono por número");
    System.out.println(OPCION_CONTAR + " para contar teléfonos");
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
