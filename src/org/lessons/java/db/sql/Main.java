package org.lessons.java.db.sql;

import java.sql.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        String URL = "jdbc:mysql://localhost:3306/db_nations";
        String USER = "root";
        String PASSWORD = "root";

        Scanner scan = new Scanner(System.in);
        System.out.print("Cerca una nazione: ");
        String userChoice = "%";
        userChoice += scan.nextLine();
        userChoice += "%";
        scan.close();


        try(Connection con = DriverManager.getConnection(URL, USER, PASSWORD)){
            String sql = """
                    SELECT countries.name, countries.country_id, regions.name, continents.name  FROM regions
                    JOIN continents on regions.continent_id = continents.continent_id
                    JOIN countries on regions.region_id = countries.region_id
                    WHERE countries.name LIKE ?
                    ORDER BY countries.name;
                    """;

            try(PreparedStatement ps = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

                ps.setString(1, userChoice);

                try(ResultSet rs = ps.executeQuery()){

                    if(!rs.next()){
                        System.out.println("Non ci sono risultati per la tua ricerca");
                    } else {
                        rs.beforeFirst();
                    }

                    while (rs.next()){
                        String regionName = rs.getString(1);
                        String continentName = rs.getString(3);
                        String countryName = rs.getString(4);

                        System.out.println(regionName + " " + continentName + " " + countryName);
                    }
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
