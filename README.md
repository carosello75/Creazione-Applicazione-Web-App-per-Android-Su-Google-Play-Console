# Creazione applicazione Android per Google Play Console

Repository di **base** per un’app Android nativa (Kotlin): menu in basso (Home, Contatti, Strumenti, Info, Altro), tema Material e struttura pronta da clonare per ogni nuovo progetto Play Store.

**Repository:** [carosello75/Creazione-Applicazione-Web-App-per-Android-Su-Google-Play-Console](https://github.com/carosello75/Creazione-Applicazione-Web-App-per-Android-Su-Google-Play-Console)

---

## Corso rapido (indice)

| Argomento | Dove approfondire |
|-----------|-------------------|
| Come aprire, compilare e pubblicare | [GUIDA-PROGETTO.md](GUIDA-PROGETTO.md) — Parte 1 |
| HTML/CSS vs XML/temi Android | [GUIDA-PROGETTO.md](GUIDA-PROGETTO.md) — Parte 2 |
| Stile: colori, tema, layout | [GUIDA-PROGETTO.md](GUIDA-PROGETTO.md) — Parte 3 |
| Logica app (Kotlin) | [GUIDA-PROGETTO.md](GUIDA-PROGETTO.md) — Parte 4 |
| Backend, API, JavaScript | [GUIDA-PROGETTO.md](GUIDA-PROGETTO.md) — Parte 5 |
| Database locale e cloud | [GUIDA-PROGETTO.md](GUIDA-PROGETTO.md) — Parte 6 |
| Schema cartelle e ordine di lavoro | [GUIDA-PROGETTO.md](GUIDA-PROGETTO.md) — Parte 7 e riepilogo |
| **AAB / bundle, firma, Play Console, nuova app** | [GUIDA-PROGETTO.md](GUIDA-PROGETTO.md) — **Parte 8** |
| **Git / GitHub: account, token, login, `git push`** | [GUIDA-PROGETTO.md](GUIDA-PROGETTO.md) — **Parte 9** |

Il file **[GUIDA-PROGETTO.md](GUIDA-PROGETTO.md)** è il **corso completo** testuale: tienilo come riferimento mentre modifichi il progetto. Per pubblicare: **Parte 8** + file esempio **`keystore.properties.example`** (non committare `keystore.properties` né il `.jks`). Per salvare il codice su GitHub: **Parte 9**.

---

## Avvio in 3 passi

1. **Clona** il repository (o scarica ZIP da GitHub).
2. Apri la cartella in **Android Studio** (`File → Open`, scegli la root dove c’è `settings.gradle.kts`).
3. **Run** su emulatore o dispositivo con USB debugging.

Da terminale, nella root del progetto:

```bash
./gradlew assembleDebug
```

L’APK debug si trova in `app/build/outputs/apk/debug/`.

---

## File chiave da ricordare

| Obiettivo | File |
|-----------|------|
| Nome app, testi voci menu | `app/src/main/res/values/strings.xml` |
| Menu basso | `app/src/main/res/menu/bottom_nav_menu.xml` |
| Collegamento voci → schermate | `app/src/main/res/navigation/mobile_navigation.xml` |
| Colori e tema | `res/values/colors.xml`, `themes.xml` |
| Schermata principale | `res/layout/activity_main.xml` |
| Logica per tab | `app/src/main/java/com/appbase/starter/ui/*Fragment.kt` |
| ID pacchetto e versione Play Store | `app/build.gradle.kts` (`applicationId`, `versionCode`, `versionName`) |

---

## Google Play Console e bundle (AAB)

- Guida passo-passo: **[GUIDA-PROGETTO.md — Parte 8](GUIDA-PROGETTO.md#parte-8--bundle-aab-per-google-play-firma-release-e-play-console)** (nuova app in console, generazione **`.aab`**, firma, caricamento release).
- Console: [https://play.google.com/console](https://play.google.com/console)
- Cambia **`applicationId`** e incrementa **`versionCode`** prima di ogni upload.
- Esempio configurazione segreti firma (solo locale): **`keystore.properties.example`** → copia come `keystore.properties` (è in `.gitignore`).

---

## Licenza e uso

Usa questo progetto come template per le tue app; adatta nome pacchetto, risorse e policy secondo le regole del Play Store.
