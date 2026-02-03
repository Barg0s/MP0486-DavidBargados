const axios = require('axios');
const SentimentAnalysis = require('../models/sentimentAnalysis');
const { logger } = require('../config/logger');

const OLLAMA_API_URL = process.env.CHAT_API_OLLAMA_URL;
const DEFAULT_OLLAMA_MODEL = process.env.CHAT_API_OLLAMA_MODEL;

async function getSentimentFromOllama(text, model = DEFAULT_OLLAMA_MODEL) {
  try {
    const response = await axios.post(`${OLLAMA_API_URL}/generate`, {
      model,
      prompt: `Analitza el sentiment del següent text i respon només amb positive, negative o neutral.\n\nText:\n"${text}"`,
      stream: false
    });

    const raw = response.data.response || (response.data?.choices && response.data.choices[0]?.content) || '';
    let sentiment = String(raw).trim().toLowerCase();

    if (!['positive', 'negative', 'neutral'].includes(sentiment)) {
      const match = sentiment.match(/(positive|negative|neutral)/);
      sentiment = match ? match[1] : 'error';
    }

    return { sentiment, raw };

  } catch (err) {
    logger.error('Error llamando a Ollama', { error: err.message });
    return { sentiment: 'error', raw: err.message };
  }
}

async function analyzeSentimentController(req, res) {
  try {
    const { text, userId = null, gameId = null } = req.body;

    if (!text || typeof text !== 'string' || !text.trim()) {
      return res.status(400).json({ error: 'El campo "text" es obligatorio y debe ser un string.' });
    }

    const { sentiment, raw } = await getSentimentFromOllama(text);

    // Guardar en BD
    const saved = await SentimentAnalysis.create({
      text,
      sentiment,
      raw,
      userId,
      gameId
    });

    logger.info(`Sentiment analizado: ${sentiment}`);

    return res.status(201).json(saved);

  } catch (err) {
    logger.error('Error en analyzeSentimentController', { error: err.message });
    return res.status(500).json({ error: 'Error interno del servidor' });
  }
}

module.exports = { analyzeSentimentController, getSentimentFromOllama };
