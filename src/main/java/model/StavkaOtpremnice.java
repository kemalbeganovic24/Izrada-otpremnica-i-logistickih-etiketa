package model;

public record StavkaOtpremnice(long id, long otpremnicaId, long artikalId,
                               double kolicina, String lot, java.time.LocalDate rokTrajanja,
                               Long paletaId) {}