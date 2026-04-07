import WebSocket from 'ws';

const ws = new WebSocket('ws://localhost:3000');

ws.on('open', () => {
  console.log('Conectado al servidor');

  ws.send('Hola desde el cliente');
});

ws.on('message', (data) => {
  console.log('Mensaje del servidor:', data.toString());
});

ws.on('close', () => {
  console.log('Conexión cerrada');
});



ws.on('error', (err) => {
  console.error('Error:', err);
});
