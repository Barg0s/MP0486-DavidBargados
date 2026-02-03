const express = require('express');
const router = express.Router();
const { registerPrompt, getConversation, listOllamaModels } = require('../controllers/chatController');
const { analyzeSentimentController } = require('../controllers/sentimentController');

/**
 * @swagger
 * /api/chat/sentiment-analysis:
 *   post:
 *     summary: Analitza el sentiment d’un missatge mitjançant IA
 *     tags: [Chat]
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             type: object
 *             properties:
 *               text:
 *                 type: string
 *                 example: "Aquest servei és molt dolent"
 *     responses:
 *       201:
 *         description: Resultat de l’anàlisi i guardat en BD
 */
router.post('/sentiment-analysis', analyzeSentimentController);
router.get('/models', listOllamaModels);

module.exports = router;
