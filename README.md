# Otpremnice i logističke etikete

Desktop aplikacija u Javi za izradu otpremnica i GS1 logističkih etiketa (SSCC, GS1-128) sa generisanjem PDF dokumenata.

## Funkcionalnosti

- Upravljanje partnerima (pošiljaoci i primaoci) i artiklima
- Izrada otpremnica s automatskim brojem (`OTP-GODINA/0001`)
- Podjela stavki po paletama, svaka paleta dobija jedinstven SSCC
- Generisanje GS1-128 barkoda (OkapiBarcode)
- PDF logistička etiketa 10x15 cm (SSCC, primalac, sadržaj palete, barkod)
- PDF otpremnica (A4) s tabelom stavki i poljima za potpis
- Trajna pohrana podataka u MySQL bazu
- JavaFX korisničko sučelje

## Tehnologije

| Namjena | Tehnologija |
|---|---|
| Jezik | Java 27 |
| Build | Maven |
| IDE | JetBrains IntelliJ IDEA |
| GUI | JavaFX 26 (FXML) |
| Barkodovi | [OkapiBarcode](https://github.com/woo-j/OkapiBarcode) (GS1-128) |
| PDF | Apache PDFBox 3.x |
| Baza | MySQL 8 (`mysql-connector-j`), pregled kroz MySQL Workbench |
| Font u PDF-u | DejaVu Sans (podrška za č, ć, đ, š, ž) |

## GS1 standardi

- **SSCC** (AI 00): 18 cifara = extension digit + GS1 prefiks kompanije + serijski broj + kontrolna cifra (modulo 10)
- **GS1-128** barkod nosi SSCC i, ako je na paleti jedan LOT, i LOT (AI 10)
- SSCC brojač se trajno čuva u bazi (`postavke.sscc_brojac`), pa se SSCC ne ponavlja ni nakon restarta aplikacije

> Prefiks `3850123` u bazi je probni. Pravi GS1 prefiks dobija se od nadležne GS1 organizacije i mijenja se u tabeli `postavke` (ključ `gs1_prefiks`).

## Struktura projekta

```
src/main/java/
├── app/        Main, Launcher (pokretanje aplikacije)
├── model/      Partner, Artikal, Otpremnica, StavkaOtpremnice, Paleta
├── gs1/        Gs1Util (kontrolne cifre, SSCC), BarcodeService
├── repo/       Database (konekcija i šema)
├── service/    OtpremnicaService, EtiketaService
├── pdf/        EtiketaPdf, EtiketaPodaci, OtpremnicaPdf, OtpremnicaPodaci
└── ui/         MainController
src/main/resources/
├── fonts/      DejaVuSans.ttf, DejaVuSans-Bold.ttf
└── ui/         main-view.fxml
```

## Pokretanje

### 1. Preduslovi

- JDK 27 i IntelliJ IDEA (zadnja verzija)
- MySQL Server 8 i MySQL Workbench

### 2. Baza

U MySQL Workbenchu izvrši:

```sql
CREATE DATABASE otpremnice CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Zatim u `repo/Database.java` podesi korisnika i lozinku:

```java
private static final String USER = "root";
private static final String PASSWORD = "TVOJA_LOZINKA";
```

Tabele (`partner`, `artikal`, `otpremnica`, `paleta`, `stavka`, `postavke`) se kreiraju automatski pri prvom pokretanju.

### 3. Fontovi

Preuzmi [DejaVu Sans](https://dejavu-fonts.github.io/) i stavi `DejaVuSans.ttf` i `DejaVuSans-Bold.ttf` u `src/main/resources/fonts/`.

### 4. Pokretanje aplikacije

1. Otvori projekat u IntelliJ-u kao Maven projekat i uradi *Reload All Maven Projects*.
2. Pokreni klasu **`app.Launcher`** (ne `Main`, jer direktno pokretanje `Application` klase daje grešku o JavaFX runtimeu).
3. Upozorenja o *native access* pri pokretanju su bezazlena; po želji dodaj VM opciju `--enable-native-access=ALL-UNNAMED`.

## Korištenje

1. **Partneri:** dodaj pošiljaoca i primaoca.
2. **Artikli:** dodaj artikle (šifra, naziv, opcionalno GTIN).
3. **Otpremnice:** odaberi datum, pošiljaoca i primaoca pa *Kreiraj otpremnicu*.
4. Odaberi otpremnicu u tabeli, pa *Dodaj paletu* (dobija SSCC) i *Dodaj stavku* (artikal, količina, LOT, rok, paleta).
5. *PDF otpremnice* i *PDF etikete* spremaju dokumente u folder `izlaz/`.

## Šema baze

`partner`, `artikal`, `otpremnica`, `paleta`, `stavka`, `postavke`

Veze: `otpremnica` → `partner` (pošiljalac i primalac), `paleta` → `otpremnica`, `stavka` → `otpremnica`, `artikal`, `paleta`.

## Plan razvoja

- [x] GS1 jezgro (kontrolna cifra, SSCC)
- [x] GS1-128 barkod
- [x] Baza i generator SSCC-a
- [x] PDF etiketa 10x15 cm
- [x] PDF otpremnica (A4)
- [x] JavaFX korisničko sučelje
- [ ] Brisanje i izmjena partnera, artikala, stavki i otpremnica
- [ ] Validacija GTIN i GLN kontrolne cifre, GTIN (AI 01) u barkodu
- [ ] Automatsko otvaranje generisanih PDF-ova


## Autor

Kemal Beganović, Politehnički fakultet Univerziteta u Zenici
