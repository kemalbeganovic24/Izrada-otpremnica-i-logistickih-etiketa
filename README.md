# Otpremnice i logističke etikete
 
Java aplikacija za izradu otpremnica i GS1 logističkih etiketa (SSCC, GS1-128) sa generisanjem PDF dokumenata.
 
## Funkcionalnosti
 
- Izrada otpremnica sa stavkama (artikal, količina, LOT, rok trajanja)
- Podjela stavki po paletama, svaka paleta dobija jedinstven SSCC
- Generisanje GS1-128 barkoda (OkapiBarcode)
- PDF logistička etiketa 10x15 cm (SSCC, primalac, sadržaj palete, barkod)
- PDF otpremnica (A4) *(u izradi)*
- Pohrana podataka u lokalnu SQLite bazu
## Tehnologije
 
| Namjena | Tehnologija |
|---|---|
| Jezik | Java 21 |
| Build | Maven |
| IDE | JetBrains IntelliJ IDEA |
| Barkodovi | [OkapiBarcode](https://github.com/woo-j/OkapiBarcode) (GS1-128) |
| PDF | Apache PDFBox 3.x |
| Baza | SQLite (`sqlite-jdbc`) |
| GUI | JavaFX *(planirano)* |
 
## GS1 standardi
 
- **SSCC** (AI 00): 18 cifara = extension digit + GS1 prefiks kompanije + serijski broj + kontrolna cifra (modulo 10)
- **GS1-128** barkod nosi SSCC i opcionalno LOT (AI 10)
- Podržano proširenje: GTIN (01), rok trajanja (15/17), količina (37), odredište (410)
> Prefiks `3850123` u bazi je probni. Pravi GS1 prefiks dobija se od nadležne GS1 organizacije i mijenja se u tabeli `postavke` (ključ `gs1_prefiks`).
 
## Struktura projekta
 
```
src/main/java/
├── model/      Partner, Artikal, Otpremnica, StavkaOtpremnice, Paleta
├── gs1/        Gs1Util (kontrolne cifre, SSCC), BarcodeService
├── repo/       Database (šema, konekcija), DAO klase
├── service/    OtpremnicaService, EtiketaService
├── pdf/        EtiketaPdf, EtiketaPodaci, (OtpremnicaPdf)
└── ui/         JavaFX kontroleri (planirano)
src/main/resources/
└── fonts/      DejaVuSans.ttf, DejaVuSans-Bold.ttf
```
 
## Pokretanje
 
1. Kloniraj repozitorij i otvori ga u IntelliJ IDEA kao Maven projekat.
2. Preuzmi font **DejaVu Sans** i stavi `DejaVuSans.ttf` i `DejaVuSans-Bold.ttf` u `src/main/resources/fonts/` (potrebno za slova č, ć, đ, š, ž u PDF-u).
3. Pokreni `Database.init()` za kreiranje baze `otpremnice.db` (nastaje u korijenu projekta).
4. Pokreni test-klasu koja generiše `etiketa-test.pdf`.
## Primjer korištenja
 
```java
Database.init();
EtiketaService servis = new EtiketaService();
String sscc = servis.sljedeciSscc();
 
EtiketaPodaci podaci = new EtiketaPodaci(
        "Željezara d.o.o., Zenica",
        "Šped-Trans d.o.o.",
        "Šantićeva 12, 71000 Sarajevo",
        "OTP-2026/0001",
        1, 3, sscc,
        List.of("Čelični profil 40x40 - 120 kom"));
 
servis.generisiPdf(Path.of("etiketa-test.pdf"), podaci, "LOT42");
```
 
## Šema baze
 
`partner`, `artikal`, `otpremnica`, `paleta`, `stavka`, `postavke`
 
SSCC brojač se trajno čuva u tabeli `postavke` (`sscc_brojac`), pa se SSCC nikad ne ponavlja ni nakon restarta aplikacije.
 
## Plan razvoja
 
- [x] GS1 jezgro (kontrolna cifra, SSCC)
- [x] GS1-128 barkod
- [x] Baza i generator SSCC-a
- [x] PDF etiketa 10x15 cm
- [ ] CRUD repozitoriji (partneri, artikli, otpremnice)
- [ ] PDF otpremnica (A4)
- [ ] JavaFX korisničko sučelje
      
## Članovi
 
Kemal Beganović
Emir Đelilbašić
Ramo Kozlić

Politehnički fakultet Univerziteta u Zenici, Elektronsko poslovanje, akademska 2026/2027.
