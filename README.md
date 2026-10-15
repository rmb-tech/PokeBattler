# 🐾 PokeBattler – Pokémon Battler i Java

Ett konsolbaserat Pokémon-spel där man samlar Pokémon i ett Pokédex och slåss turbaserat mot datorstyrda motståndare. Projektet är **Inlämning 2** i kursen *Programmering med Java, grund (JAVA26S)* och bygger vidare på Pokédexet från Inlämning 1. Pokédexet har refaktorerats mot en tydlig OOP-arkitektur och utökats med stridsmotor, typeffektivitet, kritiska träffar, ett Raid-läge och persistent statistik.

**Författare:** Rickard Malm

---

## 📑 Innehåll

1. [Funktioner](#-funktioner)
2. [Kom igång](#-kom-igång)
3. [Projektstruktur](#-projektstruktur)
4. [OOP-val och designmotiveringar](#-oop-val-och-designmotiveringar)
5. [Stridssystemet](#-stridssystemet)
6. [Persistens (JSON)](#-persistens-json)
7. [Statistik](#-statistik)
8. [Felhantering och kraschtest](#-felhantering-och-kraschtest)
9. [Uppfyllda VG-krav](#-uppfyllda-vg-krav)
10. [Kända begränsningar / TODO's](#-kända-begränsningar--todos-om-tid-finnes)

---

## 🎮 Funktioner

### Pokédex (från Inlämning 1, bevarat och förbättrat)
* Visa, lägga till, redigera, ta bort och söka Pokémon.
* Redigera namn, typ, Max HP, försvar, snabbhet samt lägga till, ändra och ta bort attacker (1–4 per Pokémon).
* Fånga en slumpad Pokémon, läka alla Pokémon och återställa till seedat läge.
* Stat-varians: varje Pokémon som fångas, seedas eller möts som vild får en slumpad faktor mellan 0.80 och 1.20. Samma faktor skalar HP, försvar, snabbhet och attackstyrka (inom giltiga gränser via Math.clamp). 
* IV: Variantfaktorn blir IV/Potential 0–100 % (0 % = ×0.80, 100 % = ×1.20) som visas som (IV 87%). Manuellt skapade Pokémon har ingen faktor och får IV 0 %.
* Fullständig validering av alla fält vid skapande och redigering.

### Strid (nytt i Inlämning 2)
* **1v1 Strid:** En av dina Pokémon mot en vild Pokémon.
* **3v3 Lagstrid:** Tre mot tre, nästa Pokémon skickas ut automatiskt när en svimmar.
* **RAID (5v1):** Fem av dina Pokémon mot en enda stark Raid-boss.
* Turordning efter snabbhet. 
* Träffsäkerhet, typeffektivitet, kritiska träffar och statusattacker.
* Tydlig battle-logg för varje runda samt slutresultat.

### Statistik
* Totala vinster och förluster, vinstprocent, resultat per stridstyp, vinst/förlust per Pokémon samt mest använda Pokémon och attack. Sparas mellan sessioner.
* Unika Pokémon-instanser (UUID): Varje enskild Pokémon tilldelas ett unikt ID vid skapandet. Statistiken sparas per Pokémon-id `(PokemonRecord)` i stället för på namn.

### Menyer

| Huvudmeny | Pokédex-meny |
| :--- | :--- |
| 1. Pokédex | 1. Visa alla Pokémon |
| 2. Fånga ny slumpad Pokémon | 2. Lägg till ny Pokémon |
| 3. Starta strid | 3. Redigera Pokémon |
| 4. Stridsstatistik | 4. Ta bort Pokémon |
| 5. Återställ till seedat läge | 5. Sök Pokémon |
| 6. Spara till fil | 6. Läk alla Pokémon |
| 7. Avsluta (sparar automatiskt) | 7. Tillbaka |

---

## 🚀 Kom igång

**Krav:** Java 21 och Maven. Beroenden hämtas automatiskt av Maven (Jackson för JSON).

### Klona och kör

```bash
git clone <repo-url>
cd PokeBattler
```

* **I IntelliJ:** öppna projektmappen och kör `se.rmbtech.pokebattler.Main`.
* **Med Maven från terminalen** (kör i projektets rotmapp, där `pom.xml` ligger):
  ```bash
  mvn compile exec:java "-Dexec.mainClass=se.rmbtech.pokebattler.Main"
  ```

### Bra att veta
* Datafilerna `pokebattler.json` och `battle-stats.json` skapas automatiskt i den mapp programmet körs från. Första gången (ingen sparfil) genereras sex slumpade Pokémon.
* Konsolen använder emojis. Terminalen bör stödja UTF-8 (IntelliJ:s Run-fönster fungerar. I Windows cmd kan du köra `chcp 65001` först).

### Exempel på användning

```text
=== HUVUDMENY ===
3. ⚔️ Starta Strid
Val: 3
1) 1v1 Strid   2) 3v3 Lagstrid   3) RAID (5v1 mot Raid-boss)
Välj typ av strid: 1
Välj Pokémon #1 (#): 2

--- DIN TUR (Charmander) ---
1) Ember (Skada: 45, Träff: 100%, Typ: FIRE)
Välj attack: 1
Charmander använder Ember!
💥 Träff! Bulbasaur tog 32 skada! (HP: 93/125) 🔥 Super effektivt!
```

---

## 📁 Projektstruktur

```text
se.rmbtech.pokebattler
├── Main
├── exception/
│   ├── InputCancelledException
│   ├── InvalidPokemonException
│   └── PokemonNotFoundException
├── model/
│   ├── Attack (abstrakt)
│   ├── BattleAction (interface)
│   ├── BattleMode
│   ├── BattleResult (record)
│   ├── BattleStats
│   ├── DamageAttack
│   ├── Pokemon
│   ├── PokemonRecord
│   ├── PokemonType
│   ├── StatusAttack
│   └── WinLossRecord
├── repository/
│   ├── JsonPokemonRepository
│   ├── PokemonRepository (interface)
│   └── StatsRepository
├── service/
│   ├── AttackChooser (interface)
│   ├── BattleEngine
│   ├── CpuAttackChooser
│   ├── PokedexService
│   ├── PokemonDataSeeder
│   ├── RaidDataSeeder
│   └── StatsService
├── ui/
│   ├── BattleUI
│   ├── PlayerAttackChooser
│   ├── PokedexUI
│   └── StatsUI
└── util/
    ├── FileUtils
    ├── InputHelper
    └── UIHelper
```

**Principen är Separation of Concerns:** `model` känner inte till konsolen eller filer, `service` innehåller spelreglerna, `repository` sköter filerna och `ui` pratar med användaren.

---

## 🛠️ OOP-val och designmotiveringar

### Arv och polymorfism: attackerna

```text
BattleAction (interface)
      ▲  implements
Attack (abstrakt basklass)  ── delad data + validering: namn, träffsäkerhet, typ
      ▲  extends             ▲  extends
DamageAttack                 StatusAttack
(gör skada)                  (sänker försvar)
```

* **`BattleAction`** beskriver bara att förmågan "kan utföras" (`execute`).
* **`Attack`** är en **abstrakt klass** eftersom alla attacker delar data och validering (namn, träffsäkerhet, typ). Ett interface kan inte bära det delade tillståndet.
* **Polymorfa metoder:** Varje subklass bestämmer själv sitt beteende, och ingen kod utanför attackklasserna behöver veta vilken subtyp det rör sig om (inga `instanceof`-kedjor):

| Metod | `DamageAttack` | `StatusAttack` |
| :--- | :--- | :--- |
| `execute(attacker, defender)` | räknar ut och tillför skada | sänker motståndarens försvar |
| `isUsefulAgainst(defender)` | alltid `true` | `false` om försvaret redan är sänkt |
| `effectSummary()` | `Skada: 90` | `Sänker försvar: -15` |
| `scaledCopy(scaler)` | skalar basskadan (stat-varians vid seedning) | skalar försvarssänkningen |
| `updatePower(n)` | sätter basskadan | sätter försvarssänkningen |



### Strategimönster: vem väljer attack?

`AttackChooser` är ett interface med två implementationer:
* **`PlayerAttackChooser`** (UI-lagret) visar en meny och läser spelarens val.
* **`CpuAttackChooser`** (service-lagret) väljer slumpmässigt bland de attacker som är användbara.



### Inkapsling och robusthet

* **Privata fält & validerande settrar:** Alla fält är `private` med validerande settrar som kastar `InvalidPokemonException` direkt (*fail fast*). Ett `Pokemon`- eller `Attack`-objekt kan därför inte hamna i ett ogiltigt tillstånd.
* **Defensiva kopior:** `Pokemon.getAttacks()` och `PokedexService.getPokemons()` returnerar kopior, och `setAttacks()` kopierar listan in. 
* **Regler på ett ställe:** Gränsvärden finns som konstanter (`Pokemon.MAX_HP`, `DamageAttack.MIN_DAMAGE` med flera) och används av både validering och menyer.
* **Stridsstatus vs permanenta värden:** `defense` är värdet under strid och `baseDefense` är det permanenta. Efter varje strid återställs försvaret i `finally`.

### Övriga val

* **`enum PokemonType`** innehåller typeffektivitetstabellen. **`enum BattleMode`** bär sin egen data (etikett, lagstorlek) i stället för `if`-kedjor i gränssnittet.
* **`record BattleResult`** är ett oföränderligt resultatobjekt. Motorn *returnerar* resultatet och statistiken sparas av `StatsService`, så motorn behöver inte känna till statistik eller filer.
* **Egna exceptions:** `InvalidPokemonException` (ogiltig data), `PokemonNotFoundException` (ogiltigt index) och `InputCancelledException` (input avbröts, t.ex. Ctrl+D).
* **Jackson med polymorfism:** `@JsonTypeInfo`/`@JsonSubTypes` på `Attack` gör att rätt subklass läses in från JSON.

---

## ⚔️ Stridssystemet

### Turordning

Pokémon med högst **snabbhet** attackerar först i varje runda. Vid lika snabbhet avgörs det slumpmässigt (slumpas varje runda).

### Träffsäkerhet

En attack träffar med sannolikheten `träffsäkerhet`, så en attack med 90 träffar i 90 % av fallen. Missar den skrivs det i loggen.

### Skadeformel

```text
blockedDamage = defense / 2  
rawDamage     = (baseDamage - blockedDamage) × typeMultiplier × critMultiplier
finalDamage   = max(1, round(rawDamage))
```

| Del | Förklaring |
| :--- | :--- |
| `baseDamage` | Attackens grundskada (5–250) |
| `defense` | Försvararens aktuella försvar, vilket kan vara sänkt av en statusattack |
| `typeMultiplier` | Från typeffektivitetstabellen (se nedan) |
| `critMultiplier` | 10 % chans för kritisk träff vid träff, då 1.5×, annars 1.0× |
| `max(1, …)` | En träff gör alltid minst 1 skada |

**Exempel:** Thunderbolt (`baseDamage` 90) mot en Pokémon med försvar 55 av typ Water, utan kritiskträff:
`(90 − 27) × 1.6 = 100.8`, vilket avrundas till **101 skada**.

Slumpfaktorerna i varje attack är alltså träff/miss och kritisk träff. Kritiska träffar visas tydligt i loggen (`🎯 KRITISK TRÄFF!`).

### Typeffektivitet (värden från Pokémon GO)

Alla 18 typer ingår i tabellen i `PokemonType`. Multiplikatorerna har hämtats från **Pokémon GO**:

| Effekt | Multiplikator |
| :--- | :--- |
| Super effektivt | **1.6×** |
| Neutralt | 1.0× |
| Inte så effektivt | **0.625×** |
| Dubbel resistens (ersätter immunitet) | **0.390625×** |

Normal är neutral (1×) mot alla typer **utom** Rock och Steel (0.625×) och Ghost (0.390625×), vilket är Pokémon GO:s officiella värden.

### Statusattacker

En statusattack (t.ex. *Screech*) sänker motståndarens försvar. Effekten kan bara läggas på **en gång per strid och Pokémon** (för att förhindra oändlig stackning). CPUn undviker att välja en statusattack som inte skulle göra något. Efter striden återställs försvaret.

### Dödläge

En duell får vara högst **30 rundor**. Om ingen svimmat efter det räknas spelarens Pokémon som besegrad.

### RAID-läge

Spelaren väljer **exakt 5** stridsdugliga Pokémon som möter en slumpad Raid-boss (MEGA Mewtwo, MEGA Charizard eller MEGA Tyranitar) med mycket höga stats. Bossen har fasta attacker och ingen stats-varians. Dess HP **bevaras mellan duellerna**, så laget slåss tillsammans mot samma boss. Striden är vunnen när bossen svimmat och förlorad när alla fem svimmat.

---

## 💾 Persistens (JSON)

All persistens sker med **Jackson**, med två filer:

| Fil | Innehåll |
| :--- | :--- |
| `pokebattler.json` | Alla dina Pokémon med attacker (attackens subtyp sparas som `attackType`: `damage`/`status`) |
| `battle-stats.json` | Statistik: totalsiffror, per Pokémon, per attack och per stridstyp |

**Förenklat exempel:**

```json
{
  "name" : "Pikachu",
  "type" : "ELECTRIC",
  "maxHp" : 112,
  "currentHp" : 112,
  "attacks" : [ { "attackType" : "damage", "name" : "Thunderbolt", "baseDamage" : 91 } ]
}
```

Sparas automatiskt efter ändringar i Pokédexet, efter varje strid, vid manuellt spar-val och när programmet avslutas (även vid avbruten input).

### Tålighet mot dåliga filer

* **Saknad fil:** Pokémon seedas på nytt (6 stycken) och statistiken startar tom.
* **Korrupt eller oläsbar fil:** Filen döps om till `<filnamn>.bak`, ett felmeddelande visas och programmet startar med nya data.
* **Ofullständiga Pokémon** (t.ex. utan typ eller attacker) hoppas över med en varning.

---

## 📊 Statistik

Efter varje **avslutad** strid sparas resultatet (avbrutna strider räknas inte). Menyval **4. Stridsstatistik** visar:
* Totalt antal strider, vinster, förluster och vinstprocent.
* Fördelning per stridstyp (1v1 / 3v3 / RAID).
* Mest använda Pokémon (flest dueller) och mest använda attack (flest gånger vald av spelaren).
* Vinst/förlust och vinstprocent per Pokémon.

**Definitioner:** En *duell* är en av dina Pokémon mot en motståndare. En strid består av en eller flera dueller. Statistik per Pokémon räknas per duell. Vid lika antal i "mest använd" väljs den som kommer först i alfabetet. Utan tidigare strider visas ett vänligt meddelande.

---

## 🛡️ Felhantering och kraschtest

| Scenario | Hantering |
| :--- | :--- |
| Bokstäver där siffror förväntas | `InputHelper` fångar felet och frågar igen |
| Tom rad / bara mellanslag | Avvisas, frågar igen |
| Menyval som inte finns (0, -1, 999, "abc") | Alla menyer har intervallkontroll |
| Extremt långa strängar | Hanteras som vanlig text, inga krascher |
| Sök efter Pokémon som inte finns | Tydligt meddelande |
| Index utanför listan (redigera/ta bort) | Intervall i menyn samt `PokemonNotFoundException` |
| Negativ/0 HP, tomt namn, mellanslag som namn | Avvisas både i inmatning och i domänmodellens settrar |
| Starta strid utan Pokémon | Meddelande, tillbaka till menyn |
| Starta strid med Pokémon utan attacker eller med 0 HP | Sådana Pokémon filtreras bort från urvalet |
| Ogiltigt attackindex (-1, 99, "abc") | Intervallkontroll i attackmenyn |
| Korrupt JSON-fil | Flyttas till `.bak`, nya data skapas |
| Saknad JSON-fil | Pokémon seedas, statistik startar tom |
| Avbruten input mitt i en strid (Ctrl+D / Ctrl+Z) | `InputCancelledException`: Stridsstatus återställs, data sparas, programmet avslutas snyggt |
| Visa statistik utan tidigare strider | "Inga strider har spelats än" |

---

## ⭐ Uppfyllda VG-krav

1. **JSON som persistensformat med Jackson:** All persistens (Pokémon och statistik) sker i JSON.
2. **Typeffektivitetstabell** med alla 18 typer, med Pokémon GO:s multiplikatorer (se ovan).
3. **Kritiska träffar:** 10 % chans, 1.5× skada, visas i loggen.
4. **Persistent statistik:** Vinster/förluster, vinst/förlust per Pokémon, mest använda attack och Pokémon, visas via menyval.
5. **Polymorfism och välmotiverad OOP-arkitektur:** Abstrakt `Attack`-hierarki, interfacet `BattleAction`, strategimönstret `AttackChooser`, se OOP-val.
6. **Extra:** Raid-läge (5v1) och statistik per stridstyp.

---

## ⚠️ Kända begränsningar / Todo's om tid finnes

* Battle-loggen skrivs direkt till konsolen från `Attack.execute` och `BattleEngine`. I en vidareutveckling skulle ett lyssnar-/händelsegränssnitt skilja loggningen från spellogiken och göra den lättare att testa.
* Slumpen (`Random`) är inte injicerbar i attackklasserna, vilket gör exakta skadevärden svåra att enhetstesta. Inga automatiska enhetstester är skrivna ännu.
* Försvaret dras av som ett fast belopp (`försvar / 2`). Mycket svaga attacker mot Pokémon med högt försvar gör därför bara 1 skada.
* Raid-läget kräver exakt 5 stridsdugliga Pokémon. Raid-bossar har fasta attacker och kan inte fångas. 
* 5 RAID bossar är implementerade och man kan inte välja vilken man möter.  
* Manuellt skapade Pokemon får en IV på 0% 
* Pokémon läker inte automatiskt mellan strider, utan man läker via Pokédex-menyn.
* Nivå- och erfarenhetsmekanik (Level-up) är ännu inte implementerat.
* Datafilerna sparas relativt arbetsmappen, så programmet bör startas från projektets rotmapp.
* Gränssnittet är textbaserat och förutsätter en terminal med UTF-8-stöd för emojis.