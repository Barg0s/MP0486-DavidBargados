  import { WebSocketServer } from "ws";
  import { MongoClient, ObjectId } from "mongodb";
  import winston from "winston";
  import LokiTransport from "winston-loki";
  //SERVER

  const wss = new WebSocketServer({ port: 8000 });

  const logger = winston.createLogger({
    level: "info",
    format: winston.format.json(),
    transports: [
      new winston.transports.Console(),
      new winston.transports.File({filename: "server.log"}),
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
    let partidaId = new ObjectId();
    let startPos = null;
    let lastPos = null;
    let timeout;


    ws.on('message', async (message) => {
      try {
        const data = JSON.parse(message);
        const posicioActual = data.posicio;
        if (startPos === null){
          startPos = posicioActual;
        }
        lastPos = posicioActual;


        const movement = {
          partidaId,
          x: data.posicio.x,
          y: data.posicio.y,
          direccio: data.direccio
        };

        await collection.insertOne(movement);
        logger.info("Moviment guardat", {
          partidaId: partidaId.toString(),
          x: data.posicio.x,
          y: data.posicio.y,
          direccio: data.direccio
        });

        clearTimeout(timeout);

        timeout = setTimeout(() => {
          logger.info("Partida finalitzada")
          if (startPos && lastPos){
            const dx = lastPos.x - startPos.x;
            const dy = lastPos.y - startPos.y;

            const distancia = Math.sqrt(dx * dx + dy * dy);
            logger.info("Distància calculada", { distancia });
            const missatge = {
              type : "resultat",
              distancia : distancia
            }
            ws.send(JSON.stringify(missatge));
            startPos = null;
            lastPos = null;
            partidaId = new ObjectId(); 
        }
        },10000);


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
