package model;

public record Otpremnica(long id, String broj, java.time.LocalDate datum,
                         long posiljalacId, long primalacId, String napomena) {}
