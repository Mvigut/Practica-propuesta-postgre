/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ad05_practicapropuesta_postgresql;

import java.sql.*;
/**
 *
 * @author Moy
 */
public class Main {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Utilidades util = new Utilidades();
        util.eliminarSiExisten();
        util.crearTablasYTipo();
        util.llenarDB();
        util.consultarArticulos();
        util.actualizarCategoria();
        util.eliminarCategoria();
    }
    
}
