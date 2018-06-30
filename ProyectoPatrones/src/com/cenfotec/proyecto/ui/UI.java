package com.cenfotec.proyecto.ui;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;

import com.cenfotec.proyecto.clases.Gestor;
import com.cenfotec.proyecto.clases.Historial;
import com.cenfotec.proyecto.clases.Proceso;
import com.cenfotec.proyecto.clases.Tarea;
import com.cenfotec.proyecto.clases.Usuario;

/*OBSERVACIONES
 * -El gestor puede declararse de forma global
 * -A la hora de mostrar los menus, en ves de hacerlos en métodos aparte, se pudo hacer en el mismo
 * -Cuando se realizan los procesos, hacerlo en metodos más específicos
 * */

public class UI {

	static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	static PrintStream out = System.out;
	static String correoUsuario = "";
	static int contador = 0;
	
	/*Se ingresan los datos para iniciar sesión*/
	public static void main(String[] args)throws java.io.IOException {
		
		if(contador == 0) {
			Gestor gestor = new Gestor();
			gestor.quemarDatos();
		}
		
		contador++;
		
		String correo, contrasenna;
		boolean iniciar = false;
		
		do {
			out.println("<---- Iniciar sesión ---->");
			out.println("");
			out.println("Digite su correo electrónico");
			correo = in.readLine();
			out.println("Digite su contraseña");
			contrasenna = in.readLine();
			
			iniciar = iniciarSesion(correo, contrasenna);
			
			if(iniciar) {
				out.println("Bienvenido");
				mostrarMenuGrupo();
			} else {
				out.println("Los datos ingresados son incorrectos");
			}
		}while(!iniciar);
	}
	
	/*Valida los datos ingresados para el inicio de sesión*/
	static boolean iniciarSesion(String pCorreo, String pContrasenna)throws java.io.IOException{
		boolean iniciar = false;
		Gestor gestor = new Gestor();
		String correo, contrasenna;
		
		ArrayList<Usuario> listaUsuarios = gestor.getListaUsuarios();
		for(int i = 0; i < listaUsuarios.size(); i++) {
			
			correo = listaUsuarios.get(i).getCorreo();
			contrasenna = listaUsuarios.get(i).getContrasenna();
			
			if(pCorreo.equals(correo) && pContrasenna.equals(contrasenna)) {
				correoUsuario = correo;
				iniciar = true;
			}
		}
		
		return iniciar;
	}//Poner en clase InicioSesion?
	
	/*Redirecciona al usuario dependiendo del grupo al que pertenezca*/
	static void mostrarMenuGrupo()throws java.io.IOException{
		Usuario usuario;
		
		usuario = obtenerUsuario();
		
		if(usuario.getGrupo().equals("Administrador")) {//Validar grupo en InicioSesion
			verMenuAdministrador();
		}else {
			verMenuGrupo();
		}
	}//InicioSesion?
	
	/*Devuelve al usuario de acuerdo al correo con el que se inició sesión*/
	static Usuario obtenerUsuario()throws java.io.IOException{
		Usuario usuario = new Usuario();
		String correo;
		Gestor gestor = new Gestor();
		
		ArrayList<Usuario> listaUsuarios = gestor.getListaUsuarios();
		
		for(int i = 0; i < listaUsuarios.size(); i++) {
			
			correo = listaUsuarios.get(i).getCorreo();
			
			if(correo.equals(correoUsuario)) {
				usuario = listaUsuarios.get(i);
			}
		}
		
		return usuario;
	}//Usuario*
	
	/*Muestra el menú del usuario administrador*/
	static void verMenuAdministrador()throws java.io.IOException{
		int opc;
		boolean noSalir = true;
		String[] listaMenu = {
							"<---- Menú manejo de procesos ---->",
							" ",
							"1.  Crear proceso",
							"2.  Registrar usuario",
							"3.  Ver historial",
							"4.  Salir",
							};

		do{
			mostrarMenu(listaMenu);
			opc = leerOpcion();
			noSalir = ejecutarMenuAdministrador(opc);
		}while (noSalir);
		if(noSalir == false) {
			main(null);
		}
	}
	
	/*Muestra el menú para los demás grupos --- (Se puede hacer diferente)*/
	static void verMenuGrupo()throws java.io.IOException{
		int opc;
		boolean noSalir = true;
		String[] listaMenu = {
							"<---- Menú de ejecución de procesos ---->",
							" ",
							"1.  Ejecutar proceso",
							"2.  Ver procesos disponibles",
							"3.  Salir",
							};

		do{
			mostrarMenu(listaMenu);
			opc = leerOpcion();
			noSalir = ejecutarMenuGrupo(opc);
		}while (noSalir);
		if(noSalir == false) {
			main(null);
		}
	}//Modificar metodo
	
	/*Imprime el arreglo del menú*/
	static void mostrarMenu(String[] plista){
		
		out.println();
		
		for(int i=0;i<plista.length;i++){
			out.println(plista[i]);
		}
		out.println();
	}//eliminar metodo luego de modificar el verMenuGrupo
	
	/*Se ingresa la opción deseada y la retorna*/
	static int leerOpcion()throws java.io.IOException{
		
		int opcion;

		out.print("Seleccione su opción: ");
		opcion = Integer.parseInt(in.readLine());
		out.println();

		return opcion;
	}
	
	/*Ejecuta los metodos de acuerdo a la opción ingresada por el administrador*/
	static boolean ejecutarMenuAdministrador(int popcion)throws java.io.IOException{
		boolean noSalir = true;
		
		switch(popcion){
		case 1:
			crearProceso();
			break;

		case 2:
			registrarUsuario();
			break;
			
		case 3:
			verHistorial();
			break;

		case 4:
			noSalir = false;
			correoUsuario = "";
			break;

		default:
			out.println("Opcion invalida");
			out.println();
			break;
		}
		
		return noSalir;
	}//Enviar a clase MenuDeSistema
	
	/*Ejecuta los metodos de acuerdo a la opción ingresada por el usuario*/
	static boolean ejecutarMenuGrupo(int popcion)throws java.io.IOException{
		boolean noSalir = true;
		int num = 0;
		
		switch(popcion){
		
		case 1:
			ejecutarProceso();
			break;

		case 2:
			num = verProcesos();
			break;

		case 3:
			noSalir = false;
			correoUsuario = "";
			break;

		default:
			out.println("Opcion invalida");
			out.println();
			break;
		}
		
		return noSalir;
	}//Enviar a clase MenuDeSistema
	
	/*Se crea un proceso*/
	static void crearProceso()throws java.io.IOException{
		String nomProceso;
		int cantTareas;
		ArrayList<Tarea> listaTareas = new ArrayList<Tarea>();
		Gestor gestor = new Gestor();
		
		out.println("Indique el nombre del proceso que desea realizar");
		nomProceso = in.readLine();
		
		out.println("Indique la cantidad de tareas que desea asignarle al proceso");
		cantTareas = Integer.parseInt(in.readLine());
		
		listaTareas = contruirTarea(cantTareas);
		
		gestor.crearProceso(nomProceso, listaTareas);
	}//Enviar a Proceso
	
	/*Se crean las partes de una tarea y retorna una lista de tareas*///*************************************************
	
	
	
	
	static ArrayList<Tarea> contruirTarea(int pCantTareas)throws java.io.IOException{
		String titTarea, grupo;
		int resInd;
		ArrayList<String> listaIndicaciones = new ArrayList<String>();
		Tarea tarea = new Tarea();
		Gestor gestor = new Gestor();
		ArrayList<Tarea> listaTareas = new ArrayList<Tarea>();
		
		for(int i = 0; i < pCantTareas; i++) {
			
			out.println("Indique el título de la tarea " + (i+1));
			titTarea = in.readLine();
			
			out.println("Indique el grupo responsable de la tarea " + (i+1));
			grupo = in.readLine();
			
			do {
				out.println("¿Desea ingresar un formulario o una pregunta a la tarea " + (i+1) + " ?");
				out.println("Digite 1 para formulario ó 2 para pregunta");
				resInd = Integer.parseInt(in.readLine());
				
				if(!(resInd > 2 || resInd < 0)) {
					
					listaIndicaciones = crearIndicaciones(resInd, i);
				} else {
					out.println("Opción incorrecta, por favor digite 1 para formulario ó 2 para pregunta");
				}
			}while(resInd > 2 || resInd < 0);
			
			tarea = gestor.crearTarea(titTarea, grupo, listaIndicaciones);
			listaTareas.add(tarea);
		}
		
		return listaTareas;
	}//Duda de que hacer*****************************************************************************************************
	
	/*Se crean las indicaciones para la tarea y se devuelve un arreglo de estas*/
	static ArrayList<String> crearIndicaciones(int pRestIndi, int pI)throws java.io.IOException{
		int cantDatos;
		String indiDato, resPregunta;
		ArrayList<String> listaIndicaciones = new ArrayList<String>();
		
		if(pRestIndi == 1) {
			out.println("Indique la cantidad de datos que desea agregar al formulario");
			cantDatos = Integer.parseInt(in.readLine());
			
			for(int j = 0; j < cantDatos; j++) {
				out.println("Digite la indicación del dato " + (j+1));
				indiDato = in.readLine();
				
				listaIndicaciones.add(indiDato);
			}
		} else {
			out.println("Digite la pregunta de la tarea " + (pI+1));
			resPregunta = in.readLine();
			
			listaIndicaciones.add(resPregunta);
		}
		
		return listaIndicaciones;
	}//*****************************************************************
	
	
	
	
	
	/*Se registran los datos de un nuevo usuario*/
	static void registrarUsuario()throws java.io.IOException{
		String nombre, apellido, grupo, correo, contrasenna;
		Gestor gestor = new Gestor();
		boolean errorC = false;
		boolean error = false;
		
		do {
			out.println("Digite el nombre del usuario");
			nombre = in.readLine();
			
			out.println("Digite el apellido del usuario");
			apellido = in.readLine();
			
			out.println("Digite el grupo al que pertenece el usuario");
			grupo = in.readLine();
			
			do {
				out.println("Digite el correo electrónico del usuario");
				correo = in.readLine();
				
				errorC = gestor.validarCorreo(correo);
				
				if(errorC) {
					out.println("El correo que desea registrar ya pertenece a un usuario registrado; Por favor digite un correo diferente");
				}
			}while(errorC);
			
			out.println("Digite la contrasenna del usuario");
			contrasenna = in.readLine();
			
			String[] datos = {nombre, apellido, grupo, correo, contrasenna};
			error = validarDatosUsuario(datos);
			
			if(error) {
				out.println("Dejó datos en blanco; Por favor digite los datos correctamente");
			}
		}while(error);
		
		gestor.crearUsuario(nombre, apellido, grupo, correo, contrasenna);
	}

	
	/*Se validan los datos del usuario*/
	static boolean validarDatosUsuario(String[] pDatos)throws java.io.IOException{
		boolean error = false;
		
		for(int i = 0; i < pDatos.length; i++) {
			if(pDatos[i].equals("") || pDatos[i].equals(" ")) {
				error = true;
			}
		}
		
		return error;
	}
	
	/*Se selecciona el proceso para ejecutar la tarea de acuerdo al grupo que pertenece el usuario*/
	static void ejecutarProceso()throws java.io.IOException{
		int cantProc = 0, opc;
		Proceso proceso;
		
		cantProc = verProcesos();
		
		if(cantProc > 0) {
			out.println("Seleccione el número del proceso que desea realizar");
			opc = Integer.parseInt(in.readLine());
			proceso = seleccionarProceso(opc);	
			completarTarea(proceso);
		}
	}
	
	static void verHistorial()throws java.io.IOException{
		Gestor gestor = new Gestor();
		ArrayList<Historial> listaHistorial = gestor.getListaHistorial();
		Historial historial;
		
		if(listaHistorial != null) {
			for(int i = 0; i < listaHistorial.size(); i++) {
				historial = listaHistorial.get(i);
				out.println("Se ejecutó el proceso: " + historial.getNomProceso());
				out.println("Se completó la tarea: " + historial.getTituloTarea());
				out.println("Por: " + historial.getAutor());
				out.println("La fecha: " + historial.getFecha());
				out.println("A la hora: " + historial.getHora());
				out.println("");
				out.println("<-------------------------------->");
				out.println("");
			}
		}else {
			out.println("No se han ejecutado procesos");
		}
		
	}
	
	/*Se muestran los procesos y las tareas de acuerdo al grupo que pertenece el usuario*/
	static int verProcesos()throws java.io.IOException{
		Usuario usuario = obtenerUsuario();
		Gestor gestor = new Gestor();
		ArrayList<Proceso> listaProcesos = gestor.getListaProcesos();
		Proceso proceso = new Proceso();
		int indice, contador = 0;
		ArrayList<Tarea> listaTareas = new ArrayList<Tarea>();
		String grupoUsuario, grupoTarea;
		Tarea tarea = new Tarea();
		
		for(int i = 0; i < listaProcesos.size(); i++) {
			
			proceso = listaProcesos.get(i);
			indice = proceso.getIndiceTarea();
			
			listaTareas = proceso.getTareas();
			tarea = listaTareas.get(indice);
			
			grupoUsuario = usuario.getGrupo();
			grupoTarea = tarea.getGrupoResponsable();
			
			if(grupoUsuario.equals(grupoTarea)) {
				out.println("");
				out.println((contador + 1) + ".");
				out.println("Proceso: " + proceso.getNomProceso());
				out.println("Tarea: " + tarea.getTitulo());
				out.println("");
				out.println("<---------------------->");
				contador++;
			}
		}
		
		if(contador == 0) {
			out.println("");
			out.println("No hay tareas de procesos a ejecutar disponibles");
			out.println("");
		}
		
		return contador;
	}
	
	/*Se selecciona el proceso de acuerdo a la tarea del grupo seleccionado por el usuario*/
	static Proceso seleccionarProceso(int opc)throws java.io.IOException{
		Gestor gestor = new Gestor();
		ArrayList<Proceso> listaProcesos = gestor.getListaProcesos();
		Proceso proceso = new Proceso();
		int indice, contador = 0;
		ArrayList<Tarea> listaTareas = new ArrayList<Tarea>();
		Tarea tarea = new Tarea();
		Usuario usuario = obtenerUsuario();
		String grupoUsuario, grupoTarea;
		Proceso procesoSelec = new Proceso();;
		
		
		for(int i = 0; i < listaProcesos.size(); i++) {
			proceso = listaProcesos.get(i);
			indice = proceso.getIndiceTarea();
			listaTareas = proceso.getTareas();
			tarea = listaTareas.get(indice);
			
			grupoUsuario = usuario.getGrupo();
			grupoTarea = tarea.getGrupoResponsable();
			
			if(grupoUsuario.equals(grupoTarea)) {
				
				if((opc-1) == contador) {
					procesoSelec = proceso;
				}
				
				contador++;
			}
		}
		
		return procesoSelec;
	}
	
	/*Se ejecuta la tarea de acuerdo al grupo del usuario*/
	static void completarTarea(Proceso pProceso)throws java.io.IOException{
		int indice = pProceso.getIndiceTarea();
		ArrayList<Tarea> listaTareas = pProceso.getTareas();
		Tarea tarea = listaTareas.get(indice);
		ArrayList<String> indicaciones = tarea.getIndicaciones();
		ArrayList<String> respuestas = new ArrayList<String>();
		Tarea tarAct = new Tarea();
		Gestor gestor = new Gestor();
		Proceso proAct = new Proceso();
		Usuario usuario = obtenerUsuario();
		
		for(int i = 0; i < indicaciones.size(); i++) {
			out.println(indicaciones.get(i));
			respuestas.add(in.readLine());
		}
		
		tarAct = gestor.actualizarTarea(tarea, respuestas);
		listaTareas.remove(indice);
		listaTareas.add(indice, tarAct);
		proAct = gestor.actulizarProceso(pProceso, listaTareas);
		gestor.actualizarListaProcesos(proAct);
		
		gestor.registrarHistorial(pProceso.getNomProceso(), tarea.getTitulo(), usuario.getNombre() + " " + usuario.getApellido());
	}
}