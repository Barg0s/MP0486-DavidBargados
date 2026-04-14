  import { WebSocketServer } from "ws";
  import { MongoClient, ObjectId, Timestamp } from "mongodb";
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
    let posicioInicial = null;
    let ultimaPosicio = null;
    let timeout;


    ws.on('message', async (message) => {
      try {
        const data = JSON.parse(message);
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

          logger.info("Moviment insertat en X: " + data.posicio.x + " Y: " + data.posicio.y)

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
              distancia : distancia
            }
            await collection.insertOne(distanciaSave);

            ws.send(JSON.stringify(missatge));
            posicioInicial = null;
            ultimaPosicio = null;
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
