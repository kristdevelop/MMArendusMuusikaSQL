import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class SQLiteAndmed {
    Connection c=null;
    Statement stmt = null;
    public ObservableList<Persoon> persoonid= FXCollections.observableArrayList();  //A list that allows listeners to track changes when they occur.

    public void uhenda(){
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:highScore.db");            //After you've loaded the driver, you can establish a connection getConnection(String url, String user, String password)
        }catch (Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }
    public void sulge(){
        try {
            Class.forName("org.sqlite.JDBC");
            c.close();
        }catch (Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Closed database successfully");
    }
    public void lisaKasutaja(String nimi, int fullscore){
        try {
            stmt=c.createStatement();                       //Before you can use a Statement object to execute a SQL statement, you need to create one using the Connection object's createStatement( ) method
            String sql="INSERT INTO HighScore (Nimi, Score) VALUES ('"+ nimi +"', "+ fullscore +")";
            stmt.execute(sql);                              //Returns a boolean value of true if a ResultSet object can be retrieved; otherwise, it returns false. Use this method to execute SQL DDL statements or when you need to use truly dynamic SQL.
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Records created successfully");
        sulge();
    }
    public void loeAndmed(){
        uhenda();
        try {
            stmt=c.createStatement();
            ResultSet sql=stmt.executeQuery( "SELECT * FROM HighScore ORDER BY Score DESC" );       //Returns a ResultSet object. Expect to get a result set, as you would with a SELECT statement.
            while ( sql.next() ) {
                persoonid.add(new Persoon(
                        sql.getString("Score"),
                        sql.getString("Nimi")
                ));
            }
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Database read successfully");
        sulge();
    }
}
