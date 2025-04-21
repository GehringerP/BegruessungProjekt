import java.util.Scanner;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit; // Für die Berechnung der Tage

/**
 * Ein Programm, das Benutzer nach ihrem Namen und Geburtsdatum fragt,
 * ihr Alter berechnet und eine Alterskategorie ausgibt.
 * Es unterstützt mehrere Personen und enthält umfassende Fehlerbehandlung.
 */
public class Begruessung {
    public static void main(String[] args) {
        Scanner namenscan = new Scanner(System.in);
        String antwort = "";

        // Hauptschleife: Wiederholt, bis der Benutzer "nein" eingibt
        do {
            // Name abfragen und prüfen, ob er leer ist oder ungültige Zeichen enthält
            String name = "";
            do {
                System.out.println("Wie heißt du ?");
                name = namenscan.nextLine();
                if (name.trim().isEmpty()) {
                    System.out.println("Bitte gib einen gültigen Namen ein.");
                } else if (!name.matches("[a-zA-Z\\- ]+")) {
                    System.out.println("Der Name darf nur Buchstaben, Leerzeichen oder Bindestriche enthalten.");
                }
            } while (name.trim().isEmpty() || !name.matches("[a-zA-Z\\- ]+"));

            System.out.println("Hallo " + name);

            // Fehlerbehandlung für das Geburtsdatum
            int alter = 0;
            int geburtsjahr = 0;
            int geburtsmonat = 0;
            int geburtstag = 0;
            boolean gueltigeEingabe = false;
            LocalDate heute = LocalDate.now(); // Aktuelles Datum
            int aktuellesJahr = heute.getYear();
            int aktuellerMonat = heute.getMonthValue();
            int aktuellerTag = heute.getDayOfMonth();

            // Geburtsjahr abfragen
            do {
                System.out.println("In welchem Jahr bist du geboren?");
                String eingabe = namenscan.nextLine();
                try {
                    geburtsjahr = Integer.parseInt(eingabe);
                    if (geburtsjahr > aktuellesJahr) {
                        throw new IllegalArgumentException("Das Geburtsjahr kann nicht in der Zukunft liegen.");
                    }
                    if (geburtsjahr <= aktuellesJahr - 125) {
                        throw new IllegalArgumentException(
                                "Das Geburtsjahr ist zu weit in der Vergangenheit (muss nach " + (aktuellesJahr - 125) + " sein).");
                    }
                    gueltigeEingabe = true;
                } catch (NumberFormatException e) {
                    System.out.println("Bitte gib eine gültige Zahl ein.");
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            } while (!gueltigeEingabe);

            // Geburtsmonat abfragen
            gueltigeEingabe = false;
            do {
                System.out.println("In welchem Monat bist du geboren? (1-12)");
                String eingabe = namenscan.nextLine();
                try {
                    geburtsmonat = Integer.parseInt(eingabe);
                    if (geburtsmonat < 1 || geburtsmonat > 12) {
                        throw new IllegalArgumentException("Der Monat muss zwischen 1 und 12 liegen.");
                    }
                    gueltigeEingabe = true;
                } catch (NumberFormatException e) {
                    System.out.println("Bitte gib eine gültige Zahl ein.");
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            } while (!gueltigeEingabe);

            // Geburtstag abfragen
            gueltigeEingabe = false;
            do {
                System.out.println("An welchem Tag bist du geboren? (1-31)");
                String eingabe = namenscan.nextLine();
                try {
                    geburtstag = Integer.parseInt(eingabe);
                    // Prüfen, ob der Tag im angegebenen Monat/Jahr gültig ist
                    LocalDate geburtsdatum = LocalDate.of(geburtsjahr, geburtsmonat, geburtstag); // Wirft eine Exception, wenn ungültig
                    // Prüfen, ob das Geburtsdatum in der Zukunft liegt
                    if (geburtsdatum.isAfter(heute)) {
                        throw new IllegalArgumentException("Das Geburtsdatum kann nicht in der Zukunft liegen.");
                    }
                    gueltigeEingabe = true;
                } catch (NumberFormatException e) {
                    System.out.println("Bitte gib eine gültige Zahl ein.");
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                } catch (Exception e) {
                    System.out.println("Der Tag ist ungültig für den angegebenen Monat/Jahr (z. B. 1-31 für Januar, 1-28/29 für Februar).");
                }
            } while (!gueltigeEingabe);

            // Präzise Altersberechnung unter Berücksichtigung von Monat und Tag
            alter = aktuellesJahr - geburtsjahr;
            if (aktuellerMonat > geburtsmonat || (aktuellerMonat == geburtsmonat && aktuellerTag >= geburtstag)) {
                // Geburtstag war schon (oder ist heute), also Alter bleibt oder wird erhöht
            } else {
                alter--; // Geburtstag ist noch in der Zukunft
            }

            // Wenn das Alter 0 ist, berechne die Tage
            String altersAusgabe;
            if (alter == 0) {
                LocalDate geburtsdatum = LocalDate.of(geburtsjahr, geburtsmonat, geburtstag);
                long tage = ChronoUnit.DAYS.between(geburtsdatum, heute);
                if (tage == 0) {
                    altersAusgabe = "Du bist heute geboren!";
                } else if (tage == 1) {
                    altersAusgabe = "Du bist 1 Tag alt.";
                } else {
                    altersAusgabe = "Du bist " + tage + " Tage alt.";
                }
            } else {
                altersAusgabe = "Du bist " + alter + " Jahre alt.";
            }

            // Alterskategorie bestimmen (nur für Personen älter als 0 Jahre)
            if (alter < 13) {
                System.out.println("Du bist ein Kind!");
            } else if (alter >= 13 && alter <= 17) {
                System.out.println("Du bist ein Teenager!");
            } else {
                System.out.println("Du bist ein Erwachsener!");
            }

            // Innere Schleife - Eingabevalidierung für "ja/nein"
            do {
                System.out.println("Möchtest du noch eine Person eingeben? (ja/nein)");
                antwort = namenscan.nextLine();
                if (!antwort.equalsIgnoreCase("ja") && !antwort.equalsIgnoreCase("nein")) {
                    System.out.println("Bitte gib 'ja' oder 'nein' ein.");
                }
            } while (!antwort.equalsIgnoreCase("ja") && !antwort.equalsIgnoreCase("nein"));

            // Zusammenfassung
            System.out.println("Hallo " + name + ", " + altersAusgabe);

        } while (antwort.equalsIgnoreCase("ja"));

        // Abschiedsnachricht
        System.out.println("Danke, dass du das Programm benutzt hast!");
        System.out.println("Drücke Enter, um das Programm zu beenden...");
        namenscan.nextLine(); // Wartet auf eine Eingabe vom Benutzer
        namenscan.close();
    }
}
