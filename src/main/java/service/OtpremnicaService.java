package service;

import model.Paletа;

import java.sql.SQLException;

public class OtpremnicaService {
    private final EtiketaService etiketaService = new EtiketaService();

    public Paletа dodajPaletu(long otpremnicaId) throws SQLException {
        String sscc = etiketaService.sljedeciSscc();
        // INSERT INTO paleta (otpremnica_id, sscc) VALUES (?, ?) ...
        // vrati Paleta record
        return null;
    }
}
