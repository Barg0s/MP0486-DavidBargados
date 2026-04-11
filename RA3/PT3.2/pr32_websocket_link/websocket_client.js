import WebSocket from 'ws';
import readline from 'readline';

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

    if (ws.readyState === ws.OPEN) {
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
});

ws.on('error', (err) => {
    console.error('Error:', err);
});
