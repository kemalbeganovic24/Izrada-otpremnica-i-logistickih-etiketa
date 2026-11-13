package repo;

import java.sql.*;

public final class DAOKlase {

    private static final String URL = "jdbc:sqlite:otpremnice.db";

    public static Connection connect() throws SQLException {
        Connection c = DriverManager.getConnection(URL);
        try (Statement st = c.createStatement()) {
            st.execute("PRAGMA foreign_keys = ON");
        }
        return c;
    }

    public static void init() throws SQLException {
        try (Connection c = connect(); Statement st = c.createStatement()) {
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS partner (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    naziv TEXT NOT NULL,
                    adresa TEXT, grad TEXT, gln TEXT
                )""");
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS artikal (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    sifra TEXT NOT NULL UNIQUE,
                    naziv TEXT NOT NULL,
                    gtin TEXT, jedinica TEXT DEFAULT 'kom'
                )""");
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS otpremnica (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    broj TEXT NOT NULL UNIQUE,
                    datum TEXT NOT NULL,
                    posiljalac_id INTEGER NOT NULL REFERENCES partner(id),
                    primalac_id INTEGER NOT NULL REFERENCES partner(id),
                    napomena TEXT
                )""");
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS paleta (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    otpremnica_id INTEGER NOT NULL REFERENCES otpremnica(id) ON DELETE CASCADE,
                    sscc TEXT NOT NULL UNIQUE
                )""");
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS stavka (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    otpremnica_id INTEGER NOT NULL REFERENCES otpremnica(id) ON DELETE CASCADE,
                    artikal_id INTEGER NOT NULL REFERENCES artikal(id),
                    kolicina REAL NOT NULL,
                    lot TEXT, rok_trajanja TEXT,
                    paleta_id INTEGER REFERENCES paleta(id)
                )""");
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS postavke (
                    kljuc TEXT PRIMARY KEY,
                    vrijednost TEXT NOT NULL
                )""");
            st.executeUpdate("INSERT OR IGNORE INTO postavke VALUES ('gs1_prefiks', '3850123')");
            st.executeUpdate("INSERT OR IGNORE INTO postavke VALUES ('sscc_brojac', '0')");
        }
    }
}