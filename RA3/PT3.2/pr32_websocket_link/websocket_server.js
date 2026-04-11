import { WebSocketServer } from "ws";
import { MongoClient } from "mongodb";
import winston from "winston";
import LokiTransport from "winston-loki";

const wss = new WebSocketServer({ port: 8000 });

const logger = winston.createLogger({
  level: "info",
  format: winston.format.json(),
  transports: [
    new LokiTransport({
      host: "http://localhost:3100",
      labels: { app: "node-server" }
    })
  ]
});

// Connexió MongoDB
const uri = process.env.MONGODB_URI || 'mongodb://root:password@localhost:27017/';
const client = new MongoClient(uri);

let collection;

async function connectDB() {
  await client.connect();
  const db = client.db("jocDB");
  collection = db.collection("moviments");
  await collection.deleteMany({});

  logger.info("Connectat a MongoDB");
}

connectDB();

wss.on('connection', (ws) => {
  logger.info("Client connectat");

  ws.on('message', async (message) => {
    try {
      const data = JSON.parse(message);

      const movement = {
        x: data.posicio.x,
        y: data.posicio.y,
        direccio: data.direccio
      };

      await collection.insertOne(movement);

      logger.info("Moviment guardat", movement);

    } catch (error) {
      logger.error("Error processant missatge", {
        error: error.message
      });
    }
  });

  ws.on('close', () => {
    logger.info("Client desconnectat");
  });
});

logger.info("Servidor WebSocket a localhost:8000");
