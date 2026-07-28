package gestionacademica;

import daos.*;
import views.*;
import controllers.*;
import utils.Mensajes;

public class AppConfig {

    public void iniciar() {
        // 1. Instanciamos los DAOs y las Vistas
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
        EstudianteController controllerEst = new EstudianteController(daoEst, viewEst);
        ProfesorController controllerProf = new ProfesorController(daoProf, viewProf);
        AulaController controllerAula = new AulaController(daoAula);
        InscripcionController controllerInsc = new InscripcionController(daoInsc, viewInsc, daoEst, daoAula);
        AsignacionController controllerAsign = new AsignacionController(daoAsign, viewAsign, daoProf, daoAula);
        ConsultasController controllerCons = new ConsultasController(consView, daoEst, daoProf, daoInsc, daoAsign);
        MenuAula aulaMenu = new MenuAula(controllerAula, viewAula);

        // 3. Bucle del Menu Principal
        int opcion;
        do {
            opcion = mainView.mostrarMenuPrincipal();

            switch (opcion) {
                case 1:
                    controllerEst.iniciar();
                    break;
                case 2:
                    controllerProf.iniciar();
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
