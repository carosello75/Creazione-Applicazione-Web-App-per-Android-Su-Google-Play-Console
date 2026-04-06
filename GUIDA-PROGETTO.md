# Mini corso: come funziona questa base e dove si mette ogni cosa

Questa cartella è un’**app Android nativa** (Kotlin + layout XML). Non è un sito web dentro un’app, a meno che tu non aggiunga una **WebView**. Questa guida ti dice **dove** lavorare per stile, contenuti, eventuale HTML/CSS, backend, database.

---

## Parte 1 — Come si crea e si apre l’app

1. Installa **Android Studio** (versione recente).
2. **Apri** la cartella del progetto: `File → Open` e scegli la cartella `App base` (quella che contiene `settings.gradle.kts`).
3. Attendi il sync di Gradle; se manca l’SDK, Android Studio ti guida a scaricarlo.
4. Collega un telefono con **debug USB** oppure avvia un **emulatore**.
5. Clicca **Run** (triangolo verde): si installa l’APK di debug.
6. Per provare da terminale (dalla root del progetto):
   - `./gradlew assembleDebug` → genera l’APK debug.
   - `./gradlew bundleRelease` → genera l’**AAB** per Google Play (dopo aver configurato la firma release).

**Play Store:** serve `applicationId` univoco, versione (`versionCode` / `versionName`), icone, privacy policy se raccogli dati, e firma release in `app/build.gradle.kts` + keystore.

---

## Parte 2 — “HTML e CSS” in Android: cosa c’è davvero

| Concetto web | Equivalente in questa app nativa |
|--------------|-----------------------------------|
| HTML (struttura) | File XML in `app/src/main/res/layout/` (es. `activity_main.xml`, `fragment_placeholder.xml`) |
| CSS (colori, font, margini) | `res/values/themes.xml`, `colors.xml`, attributi sui widget XML (`android:padding`, `textAppearance`, ecc.) |
| JavaScript nel browser | **Non** c’è nel template attuale. In nativo usi **Kotlin** in `MainActivity.kt` e nei `*Fragment.kt`. |

Se vuoi **davvero** usare HTML + CSS + JS come una pagina web:

- Metti file in `app/src/main/assets/www/` (es. `index.html`, `style.css`, `app.js`).
- In un `Fragment` aggiungi una **WebView** che carica `file:///android_asset/www/index.html` oppure un URL `https://...`.
- Il **JS** in quella pagina gira **solo dentro la WebView**, non sostituisce Kotlin per il resto dell’app (menu nativo, permessi, notifiche restano Kotlin).

---

## Parte 3 — Dove modificare lo **stile** (aspetto)

| Cosa vuoi cambiare | Percorso tipico |
|--------------------|-----------------|
| Colori globali, menu selezionato | `app/src/main/res/values/colors.xml` + `res/color/bottom_nav_*_tint.xml` |
| Tema (Material, chiaro/scuro) | `app/src/main/res/values/themes.xml` |
| Disposizione schermata principale e barra in basso | `app/src/main/res/layout/activity_main.xml` |
| Contenuto di una “pagina” (tab) | Layout dedicato in `res/layout/` oppure il layout condiviso `fragment_placeholder.xml` |
| Testi traducibili | `app/src/main/res/values/strings.xml` |

**Menu in basso (voci, icone):** `res/menu/bottom_nav_menu.xml` + `res/navigation/mobile_navigation.xml` (gli `id` devono combaciare).

---

## Parte 4 — Dove sta la **logica** dell’app (il “JavaScript” del mondo Android)

| Ruolo | Dove |
|-------|------|
| Avvio app, collegamento menu ↔ schermate | `app/src/main/java/.../MainActivity.kt` |
| Comportamento di ogni tab | `app/src/main/java/.../ui/HomeFragment.kt`, `ContactFragment.kt`, ecc. |
| Classi riutilizzabili, API, utilità | Nuovi file `.kt` sotto `app/src/main/java/com/appbase/starter/` (es. cartella `data/`, `ui/`, `util/`) |

La lingua è **Kotlin** (simile concettualmente a “script lato app”, ma compilata).

---

## Parte 5 — **Backend** e **JavaScript**: dove “si inseriscono”

Il **backend** (server, API, database sul cloud) **non** vive dentro l’APK. L’app **chiama** il server via rete.

| Cosa | Dove nella app |
|------|----------------|
| URL dell’API, chiavi pubbliche (meglio non segreti) | `strings.xml`, `BuildConfig`, o file di config non committato |
| Chiamate HTTP (GET/POST JSON) | Kotlin: librerie tipo **Retrofit**, **OkHttp**, o `HttpURLConnection` |
| Logica che decide *cosa* mostrare dopo la risposta | Nei `Fragment` o in classi repository in Kotlin |

**JavaScript lato server** (Node, ecc.) sta sul **tuo server** o su **Firebase Cloud Functions** / analoghi: l’app Android non contiene quel codice, lo **invoca** solo tramite HTTP.

**JavaScript lato client** (nella WebView): dentro i file `.html/.js` in `assets` o sul sito che carichi; comunicazione con Kotlin possibile con **`@JavascriptInterface`** (bridge) se ti serve.

---

## Parte 6 — **Database**: dove si posiziona

### Dati **solo sul telefono** (offline, veloci)

| Strumento | Uso | Dove nel progetto |
|-----------|-----|-------------------|
| **Room** (SQLite) | Tabelle strutturate, query | Classi `@Entity`, `@Dao`, `Database` in `app/src/main/java/.../data/` + dipendenze Gradle |
| **DataStore** | Preferenze piccole (impostazioni) | Kotlin in `data/` o nei Fragment |
| File in `assets/` | HTML/CSS/JSON statici read-only | `app/src/main/assets/` |
| File interni app | Salvataggi custom | Codice Kotlin (`context.filesDir`, ecc.) |

### Dati **sul server** (multi-dispositivo, account)

- Il database vero (PostgreSQL, MySQL, MongoDB, ecc.) è sul **backend**.
- L’app Android ha solo il **client**: Kotlin che fa richieste REST/GraphQL e magari una **cache locale** (Room) se vuoi offline.

### **Firebase** (caso intermedio)

- **Firestore / Realtime Database**: configurazione `google-services.json`, SDK in Gradle, codice Kotlin nei Fragment o repository.
- Non è “un file database nella cartella progetto”: è servizio cloud collegato al tuo progetto Firebase.

---

## Parte 7 — Schema mentale (ordine consigliato)

1. **UI e navigazione** → layout XML + `mobile_navigation.xml` + Fragment.
2. **Stile** → `themes.xml`, `colors.xml`.
3. **Testi** → `strings.xml`.
4. **Dati locali** → Room / DataStore in `data/`.
5. **Dati remoti** → API documentata + Kotlin (Retrofit) + eventualmente modelli in `data/remote/`.
6. **Solo se ti serve il web nell’app** → `assets/www` + WebView in un Fragment.

---

## Riepilogo percorsi chiave

```
App base/
├── app/src/main/
│   ├── AndroidManifest.xml          # nome app, activity principale, permessi
│   ├── java/com/appbase/starter/    # Kotlin (logica)
│   ├── res/
│   │   ├── layout/                  # “HTML-like” XML
│   │   ├── values/                  # colori, stringhe, tema
│   │   ├── menu/                    # voci menu basso
│   │   ├── navigation/              # collegamento voci → schermate
│   │   └── drawable/                # icone vettoriali
│   └── assets/                      # (opzionale) www per WebView
├── app/build.gradle.kts             # dipendenze, versione, applicationId
└── GUIDA-PROGETTO.md                # questa guida
```

Tieni questo file nel repo: ogni nuova app può partire da questa base e questa mappa ti ricorda **dove** mettere stile, logica, web opzionale, API e database.
