import WebSocket from 'ws';
import readline from 'readline';

let ws;
let tries = 0;

let pos = { x: 0, y: 0 };
let direccio = "none";

function connect() {
    ws = new WebSocket('ws://localhost:8000');

    ws.on('open', () => {
        console.log('Connectat al server');
        tries = 0;
    });

    ws.on('message', (message) => {
        console.log('server:', message.toString());
    });

    ws.on('close', () => {
        if (tries < 2) {
            tries++;
            console.log("Reintentant connexió");
            setTimeout(connect, 3000);
        } else {
            console.error("No es pot connectar al server");
        }
    });

    ws.on('error', (err) => {
        console.error('Error:', err.message);
    });
}

connect();

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

    if (ws && ws.readyState === WebSocket.OPEN) {
        const missatge = {
            type: "moviment",
            posicio: pos,
            direccio: direccio
        };
        ws.send(JSON.stringify(missatge));
    }
});
