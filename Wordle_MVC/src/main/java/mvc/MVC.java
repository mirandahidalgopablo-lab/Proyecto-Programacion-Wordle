package mvc;

import javax.swing.UIManager;
import controlador.MenuCtrl;

public class MVC {

    public static void main(String[] args) {
        UIManager.put("OptionPane.yesButtonText", "Sí");
        UIManager.put("OptionPane.noButtonText", "No");
        UIManager.put("OptionPane.cancelButtonText", "Cancelar");
        UIManager.put("OptionPane.okButtonText", "Aceptar");

        MenuCtrl ctrl = new MenuCtrl();
        ctrl.iniciar();
    }
}
