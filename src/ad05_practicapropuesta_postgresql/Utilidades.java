/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ad05_practicapropuesta_postgresql;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Moy
 */
public class Utilidades {
    private Connection conn = null;
    private Statement statement =null;
    private ResultSet resultSet =null;
    

    public Utilidades() {
        
        
    }
    
    private void iniciarConexion() {
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost/prueba", "java", "java");
        } catch (SQLException ex) {
            Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Excepcion al abrir la conexión");
        }
    }
    
    private void cerrar() {
        try {
            if(statement!=null)statement.close();
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("Excepcion al cerrar la conexión");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void eliminarSiExisten()
    {
        iniciarConexion();
        String sentencia = "drop table if exists Articulo cascade;"
                + "drop type if exists Origen cascade;"
                + "drop table if exists Categoria cascade;";
        try {
            statement = conn.createStatement();
            statement.execute(sentencia);
            System.out.println("Borrado");
        } catch (SQLException ex) {
            Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            try {
                statement.close();
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    /*Crear el tipo de datos compuesto definido por el usuario Origen, que 
    contendrá los datos simples: País, Ciudad y Mail de contacto.*/
    public void crearTablasYTipo(){
        try {
            iniciarConexion();
            statement = conn.createStatement();
            String sentencia= ""
                    + "Create type Origen as("
                    + "Pais varchar(20),"
                    + "Ciudad varchar(20),"
                    + "Mail varchar(20));"
                    
                    + "create table Categoria("
                    + "nombre_categoria varchar(20), "
                    + "categoria_id serial, "
                    + "Constraint pk_categoria primary key (categoria_id));"
                    
                    + "Create table Articulo("
                    + "nombre varchar(20),"
                    + "Articulo_id serial, "
                    + "Categoria_id integer,"
                    + "fuente Origen,"
                    + "Constraint pk_articulo Primary key (Articulo_id),"
                    + "Constraint fk_categoria Foreign key (Categoria_id) references Categoria(categoria_id)); ";
            statement.execute(sentencia);
        } catch (SQLException ex) {
            Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            cerrar();
        }}
    public void llenarDB(){//nombre, Categoria_id, fuente
        try {
            iniciarConexion();
            String sentencia = "insert into Categoria values ('hogar');"
                    + "insert into Categoria values ('electronica');"
                    + "insert into Categoria values ('ocio');"
            
                    + "insert into Articulo (nombre, Categoria_id, fuente) values ('cortina',1,ROW('España','Valencia','corinas@gmail.com'));"
                    + "insert into Articulo (nombre, Categoria_id, fuente) values ('mesa',1,ROW('Brasil','Brasilia','mesas@mesas.com'));"
                    + "insert into Articulo (nombre, Categoria_id, fuente) values ('pendrive',2,ROW('Alemania','Berlin','pendrives@pen.com'));"
                    + "insert into Articulo (nombre, Categoria_id, fuente) values ('CD',2,ROW('EEUU','Minesota','cds@cds.com'));"
                    + "insert into Articulo (nombre, Categoria_id, fuente) values ('Balon',3,ROW('Argentina','Entrerrios','balones@balon.com'));";
            statement = conn.createStatement();
            int registros = statement.executeUpdate(sentencia);
            System.out.println(""+registros+" registros afectados;");
        } catch (SQLException ex) {
            Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
        } finally{cerrar();}
    }
    
    /*Realizar, al menos, 3 consultas diferentes que seleccionen datos de: tabla Artículos, 
    tabla Categorías y ambas tablas. 
    Algunas de las consultas deben incluir condiciones where.*/
    public void consultarArticulos(){
        iniciarConexion();
        try {
            statement = conn.createStatement();
            //String sentencia = "select Articulo.nombre, (fuente).Mail from Articulo where Categoria_id = 2;";
            String sentencia = "select Articulo.nombre, Categoria.nombre_categoria from Articulo,Categoria where Articulo.Categoria_id = Categoria.categoria_id and Articulo.Categoria_id =1;";
            resultSet = statement.executeQuery(sentencia);
            mostrarDatos(resultSet);
            
            sentencia = "select (fuente).pais, (fuente).mail, Categoria.nombre_categoria from Articulo, Categoria where Articulo.Categoria_id = Categoria.categoria_id;";
            resultSet = statement.executeQuery(sentencia);
            mostrarDatos(resultSet);
        } catch (SQLException ex) {
            Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
        }finally{cerrar();}
    }

    private void mostrarDatos(ResultSet resultSet) throws SQLException {
        ResultSetMetaData rsmd = resultSet.getMetaData();
        for (int i = 0; i < rsmd.getColumnCount(); i++) {
            int j = i + 1;
            System.out.print("\t" + rsmd.getColumnName(j));
        }
        System.out.println("");
        while (resultSet.next()) {
            for (int i = 0; i < rsmd.getColumnCount(); i++) {
                System.out.print("\t" + resultSet.getString(i + 1));
            }
            System.out.println("");
        }
    }
    
    /*Actualizar la categoría de todos los artículos que coincidan con un cierto código por otra.*/
    public void actualizarCategoria(){
        
            iniciarConexion();
        try {
            String sentencia = "update Articulo set Categoria_id = 3 where Categoria_id = 2";
            statement = conn.createStatement();
            int resultado = statement.executeUpdate(sentencia);
            System.out.println("resultado: "+resultado);
        } catch (SQLException ex) {
            Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
        }finally{cerrar();}
        
    }
    public void eliminarCategoria()
    {
        
            iniciarConexion();
        try {
            String sentencia = "delete from Categoria where categoria_id = 2";
            statement = conn.createStatement();
            int resultado = statement.executeUpdate(sentencia);
            System.out.println("Resultado :"+resultado);
        } catch (SQLException ex) {
            Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
        }finally{cerrar();}
    }
    
    
}
