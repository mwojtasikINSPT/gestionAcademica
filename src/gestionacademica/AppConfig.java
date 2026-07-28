package gestionacademica;

import daos.*;
import views.*;
import controllers.*;
import utils.Mensajes;

public class AppConfig {

    public void iniciar() {
        // 1. Instanciamos Capas de datos y las Vistas (no dependen de otras clases)
        EstudianteDAO daoEst = new EstudianteDAO();
        EstudianteView viewEst = new EstudianteView();

        ProfesorDAO daoProf = new ProfesorDAO();
        ProfesorView viewProf = new ProfesorView();

        AulaDAO daoAula = new AulaDAO();
        AulaView viewAula = new AulaView();

        InscripcionDAO daoInsc = new InscripcionDAO();
        InscripcionView viewInsc = new InscripcionView();

        AsignacionDAO daoAsign = new AsignacionDAO();
        AsignacionView viewAsign = new AsignacionView();

        ConsultasView consView = new ConsultasView();
        MainView mainView = new MainView();

        // 2. Ensamblamos los Controladores / Inyeccion de dependencias
        //Capa Lógica (El controlador necesita al DAO para guardar y a la Vista)
        EstudianteController controllerEst = new EstudianteController(daoEst, viewEst);
        ProfesorController controllerProf = new ProfesorController(daoProf, viewProf);
        AulaController controllerAula = new AulaController(daoAula);
        InscripcionController controllerInsc = new InscripcionController(daoInsc, viewInsc, daoEst, daoAula);
        AsignacionController controllerAsign = new AsignacionController(daoAsign, viewAsign, daoProf, daoAula);
        ConsultasController controllerCons = new ConsultasController(consView, daoEst, daoProf, daoInsc, daoAsign);
        
        //3.Enrutador(El menú necesita a la Vista para dibujar las opciones y al Controller para ejecutar)
        AulaMenu aulaMenu = new AulaMenu(controllerAula, viewAula);
        ProfesorMenu profesorMenu = new ProfesorMenu(controllerProf, viewProf);

        // 3. Bucle del Menu Principal
        int opcion;
        do {
            opcion = mainView.mostrarMenuPrincipal();

            switch (opcion) {
                case 1:
                    controllerEst.iniciar();
                    break;
                case 2:
                    profesorMenu.iniciar();
                    break;
                case 3:
                    aulaMenu.iniciar();
                    break;
                case 4:
                    controllerInsc.iniciar();
                    break;
                case 5:
                    controllerAsign.iniciar();
                    break;
                case 6:
                    controllerCons.iniciar();
                    break;
                case 0:
                    mainView.mostrarMensaje(Mensajes.SALIENDO);
                    break;
                default:
                    mainView.mostrarMensaje(Mensajes.OPCION_INVALIDA);
            }
        } while (opcion != 0);
    }
}
