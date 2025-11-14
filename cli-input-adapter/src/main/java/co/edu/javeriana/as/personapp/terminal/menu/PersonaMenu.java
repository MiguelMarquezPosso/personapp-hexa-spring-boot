package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.function.Supplier;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.terminal.adapter.PersonaInputAdapterCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PersonaMenu {

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

	public void iniciarMenu(PersonaInputAdapterCli personaInputAdapterCli, Scanner keyboard) {
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
						personaInputAdapterCli.setPersonOutputPortInjection("MARIA");
						menuOpciones(personaInputAdapterCli, keyboard);
						break;
					case PERSISTENCIA_MONGODB:
						personaInputAdapterCli.setPersonOutputPortInjection("MONGO");
						menuOpciones(personaInputAdapterCli, keyboard);
						break;
					default:
						log.warn("La opción elegida no es válida.");
				}
			} catch (InvalidOptionException e) {
				log.warn(e.getMessage());
			} catch (RuntimeException e) {
				if (e.getMessage() != null && e.getMessage().contains("Entrada no disponible")) {
					log.error("No hay entrada disponible. Regresando al menú principal.");
					isValid = true; // Salir del menú
				} else {
					throw e; // Re-lanzar otras RuntimeException
				}
			}
		} while (!isValid);
	}

	private void menuOpciones(PersonaInputAdapterCli personaInputAdapterCli, Scanner keyboard) {
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
						personaInputAdapterCli.historial();
						break;
					case OPCION_CREAR:
						crearPersona(personaInputAdapterCli, keyboard);
						break;
					case OPCION_EDITAR:
						editarPersona(personaInputAdapterCli, keyboard);
						break;
					case OPCION_ELIMINAR:
						eliminarPersona(personaInputAdapterCli, keyboard);
						break;
					case OPCION_BUSCAR_POR_ID:
						buscarPersonaPorId(personaInputAdapterCli, keyboard);
						break;
					case OPCION_CONTAR:
						personaInputAdapterCli.contar();
						break;
					default:
						log.warn("La opción elegida no es válida.");
				}
			} catch (InputMismatchException e) {
				log.warn("Solo se permiten números.");
			} catch (RuntimeException e) {
				if (e.getMessage() != null && e.getMessage().contains("Entrada no disponible")) {
					log.error("No hay entrada disponible. Regresando al menú de persistencia.");
					isValid = true; // Salir del menú
				} else {
					throw e; // Re-lanzar otras RuntimeException
				}
			}
		} while (!isValid);
	}

	private void crearPersona(PersonaInputAdapterCli adapter, Scanner keyboard) {
		Supplier<Integer> leerCc = () -> {
			System.out.print("Ingrese el CC de la persona: ");
			return keyboard.nextInt();
		};
		Supplier<String> leerNombre = () -> {
			keyboard.nextLine();
			System.out.print("Ingrese el nombre: ");
			return keyboard.nextLine();
		};
		Supplier<String> leerApellido = () -> {
			System.out.print("Ingrese el apellido: ");
			return keyboard.nextLine();
		};
		Supplier<Gender> leerGenero = () -> {
			System.out.print("Ingrese el género (M/F/O): ");
			String gen = keyboard.nextLine().toUpperCase();
			return gen.equals("M") ? Gender.MALE
					: gen.equals("F") ? Gender.FEMALE
							: Gender.OTHER;
		};
		Supplier<Integer> leerEdad = () -> {
			System.out.print("Ingrese la edad (opcional, presione Enter para omitir): ");
			String edadStr = keyboard.nextLine();
			return edadStr.isEmpty() ? null : Integer.parseInt(edadStr);
		};

		Integer cc = leerCc.get();
		String nombre = leerNombre.get();
		String apellido = leerApellido.get();
		Gender genero = leerGenero.get();
		Integer edad = leerEdad.get();

		Person person = new Person();
		person.setIdentification(cc);
		person.setFirstName(nombre);
		person.setLastName(apellido);
		person.setGender(genero);
		person.setAge(edad);
		adapter.crear(person);
	}

	private void editarPersona(PersonaInputAdapterCli adapter, Scanner keyboard) {
		System.out.print("Ingrese el CC de la persona a editar: ");
		Integer cc = keyboard.nextInt();
		keyboard.nextLine();
		System.out.print("Ingrese el nuevo nombre: ");
		String nombre = keyboard.nextLine();
		System.out.print("Ingrese el nuevo apellido: ");
		String apellido = keyboard.nextLine();
		System.out.print("Ingrese el nuevo género (M/F/O): ");
		String gen = keyboard.nextLine().toUpperCase();
		Gender genero = gen.equals("M")
				? Gender.MALE
				: gen.equals("F") ? Gender.FEMALE
						: Gender.OTHER;
		System.out.print("Ingrese la nueva edad (opcional, presione Enter para omitir): ");
		String edadStr = keyboard.nextLine();
		Integer edad = edadStr.isEmpty() ? null : Integer.parseInt(edadStr);

		Person person = new Person();
		person.setIdentification(cc);
		person.setFirstName(nombre);
		person.setLastName(apellido);
		person.setGender(genero);
		person.setAge(edad);
		adapter.editar(cc, person);
	}

	private void eliminarPersona(PersonaInputAdapterCli adapter, Scanner keyboard) {
		System.out.print("Ingrese el CC de la persona a eliminar: ");
		Integer cc = keyboard.nextInt();
		adapter.eliminar(cc);
	}

	private void buscarPersonaPorId(PersonaInputAdapterCli adapter, Scanner keyboard) {
		System.out.print("Ingrese el CC de la persona a buscar: ");
		Integer cc = keyboard.nextInt();
		adapter.buscarPorId(cc);
	}

	private void mostrarMenuOpciones() {
		System.out.println("----------------------");
		System.out.println(OPCION_VER_TODO + " para ver todas las personas");
		System.out.println(OPCION_CREAR + " para crear una persona");
		System.out.println(OPCION_EDITAR + " para editar una persona");
		System.out.println(OPCION_ELIMINAR + " para eliminar una persona");
		System.out.println(OPCION_BUSCAR_POR_ID + " para buscar una persona por CC");
		System.out.println(OPCION_CONTAR + " para contar personas");
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
			keyboard.nextLine(); // Limpiar el buffer
			return leerOpcion(keyboard);
		} catch (NoSuchElementException e) {
			log.error("No hay entrada disponible. Regresando al menú principal.");
			throw new RuntimeException("Entrada no disponible", e);
		}
	}

}
