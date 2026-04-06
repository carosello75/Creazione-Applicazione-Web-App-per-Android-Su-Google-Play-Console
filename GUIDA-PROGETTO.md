# Mini corso: come funziona questa base e dove si mette ogni cosa

Questa cartella è un’**app Android nativa** (Kotlin + layout XML). Non è un sito web dentro un’app, a meno che tu non aggiunga una **WebView**. Questa guida ti dice **dove** lavorare per stile, contenuti, eventuale HTML/CSS, backend, database.

---

## Parte 1 — Come si crea e si apre l’app

### Dove scaricare e aggiornare Android Studio (sito ufficiale)

Vai sulla pagina ufficiale in italiano: **[Scarica Android Studio e strumenti per app](https://developer.android.com/studio?utm_source=android-studio&hl=it)**.

**Quando usarla:** per **scaricare** o **aggiornare** l’IDE, leggere i **requisiti di sistema**, consultare **note di rilascio** e guide introduttive (“Inizia a utilizzare Android Studio”, “Crea la tua prima app”). Non serve aprirla ogni giorno: per scrivere codice usi l’app **Android Studio** installata sul Mac; il sito è il punto di riferimento quando installi da zero, cambi computer o vuoi l’ultima versione stabile.

1. Installa **Android Studio** (versione recente) dal link sopra — su **Mac con chip Apple** scegli il `.dmg` **ARM**; su **Mac Intel** il `.dmg` **64 bit** (non ARM).
2. **Apri** la cartella del progetto: `File → Open` e scegli la cartella `App base` (quella che contiene `settings.gradle.kts`).
3. Attendi il sync di Gradle; se manca l’SDK, Android Studio ti guida a scaricarlo.
4. Collega un telefono con **debug USB** oppure avvia un **emulatore**.
5. Clicca **Run** (triangolo verde): si installa l’APK di debug.
6. Per provare da terminale (dalla root del progetto):
   - `./gradlew assembleDebug` → genera l’APK debug.
   - `./gradlew bundleRelease` → genera l’**AAB** per Google Play (dopo aver configurato la firma release).

**Play Store:** serve `applicationId` univoco, versione (`versionCode` / `versionName`), icone, privacy policy se raccogli dati, e firma release + keystore. **Procedura completa (bundle, Play Console, nuova app):** vedi **Parte 8** più sotto.

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
├── keystore.properties.example      # modello per firma release (copia → keystore.properties, non su Git)
└── GUIDA-PROGETTO.md                # questa guida (incl. Parte 9 Git/GitHub)
```

Tieni questo file nel repo: ogni nuova app può partire da questa base e questa mappa ti ricorda **dove** mettere stile, logica, web opzionale, API e database.

---

## Parte 8 — Bundle (AAB) per Google Play, firma release e Play Console

Google Play **non** vuole più l’APK “vecchio stile” per le nuove pubblicazioni: vuole l’**Android App Bundle**, file **`.aab`**. Dal bundle Google genera APK ottimizzati per ogni dispositivo.

### Link ufficiale Play Console

Apri la console qui (account Google sviluppatore, quota di registrazione una tantum):

**[https://play.google.com/console](https://play.google.com/console)**

---

### Creare una **nuova app** in Play Console (schema)

1. Accedi a **Play Console** con l’account sviluppatore.
2. Se è la prima volta, completa il profilo sviluppatore e accetta i contratti.
3. **Tutte le app** → **Crea app**.
4. Compila: **nome** mostrato sullo store, **lingua predefinita**, **tipo** (app / gioco), **gratis o a pagamento**.
5. Dichiara **norme** (content rating, privacy, annunci se presenti): la console ti guida con checklist fino a “Pronto per la revisione”.

Finché non carichi un primo **AAB** in una **traccia** (es. test interno), l’app resta in bozza: è normale.

---

### Cosa preparare **nel progetto** prima del bundle release

| Elemento | Dove / nota |
|----------|-------------|
| **ID applicazione univoco** | `app/build.gradle.kts` → `applicationId` (es. `it.tuonome.miaapp`). **Non** riutilizzare lo stesso ID di un’altra tua app se sono prodotti diversi. |
| **versionCode** | Intero che **deve aumentare** ad ogni caricamento su Play (1, 2, 3, …). |
| **versionName** | Stringa visibile agli utenti (es. `1.0`, `1.0.1`). |
| **Firma release** | Keystore **.jks** (o .keystore) + alias e password: **conservali in luogo sicuro**. Se li perdi, non potrai aggiornare la stessa app con lo stesso listing (in pratica è un disastro). |

---

### Due modi per generare l’**AAB** firmato

#### Metodo A — Android Studio (consigliato per iniziare)

1. **Build → Generate Signed App Bundle / APK…**
2. Scegli **Android App Bundle**.
3. Se non hai ancora un keystore: **Create new** e salva il file `.jks` fuori dal repo (o in cartella ignorata da Git), annota password e alias.
4. Firma in **release**, termina la procedura: Studio indica il percorso del file **`.aab`**.

#### Metodo B — Terminale (`./gradlew bundleRelease`)

Serve la **firma release** configurata in Gradle (tipicamente file `keystore.properties` in root, **non** committato: vedi `keystore.properties.example` nel repo). Dopo aver collegato keystore e password nel build come da [documentazione ufficiale sulla firma](https://developer.android.com/build/configure-app-signing), dalla root del progetto:

```bash
./gradlew bundleRelease
```

File generato (di solito):

`app/build/outputs/bundle/release/app-release.aab`

Quel **`.aab`** è ciò che carichi su Play Console.

---

### Creare un keystore da terminale (se non usi solo la finestra di Studio)

Esempio (sostituisci nomi e percorsi):

```bash
keytool -genkey -v -keystore upload-keystore.jks -keyalg RSA -keysize 2048 -validity 10000 -alias upload
```

Ti chiederà password e dati (nome, organizzazione, ecc.). **Backup** del file `.jks` e delle password in posto sicuro.

---

### Caricare il bundle su Play Console

1. **Play Console** → seleziona la **tua app**.
2. Vai su **Release** (Pubblicazione) → **Testing** (es. **Internal testing**) oppure **Production** quando sei pronto.
3. **Crea nuova release** → **Carica** il file **`.aab`**.
4. Compila note di release se richiesto → **Rivedi** → **Avvia rollout** (o invio a test).

Per la **prima** volta conviene spesso **test interno**: aggiungi email tester, installi l’app dal link che dà Play, verifichi che tutto funzioni prima della produzione.

---

### APK debug vs AAB release (chiarezza)

| Artefatto | Comando tipico | Uso |
|-----------|----------------|-----|
| APK **debug** | `./gradlew assembleDebug` | Prova sul telefono/emulatore in sviluppo. **Non** per Play Store. |
| **AAB release** | `./gradlew bundleRelease` (con firma) o wizard Studio | **Solo questo** (firmato) per Google Play. |

---

### Checklist veloce prima del “Vai live”

- [ ] `applicationId` definitivo e univoco  
- [ ] `versionCode` incrementato rispetto all’ultimo upload  
- [ ] Keystore e password al sicuro (copia di backup)  
- [ ] Scheda store: descrizione, screenshot, icona 512×512, grafica feature se richiesta  
- [ ] **Informativa sulla privacy** (URL) se l’app raccoglie dati o usa librerie che lo richiedono  
- [ ] Questionario **classificazione contenuti** completato nella console  

---

Per dettagli che cambiano nel tempo (policy Google, nuovi campi in console), usa sempre la **documentazione ufficiale** Play Console e Android Developers collegata dalla stessa console.

---

## Parte 9 — Git e GitHub: account, login, token e `git push`

Questa parte riassume **come tenere il progetto su GitHub** dal Mac (terminale): creazione account, repository, autenticazione (non si usa la password dell’account per `git push`), comandi usati ogni giorno.

### Creare un **nuovo account GitHub**

1. Vai su **[https://github.com/signup](https://github.com/signup)**.
2. Scegli **username**, **email**, **password** (quella del sito GitHub: serve per entrare nel browser, **non** per Git da terminale in modo diretto).
3. Verifica l’email se richiesto.

Dopo la registrazione puoi **creare un repository** vuoto: **[https://github.com/new](https://github.com/new)** → nome repo (es. `mia-app`) → **Create repository**. GitHub ti mostrerà i comandi suggeriti; in pratica ti servirà l’URL tipo `https://github.com/TUO_USER/mia-app.git`.

---

### Cosa installare sul Mac

- **Git** (spesso già presente). Verifica: `git --version`
- Opzionale: **[GitHub CLI](https://cli.github.com/)** (`gh`) per login guidato.

---

### Collegare la cartella del progetto a GitHub (prima volta)

Nella **root** del progetto (dove c’è `settings.gradle.kts`):

```bash
cd "/percorso/della/cartella/progetto"
git status
```

Se il progetto **non** è ancora un repo:

```bash
git init
git add -A
git commit -m "Primo commit"
```

Collega il **remote** (sostituisci `TUO_USER` e `NOME_REPO`):

```bash
git remote add origin https://github.com/TUO_USER/NOME_REPO.git
git branch -M main
git push -u origin main
```

Se `origin` esiste già ma punta altrove:

```bash
git remote set-url origin https://github.com/TUO_USER/NOME_REPO.git
```

Controlla:

```bash
git remote -v
```

---

### Login da terminale: **non** la password del sito

GitHub **non** accetta più la password dell’account per `git push` / `git pull` in HTTPS. Devi usare:

- un **Personal Access Token (PAT)** al posto della password, **oppure**
- **SSH** con chiave caricata su GitHub.

#### Token (consigliato per iniziare)

1. Nel browser, entra su GitHub con **l’account che deve pushare** sul repo.
2. Apri **[https://github.com/settings/tokens](https://github.com/settings/tokens)**.
3. **Generate new token** → **Generate new token (classic)**.
4. Spunta almeno **`repo`** (accesso ai repository).
5. **Generate** e **copia** il token (stringa lunga, spesso inizia con `ghp_`). Salvalo in un posto sicuro: non lo rivedi intero dopo.

Quando dal terminale fai `git push` e compare:

```text
Username for 'https://github.com':
Password for 'https://...':
```

- **Username:** il tuo **username GitHub** (es. `carosello75`).
- **Password:** incolla il **token** (non la password con cui entri su github.com). Mentre incolli non vedi caratteri: è normale.

Messaggio **`Invalid username or token. Password authentication is not supported`**: quasi sempre significa che hai messo la **password del sito** invece del **token**, oppure il token è sbagliato/scaduto.

#### Se Git usa sempre l’account **sbagliato** (es. 403 “denied to altro_utente”)

Su Mac le credenziali restano nel **Portachiavi**. Per far ripartire la richiesta utente/token:

```bash
printf "host=github.com\nprotocol=https\n\n" | git credential-osxkeychain erase
```

Poi di nuovo `git push` e inserisci **username** e **token** dell’account corretto.

In **Accesso Portachiavi** puoi anche cercare `github` e eliminare le voci obsolete.

---

### Comandi che userai spesso (dopo le modifiche al codice)

```bash
cd "/percorso/del/progetto"
git status
git add -A
git commit -m "Breve descrizione di cosa hai cambiato"
git push
```

- **`git status`** — cosa è modificato / in attesa di commit.  
- **`git add -A`** — mette in stage tutte le modifiche (o usa `git add file` per file singoli).  
- **`git commit -m "..."`** — crea uno snapshot locale con messaggio.  
- **`git push`** — invia i commit sul branch remoto (es. `main` su GitHub).

Se è il primo push del branch:

```bash
git push -u origin main
```

---

### Clonare il repo su un altro computer

```bash
git clone https://github.com/TUO_USER/NOME_REPO.git
cd NOME_REPO
```

Poi apri la cartella in Android Studio.

---

### Cose da **non** committare (già coperte da `.gitignore` in questo template)

- `local.properties` (percorso SDK sul tuo Mac)  
- cartelle `build/`, `.gradle/`  
- **`keystore.properties`**, file **`.jks` / `.keystore`**, token o password in chiaro  

Se per sbaglio committi un segreto, **revoca** il token su GitHub e ruota password/keystore secondo le procedure di sicurezza.

---

### Riepilogo errori comuni

| Messaggio | Cosa fare |
|-----------|-----------|
| `403` / `denied to ALTRO_UTENTE` | Stai autenticato come utente GitHub senza permesso su quel repo: cancella credenziali salvate, `git push` di nuovo con utente + token **corretti**, oppure aggiungi collaboratore sul repo. |
| `403` / `denied to STESSO_UTENTE` | Token senza permessi (es. fine-grained senza “Contents: Read and write”): ricrea token **classic** con **`repo`**. |
| `Invalid username or token` | Usa il **PAT** come password, non la password dell’account GitHub. |
| `remote: Repository not found` | URL sbagliato, repo privato senza accesso, o nome utente/repo errato. |

---

Link utili: [Iscrizione GitHub](https://github.com/signup) · [Nuovo repository](https://github.com/new) · [Token personali](https://github.com/settings/tokens) · [Documentazione Git](https://git-scm.com/doc)
