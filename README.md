# 🐾 PokeBattler – Java OOP Project (Inlämning 2)

Ett konsolbaserat Pokémon-stridsspel och hanteringssystem utvecklat i Java. Projektet bygger vidare på Pokédexet från inlämning 1 och utökar det med en robust, objektorienterad stridsmotor, flera spellägen (inklusive ett utmanande Raid-läge), persistent statistik och JSON-hantering.

---

## 🛠️ Arkitektur & Designval (OOP)

Projektet följer en strikt lagerindelning (Separation of Concerns) för att säkerställa god kodkvalitet, testbarhet och underhållsbarhet:
* model: Innehåller domänklasser och affärslogik (Pokemon, den abstrakta basklassen Attack, DamageAttack, StatusAttack, samt klasser för statistik och resultat).
* service: Hanterar spelets flöden och kärnlogik (BattleEngine, PokedexService, StatsService, samt seeders för startdata och raid-bossar).
* repository: Ansvarar för persistens och filhantering (FileManager, StatsRepository) med hjälp av Jackson-biblioteket.
* ui: Gränssnittslager för interaktion i konsolen (PokedexUI, BattleUI, StatsUI).
* exception: Egna anpassade runtime-exceptions (InvalidPokemonException, InputCancelledException, PokemonNotFoundException).

### Polymorfism och arv
För att uppfylla kraven på polymorfism är Attack en abstrakt basklass som implementerar interfacet BattleAction. Subklasserna DamageAttack och StatusAttack implementerar den abstrakta metoden execute(Pokemon attacker, Pokemon defender) polymorft. Detta gör att BattleEngine kan exekvera attacker enhetligt utan att behöva känna till den underliggande subtypen.

---

## 🎮 Funktioner och Spellägen

1. Pokédex-hantering: Lägg till, redigera, ta bort och sök efter Pokémon med fullständig validering av stats (HP, försvar, snabbhet) och attacker.
2. Dynamisk Stat-varians (PokemonDataSeeder): Alla Pokémon som skapas, fångas eller möts som CPU-motståndare får en slumpmässig men balanserad variansfaktor (mellan 90% och 110% av basvärdet) på sina stats och attacker. Värdena skyddas av strikta gränser via Math.clamp, vilket gör varje Pokémon helt unik.
3. Stridslägen:
    * 1v1 Strid: Klassisk duell mellan två Pokémon.
    * 3v3 Lagstrid: Lagbaserad strid där Pokémon byts ut automatiskt när de svimmar.
    * RAID-läge (5v1): Ett svårare spelläge där spelaren väljer upp till 5 Pokémon från sitt lag för att gemensamt besegra en enskild, mäktig Raid-boss (t.ex. Mega Mewtwo) med höga stats.
4. Stridssystem & Mekanik:
    * Turordning: Bestäms dynamiskt av Pokémonens speed (snabbast börjar).
    * Statusattacker: T.ex. Screech sänker motståndarens försvar (begränsat till en gång per Pokémon för att undvika oändlig stackning).
    * Stat-återställning: Tillfälliga stat-ändringar nollställs automatiskt efter avslutad strid via resetBattleStats().

---

## 🧮 Skadeformel

Skadan för en DamageAttack beräknas med följande formel:

rawDamage = (baseDamage - (defense / 2)) * typeMultiplier * critMultiplier
finalDamage = max(1, rawDamage)

* baseDamage: Attackens grundskada (mellan 5 och 250).
* defense: Försvararens aktuella försvar i striden (delas med 2 för att dämpa skada).
* typeMultiplier: Hämtas från typeffektivitetstabellen (t.ex. 2.0x vid super effektivt, 0.5x vid inte så effektivt, 1.0x neutralt).
* critMultiplier: 10% chans till kritisk träff vilket ger 1.5x i skada.
* Minsta skada: En attack gör alltid minst 1 skada om den träffar.

---

## 📊 VG-krav som uppfyllts

1. JSON-persistens via Jackson: Alla Pokémon och stridsresultat sparas automatiskt i pokebattler.json och battle_stats.json. Innehåller även robust felhantering och automatisk backup (.bak) vid korrupta filer.
2. Typeffektivitet: Komplett tabell som hanterar relationer mellan Fire, Water, Grass, Electric, Normal med flera.
3. Kritiska träffar: Slumpmässig chans (10%) med multiplikator som tydligt loggas i striden.
4. Persistent statistik: Sparar totala vinster/förluster, vinstprocent, mest använda attack, mest använda Pokémon samt individuell vinst/förlust-ratio per Pokémon. Visas via menyn.
5. Polymorfism: Användning av abstrakt Attack-hierarki med polymorfa anrop.

---
## 💻 Krav för att köra applikationen
* **Java SDK:** Utvecklad och testad med Java 21
* **Byggverktyg:** Maven
* **Beroenden:** `com.fasterxml.jackson.core:jackson-databind`

---

## ⚠️ Kända begränsningar
* Gränssnittet är textbaserat (konsolapplikation).
* Raid-bossar har fasta fördefinierade attacker och kan inte fångas av spelaren till sitt eget Pokédex.