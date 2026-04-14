  import { WebSocketServer } from "ws";
  import { MongoClient, ObjectId, Timestamp } from "mongodb";
  import winston from "winston";
  import LokiTransport from "winston-loki";
import { parse } from "path";
  //SERVER
  const uri = process.env.MONGODB_URI || 'mongodb://root:password@localhost:27017/';

  const RECONEXIO_MS = parseInt(process.env.RECONEXIO_MS || '3000', 10);
  const WS_PORT      = parseInt(process.env.WS_PORT       || '8000');
  const DB_NAME      = process.env.DB_NAME                || 'jocDB';
  const COLLECTION   = process.env.COLLECTION_NAME        || 'moviments';
  const LOKI_URL     = process.env.LOKI_URL               || 'http://localhost:3100';
  const INACTIVITY_MS = parseInt(process.env.INACTIVITY_TIMEOUT_MS || '10000');
  const wss = new WebSocketServer({ port: WS_PORT });

  const logger = winston.createLogger({
    level: "info",
    format: winston.format.json(),
    transports: [
      new winston.transports.Console(),
      new winston.transports.File({filename: "server.log"}),
      new LokiTransport({
        host: LOKI_URL,
        labels: { app: "node-server" }
      })
    ]
  });

  // Connexió MongoDB

  const client = new MongoClient(uri);


  let collection;

  function esMissatgeValid(data) {
    return (
      data &&
      data.type === "moviment" &&
      data.posicio &&
      typeof data.posicio.x === "number" &&
      typeof data.posicio.y === "number" &&
      typeof data.direccio === "string"
    );
  }

  async function connectDB() {
    try {
      await client.connect();

      const db = client.db(DB_NAME);
      collection = db.collection(COLLECTION);

      await collection.deleteMany({});

      logger.info("Connectat a MongoDB");
    } catch (error) {
      logger.error("Error connectant a MongoDB", {
        message: error.message
      });

      setTimeout(connectDB, RECONEXIO_MS);
    }
  }
  connectDB();
  wss.on('connection', (ws) => {
    logger.info("Client connectat");
    let partidaId = new ObjectId();
    let posicioInicial = null;
    let ultimaPosicio = null;
    let timeout;
    ws.send(JSON.stringify({
    type: "connexio",
    gameId: partidaId.toString()
    }));


    ws.on('message', async (message) => {
      try {
        const data = JSON.parse(message);
              if (!esMissatgeValid(data)) {
              logger.warn("Missatge invàlid rebut", { data });

        await collection.insertOne({
          type: "invalid",
          raw: data,
          timestamp: new Date()
        });

        return;
        }
        const posicioActual = data.posicio;
        if (posicioInicial === null){
          posicioInicial = posicioActual;
        }
        ultimaPosicio = posicioActual;


        const movement = {
          partidaId,
          x: data.posicio.x,
          y: data.posicio.y,
          direccio: data.direccio,
          timestamp: new Date()

        };

        await collection.insertOne(movement);

        logger.info("Moviment inserit", {x: data.posicio.x,y: data.posicio.y,partidaId: partidaId.toString()});

        clearTimeout(timeout);

        timeout = setTimeout(async () => {
          logger.info("Partida finalitzada")
          if (posicioInicial && ultimaPosicio){
            const dx = ultimaPosicio.x - posicioInicial.x;
            const dy = ultimaPosicio.y - posicioInicial.y;

            const distancia = Math.sqrt(dx * dx + dy * dy);
            const distanciaSave = {
              partidaId,
              distancia: distancia,
              timestamp: new Date()
            };

            logger.info("Distància calculada: " + distancia );
            const missatge = {
              type : "resultat",
              distancia : distancia,
              partidaId: partidaId.toString()
            }
            await collection.insertOne(distanciaSave);

            ws.send(JSON.stringify(missatge));
            posicioInicial = null;
            ultimaPosicio = null;
            partidaId = new ObjectId(); 
        }
            ws.send(JSON.stringify({
              type: "novaPartida",
              gameId: partidaId.toString()
            }));
        },INACTIVITY_MS);


      } catch (error) {
        logger.error("Error processant missatge", {
          error: error.message
        });
      }
    });

    ws.on('close', () => {
      clearTimeout(timeout);
      logger.info("Client desconnectat");
    });
  });

  logger.info("Servidor WebSocket a localhost:8000");
