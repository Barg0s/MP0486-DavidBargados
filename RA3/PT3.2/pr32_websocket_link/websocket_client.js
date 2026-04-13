import WebSocket from 'ws';
import readline from 'readline';
//CLIENT
const ws = new WebSocket('ws://localhost:8000');

let pos = { x: 0, y: 0 };
let direccio = "none";

readline.emitKeypressEvents(process.stdin);
process.stdin.setRawMode(true);

process.stdin.on('keypress', (str, key) => {
    switch (key.name) {
        case 'up':
            pos.y++;
            direccio = "amunt";
            break;
        case 'down':
            pos.y--;
            direccio = "avall";
            break;
        case 'left':
            pos.x--;
            direccio = "esquerra";
            break;
        case 'right':
            pos.x++;
            direccio = "dreta";
            break;
        case 'c':
            if (key.ctrl) process.exit();
    }

    console.log("nova posició:", pos);

    if (ws.readyState === WebSocket.OPEN) {
      const missatge = {
        type : "moviment",
        posicio : pos,
        direccio : direccio
      }
        ws.send(JSON.stringify(missatge));
    }
});

ws.on('open', () => {
    console.log('Connectat al server');
});

ws.on('message', (message) => {
    console.log('server:', message.toString());
    const missatge = JSON.parse(message.toString());

    if (missatge.type === "resultat"){
        console.log("La partida ha finalitzat");
        console.log("Distancia recorreguda" , missatge.distancia)
        pos = { x: 0, y: 0 };
 
    }
});

ws.on('close', () => {
  clearTimeout(timeout);
});

ws.on('error', (err) => {
    console.error('Error:', err);
});
