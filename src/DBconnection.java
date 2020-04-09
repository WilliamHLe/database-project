import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.util.Scanner;

public class DBconnection {

    private String url;
    private String username;
    private String password;

    protected Connection conn;

    public DBconnection() {
        DBconnection.checkDriver();
    }
    public static void main(String[] args) {
        DBconnection dbproject = new DBconnection();

        Connection con = dbproject.connect();
        Scanner scanner = new Scanner(System.in);

        // Dette er valgmenyen til brukeren
        boolean cont = true;
        System.out.println("Velkommen til programmet vårt");
        System.out.println("Velg funksjon:\n"
                + " 1:\tFinne navnet på alle roller til en skuespiller\n"
                + " 2:\tFinne hvilke filmer en skuespiller opptrer i\n"
                + " 3:\tFinne hvilke filmselskap som lager flest filmer innenfor en sjanger\n"
                + " 4:\tSette inn en ny film\n"
                + " 5:\tSette inn annmeldelse.\n"
                + "Eller 0 for å avslutte");
        while (cont) {
            System.out.println(".\n.");
            System.out.println("Meny");
            System.out.print(">>");
            int valg = -1;
            try {
                valg = scanner.nextInt();
            }catch(Exception e) {
                scanner.nextLine();
                System.out.println("Må være et heltall 0-7");
            }

            switch (valg) {
                case 0:
                    cont = false;
                    break;
                case 1:
                    DBqueries.findRoles(scanner, con);
                    break;
                case 2:
                    DBqueries.findMovies(scanner, con);
                    break;
                case 3:
                    DBqueries.findProducer(scanner, con);
                    break;
                case 4:
                    DBqueries.insertMovie(scanner, con);
                    break;
                case 5:
                    DBqueries.insertReview(scanner, con);
                    break;
                default:
                    System.out.println("Ikke et gyldig nummer!");
                    break;
            }
        }
        System.out.println("Program avsluttet.");
    }

    private static void checkDriver() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Connection connect() {
        // Tilkoblingsinformasjon for databasen
        url = "jdbc:mysql://127.0.0.1:3306/filmdatabase";
        username = "root";
        password = "23071998";

        try {
            Properties p = new Properties();
            p.put("user", username);
            p.put("password", password);
            conn = DriverManager.getConnection(url, p);
        } catch (Exception e)
        {
            throw new RuntimeException("Unable to connect to database", e);
        }
        return conn;
    }








}


