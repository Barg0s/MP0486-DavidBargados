# Mini-Servidor WebSocket de Registre d'Esdeveniments de Joc

## Què ens demanen

Crea un servidor Node.js que utilitzi WebSockets per rebre moviments simulats de jugadors en format JSON des de múltiples clients Node.js.

El servidor relaciona els moviments a una id d'una partida i emmagatzema les dades en una col·lecció de MongoDB. A més, gestionarà la durada de la partida en base a l’activitat del jugador.

---

## 1. Objectiu Principal

Desenvolupar un prototip funcional amb Node.js que actuï com a servidor WebSocket per rebre dades de moviment 2D d’un jugador des d’un client Node.js.

Cada moviment s’ha d’emmagatzemar a MongoDB amb:

- timestamp
- identificador de partida (gameId)

El sistema ha de:

- Detectar final de partida després de 10 segons d’inactivitat
- Calcular la distància en línia recta entre el punt inicial i final
- Retornar el resultat al client

A més, s’ha d’implementar logging amb **Winston** integrat amb **Grafana Loki**.

---

## 2. Requisits Funcionals

| ID    | Descripció                                                                                                         |
| ----- | ------------------------------------------------------------------------------------------------------------------- |
| RF-01 | El servidor WebSocket ha d'iniciar-se i escoltar connexions entrants en un port configurable                        |
| RF-02 | Els clients WebSocket han de poder connectar-se al servidor                                                         |
| RF-03 | Els clients han d’enviar dades de moviment en format JSON                                                          |
| RF-04 | El JSON ha d’incloure<br /> **posicio (x, y)** i **direccio (string)**                                 |
| RF-05 | El servidor ha de processar correctament els missatges JSON                                                         |
| RF-06 | El servidor ha de validar l'estructura dels missatges i descartar els invàlids registrant-los                      |
| RF-07 | El servidor ha d'afegir un timestamp UTC a cada missatge vàlid                                                     |
| RF-08 | Cada moviment s’ha d’emmagatzemar a MongoDB amb<br />**partidaId**, **posicio** i **timestamp** |
| RF-09 | El servidor ha de comprovar si el valor rebut està dins del rang normal definit                                    |
| RF-10 | Les anomalies detectades s’han de registrar amb logs específics                                                   |
| RF-11 | El servidor ha de gestionar desconnexions de clients i registrar-les                                                |
| RF-12 | Tots els moviments d’un jugador han de pertànyer a la mateixa partida                                             |
| RF-13 | Si no hi ha moviment durant 10 segons, la partida es considera finalitzada                                          |
| RF-14 | En finalitzar la partida, s’ha de calcular la distància en línia recta entre inici i final                       |
| RF-15 | El servidor ha d’enviar el resultat de la distància al client                                                     |

---

| ID      | MILLORA / REQUISIT NO FUNCIONAL                                                                                                                                                               |
| ------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| RF-04   | El camp 'direccio' accepta qualsevol string. Cal definir valors vàlids (ex: 'N','S','E','O' o 'nord','sud'...) i especificar els tipus de x i y (enter? float? rang?).                       |
| RF-09   | El servidor ha de comprovar si el valor rebut està dins del rang normal definit.                                                                                                             |
| RF-Nova | Comportament en reconexió — si un client es desconnecta i torna, es reprèn la partida o se n'inicia una de nova?                                                                           |
| RF-Nova | Generació del gameId — qui el genera, el client o el servidor? Quan?                                                                                                                        |
| RNF-01  | La integració amb Grafana Loki es menciona a l'apartat 1 però no apareix com a RNF. Cal afegir un RNF-04 específic amb la URL del endpoint, format d'etiquetes i entorn on és obligatori. |
| RNF-02  | Les variables d'entorn es mencionen però no es llisten. Caldria enumerar-les: PORT, MONGO_URI, etc.                                                                                          |
| RNF-03  | (Implícit) Rendiment: quants clients simultanis ha de suportar el servidor? Quin és el volum màxim de missatges/segon?                                                                     |
| RNF-04  | Seguretat: Cal WSS (WebSocket Secure/TLS)? Hi ha protecció contra flooding o missatges de gran mida?                                                                                         |
| RNF-05  | Testing: Cal cobertura de proves unitàries o d'integració? Hi ha un entorn de test separat?                                                                                                 |

## 3. Requisits No Funcionals

| ID     | Categoria      | Descripció                                                                                                                                                                           |
| ------ | -------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| RNF-01 | Logging        | Implementació de logs amb Winston. Es registraran esdeveniments clau (inici, connexió, desconnexió, errors, dades rebudes, dades guardades) a consola i fitxer**server.log** |
| RNF-02 | Mantenibilitat | El codi ha d’estar comentat i la configuració (port, URI MongoDB) ha de ser modificable via variables d’entorn o fitxer config                                                     |
| RNF-03 | Fiabilitat     | El servidor ha de gestionar errors comuns (ex: error de connexió a BD) sense aturar-se completament                                                                                  |

---

## 4. Format Missatge JSON (Servidor → Client)

```json
{
  "type": "connexio",
  "gameId": "string"
}
```

---

## 5. Format Missatge JSON (Client → Servidor)


```json
{
  "type": "moviment",
  "posicio": { "x": 0, "y": 0 },
  "direccio": "string"
}
```
`
## 6. Nova partida (Serv → Client)

```json
{
  "type": "novaPartida",
  "gameId": "string"
}

```
