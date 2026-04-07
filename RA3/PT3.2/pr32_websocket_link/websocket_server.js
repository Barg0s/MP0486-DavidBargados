import { WebSocketServer } from 'ws'


const wss = new WebSocketServer({ port: 3000 });

wss.on('connection', function connection(ws) {
  console.log('Cliente conectado');

  ws.on('message', function incoming(message) {
    const text = message.toString();
    console.log('Mensaje recibido: %s', text);
    ws.send('Mensaje recibido por el servidor: ' + text);
  });

  ws.on('error', (err) => {
    console.error('Error en WebSocket:', err);
  });



  ws.on('close', function close() {
    console.log('Cliente desconectado');
  });
});

console.log('Servidor WebSocket iniciado en ws://localhost:3000')