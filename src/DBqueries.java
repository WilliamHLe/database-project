import java.sql.*;
import java.util.Scanner;

public class DBqueries {

    // Oppgave 1
    // Finne navnet på alle rollene en gitt skuespiller har
    public static void findRoles(Scanner scanner, Connection conn) {
        System.out.println("Finne navnet på alle rollene til en skuespiller.\n----------------");
        scanner.nextLine();
        System.out.print("Navn på skuespiller>>");
        String skuespiller = scanner.nextLine();

        try {
            Statement stmt = conn.createStatement();
            String query = "SELECT DISTINCT Roller FROM SpiltAv NATURAL JOIN Person WHERE Person.Navn = '"+skuespiller+"';";
            ResultSet rs = stmt.executeQuery(query);

            // Sjekker om skuespilleren er i databasen
            if (rs.next()==false) {
                System.out.println("Fant ikke skuespiller med navn " + skuespiller + "\nMente du:");
                listActors(scanner, conn);
            }
            else {
                // Printer alle skuespillere fra databasen
                rs.beforeFirst();
                while (rs.next()) {
                    System.out.println("Roller: " + rs.getString("Roller"));
                }
            }
        }
        catch (Exception e) {
            System.out.println("Error med database:\n"+e);
        }
    }

    // Oppgave 2
    // Finne hvilke filmer som en gitt skuespiller opptrer i
    public static void findMovies(Scanner scanner, Connection conn) {
        System.out.println("Finn hvilke filmer skuespilleren opptrer i.\n----------------");
        // Get input from user:
        scanner.nextLine(); // flush enter-keypress
        System.out.print("Navn på skuespiller>>");
        String skuespiller = scanner.nextLine();

        // Sjekker om skuespilleren er i databasen
        try {
            Statement stmt = conn.createStatement();
            String query = "SELECT DISTINCT Media.Tittel FROM (Media INNER JOIN Filmer ON Media.MediaID = Filmer.MediaID) INNER JOIN (SpiltAv INNER JOIN Person ON Person.PersonID = SpiltAv.PersonID) ON Media.MediaID = SpiltAv.MediaID WHERE Person.Navn = '"+skuespiller+"' ;";
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next() == false) {
                System.out.println("Fant ikke skuespiller!");
            }
            else {
                rs.beforeFirst();
                while(rs.next()) {
                    System.out.println("Tittel: " + rs.getString("Tittel"));
                }
            }
        }
        catch (Exception e) {
            System.out.println("Error med database:\n"+e);
        }

    }
    // Oppgave 3
    // Finne hvilke filmselskap som lager flest filmer innenfor hver sjanger (grøssere, familie, o.l.).
    public static void findProducer(Scanner scanner, Connection conn) {
        System.out.println("Finne hvilke filmselskap som lager flest filmer innen sjanger .\n----------------");
        // Få input fra bruker:
        scanner.nextLine();
        System.out.print("Navn på sjanger>>");
        String genre = scanner.nextLine();

        try {
            Statement stmt = conn.createStatement();
            String query = "Select Utgivelsesselskap.Navn, Utgivelsesselskap.UtgivelsesselskapID, Media.Sjanger, COUNT(Media.Sjanger) AS Antall From Utgivelsesselskap Inner Join UtgittAv ON UtgittAv.UtgivelsesselskapID = Utgivelsesselskap.UtgivelsesselskapID INNER JOIN Media ON Media.MediaID = UtgittAv.MediaID Where Media.Sjanger = '"+genre+"' Group by Utgivelsesselskap.UtgivelsesselskapID ORDER BY Antall DESC;";
            ResultSet rs = stmt.executeQuery(query);

            // Sjekker om sjanger er i databasen
            if (rs.next() == false) {
                System.out.println("Fant ikke sjangeren");
            }
            else {
                rs.beforeFirst();
                System.out.println("Selskap(ene) med flest filmer innen "+genre+":");
                rs.next();
                System.out.println(rs.getString("Navn"));
            }
        }
        catch (Exception e) {
            System.err.println("Error med database:\n"+e);
        }
    }
    // Oppgave 4
    // Sette inn en ny film med regissør, manusforfattere, skuespillere og det som hører med.
    public static void insertMovie(Scanner scanner, Connection conn) {
        System.out.println("Sett inn en film.\n----------------");

        System.out.println("MedieID: ");
        Integer MediaID = scanner.nextInt();

        System.out.println("Tittel: ");
        scanner.nextLine();
        String Tittel = scanner.nextLine();

        System.out.println("Length in minutes: ");
        Integer Lengde = scanner.nextInt();

        System.out.println("Sjanger: ");
        scanner.nextLine();
        String Sjangere = scanner.nextLine();

        System.out.println("Utgivelsesår: ");
        Integer releaseDate = scanner.nextInt();

        // Bør bruke if-else for å sjekke om lanseringsdato er i riktig format
        System.out.println("Lanseringsdato (yyyy-mm-dd) Husk bindestrek: ");
        scanner.nextLine();
        String lansering = scanner.nextLine();

        System.out.println("Ble filmen utgitt på DVD/Bluray?: 1 for Ja, 0 for nei ");
        Integer Format = scanner.nextInt();

        System.out.println("Beskrivelse: ");
        String story = scanner.nextLine();
        // Legger til i tabellen Filmer og tabellen Media
        String query = "INSERT INTO Media(MediaID, Tittel, Lengde, Sjanger, Utgivelsesår, Lanseringsdato, Beskrivelse) VALUES ( '"+MediaID+"', '"+Tittel+"', '"+Lengde+"', '"+Sjangere+"', '"+releaseDate+"', '"+lansering+"', '"+story+"');";
        String query2 = "INSERT INTO Filmer(MediaID, Format) VALUES ( '"+MediaID+"', '"+Format+"');";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            PreparedStatement stmt2 = conn.prepareStatement(query2);

            stmt.execute();
            stmt2.execute();

        }
        catch (Exception e) {
            System.out.println("Feil med database:\n"+e);
        }
        listActors(scanner, conn);


        Integer input = 10;
        while (input >= 0) {

            System.out.println("Skriv inn personer som deltok i filmproduksjonen"
                    + "\nSkriv inn ID på personen."
                    + "\nFor å avslutte, skriv '-1'");



            input = scanner.nextInt();
            if ( input >= 0) {
                System.out.println("Hvilken rolle hadde personen? 1: Skuespiller, 2: Regissør, 3: Manusforfatter");
                Integer input2 = scanner.nextInt();
                String Jobb;
                String query3;
                switch(input2) {
                    case 1:
                        Jobb = "SpiltAv";
                        break;
                    case 2:
                        Jobb = "RegisertAv";
                        break;

                    case 3:
                        Jobb = "ManusAv";
                        break;
                    default:
                        System.out.println("Det er ingen rolle, da var det nok en skuespiller");
                        Jobb = "SpiltAv";
                        break;
                }
                try {
                    if (Jobb == "SpiltAv") {
                        scanner.nextLine();
                        System.out.println("Hvilken rolle spilte skuespilleren?");
                        String Roller = scanner.nextLine();
                        query3 = "INSERT INTO "+Jobb+"(PersonID, MediaID, Roller) VALUES('"+input+"','"+MediaID+"', '"+Roller+"');";
                    }else {
                        query3 = "INSERT INTO "+Jobb+"(PersonID, MediaID) VALUES('"+input+"','"+MediaID+"');";
                    }
                    // Legger til alle som var i filmproduksjonen i databasen
                    PreparedStatement stmt = conn.prepareStatement(query3);
                    stmt.execute();

                }
                catch (Exception e) {
                    System.err.println("Error: "+e);

                }


            }
        }



    }


    // Oppgave 5
    // Sette inn ny anmeldelse av en episode av en serie.
    public static void insertReview(Scanner scanner, Connection conn) {
        System.out.println("Skriv annmeldelse av episode.\n----------------");
        listEpisodes(scanner, conn);
        scanner.nextLine();
        System.out.println("Hvilken episode vil du anmelde? Oppgi ID:");
        Integer MediaID = scanner.nextInt();
        System.out.println("Oppgi AnmeldelsesID:");
        Integer AnmeldelseID = scanner.nextInt();
        System.out.println("Skriv din anmeldelse:");
        scanner.nextLine();
        String tekst = scanner.nextLine();

        System.out.println("Oppgi rangering mellom 1-10:");
        Integer rating = scanner.nextInt();

        // Legger til i tabellen som viser hvilke anmeldelser som tilhører hvilken episode
        String query = "INSERT INTO Anmeldelse values ('"+AnmeldelseID+"', '"+rating+"', '"+tekst+"');";
        String query2 = "INSERT INTO Tilhører values ('"+AnmeldelseID+"', '"+MediaID+"'); ";

        try {
            Statement stmt = conn.createStatement();
            stmt.execute(query);
            stmt.execute(query2);
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    // Metoder som brukes i spørringene
    public static void listActors(Scanner scanner, Connection conn) {
        try {
            System.out.println("Skuespillerne:\n----------");
            Statement stmt = conn.createStatement();
            String query = "select * from Person";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("PersonID") + ", Navn: " + rs.getString("Navn") + ", født: " + rs.getString("Alder") + " i "+ rs.getString("Fødested"));
            }

        }
        catch (Exception e) {
            System.out.println("Error med database:\n"+e);
        }

    }

    // Lister opp alle episoder i databasen
    private static void listEpisodes(Scanner scanner, Connection conn) {
        try {
            System.out.println("Episoder:\n----------");
            Statement stmt = conn.createStatement();
            String query = "select * from Media inner join Episode on Media.MediaID=Episode.MediaID";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                System.out.println("EpisodeID: " +rs.getString("MediaID")+ ". Tittel: " + rs.getString("Tittel") + ".");
            }
        }
        catch (Exception e) {
            System.out.println("Error med database:\n"+e);
        }
    }



}