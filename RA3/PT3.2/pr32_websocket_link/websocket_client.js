import WebSocket from 'ws';
import readline from 'readline';

let ws;
let intents = 0;

let pos = { x: 0, y: 0 };
let direccio = "cap";
let conectat = false;

function connect() {
    ws = new WebSocket('ws://localhost:8000');

    ws.on('open', () => {
        console.log('Connectat al server');
        intents = 0;
        });

    ws.on('message', (message) => {
        const data = JSON.parse(message.toString());
        switch (data.type) {
            case "connexio":
                console.log("Connectat. Partida ID:", data.gameId);
                conectat = true;
                break;

            case "novaPartida":
                console.log("Nova partida iniciada. ID:", data.gameId);
                pos = { x: 0, y: 0 };
                direccio = "cap";
                break;

            case "resultat":
                console.log("Partida finalitzada. Distància:", data.distancia);
                break;
            case "invalid":
                console.log(data.body);
                break;
        }
    });

    ws.on('close', () => {
        if (intents < 2) {
            intents++;
            console.log("Reintentant connexió");
            conectat = false;
            pos = { x: 0, y: 0 };
            setTimeout(connect, 3000);
        } else {
            console.error("No es pot connectar al server");
            conectat = false;
            
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
    if (key.ctrl && key.name === 'c') {
        process.exit();
    }
    if (!conectat) return;
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
