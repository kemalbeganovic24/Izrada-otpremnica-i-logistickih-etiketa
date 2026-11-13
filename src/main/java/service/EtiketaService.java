package service;

import gs1.BarcodeService;
import gs1.Gs1Util;
import repo.DAOKlase;

import java.awt.image.BufferedImage;
import java.sql.*;

public class EtiketaService {

    private final BarcodeService barcodeService = new BarcodeService();

    public synchronized String sljedeciSscc() throws SQLException {
        try (Connection c = DAOKlase.connect()) {
            c.setAutoCommit(false);
            try {
                String prefiks = citaj(c, "gs1_prefiks");
                long brojac = Long.parseLong(citaj(c, "sscc_brojac")) + 1;

                try (PreparedStatement ps = c.prepareStatement(
                        "UPDATE postavke SET vrijednost = ? WHERE kljuc = 'sscc_brojac'")) {
                    ps.setString(1, String.valueOf(brojac));
                    ps.executeUpdate();
                }
                c.commit();
                return Gs1Util.sscc(0, prefiks, brojac);
            } catch (SQLException | RuntimeException e) {
                c.rollback();
                throw e;
            }
        }
    }

    public BufferedImage barkodZaPaletu(String sscc, String lot) {
        return barcodeService.gs1_128("[00]" + sscc + (lot != null ? "[10]" + lot : ""));
    }

    private String citaj(Connection c, String kljuc) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(
                "SELECT vrijednost FROM postavke WHERE kljuc = ?")) {
            ps.setString(1, kljuc);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getString(1);
            }
        }
    }
}