#  Mini-Servidor WebSocket de Registre d'Esdeveniments de Joc amb Client Node.js 


## Què ens demanen

Crear un servidor amb **Node.js** que utilitzi **WebSockets** per:

- Permetre múltiples clients connectats simultàniament
- Rebre dades en format **JSON**
- Emmagatzemar cada lectura en **MongoDB**
- Afegir un **timestamp** a cada lectura

---

## 📄 Fitxa de Requisits

**Autor:** David Bargados Gómez
**Data:** 2026-04-07  

---

## 🎯 1. Objectiu Principal

Crear un servidor WebSocket bàsic en **Node.js** que escolti connexions entrants. El servidor rebrà missatges en format JSON simulant el moviment 2D d’un jugador en una partida des d'un client Node.js i els emmagatzemarà en una col·lecció de MongoDB
.
El sistema ha de:
- Emmagatzemar dades en **MongoDB**
- Afegir timestamps
- Detectar anomalies en temps real

---

## ⚙️ 2. Requisits Funcionals

| ID     | Descripció |
|--------|-----------|
| RF-01  | Servidor WebSocket en port configurable |

---

## 🛠️ 3. Requisits No Funcionals

| ID      | Tipus        | Descripció |
|---------|-------------|-----------|
| RNF-01  | Logging      | Logs amb **winston** (consola + fitxer) |
| RNF-02  | Mantenibilitat | Configuració flexible |
| RNF-03  | Fiabilitat   | Gestió d’errors |

---

## 4. Format del Missatge JSON

### Client → Servidor

```json
{
  "sensorId": "string",
  "type": "string",
  "value": number
}
