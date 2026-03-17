const fs = require('fs');
const path = require('path');
const { MongoClient } = require('mongodb');

const winston = require('winston');
const xml2js = require('xml2js');
require('dotenv').config();
const logsDir = path.join(__dirname, '../../data/logs');


const logger = winston.createLogger({ //https://www.npmjs.com/package/winston
  level: 'info',
  format: winston.format.json(),
  defaultMeta: { service: 'user-service' },
  transports: [
    //
    // - Write all logs with importance level of `error` or higher to `error.log`
    //   (i.e., error, fatal, but not other levels)
    //
    new winston.transports.File({ 
      filename: path.join(logsDir, 'error.log'),
      level: 'error'
    }),    //
    // - Write all logs with importance level of `info` or higher to `combined.log`
    //   (i.e., fatal, error, warn, and info, but not trace)
    //
    new winston.transports.File({ filename: path.join(logsDir,'exercici1.log')}),
  ],
});
const xmlFilePath = path.join(__dirname, '../../data/Posts.xml');
async function parseXMLFile(filePath) {
  try {
    const xmlData = fs.readFileSync(filePath, 'utf-8');
    const parser = new xml2js.Parser({ 
      explicitArray: false,
      mergeAttrs: true
    });
    
    return new Promise((resolve, reject) => {
      parser.parseString(xmlData, (err, result) => {
        if (err) {
          reject(err);
        } else {
          resolve(result);
        }
      });
    });
  } catch (error) {
    console.error('Error llegint o analitzant el fitxer XML:', error);
    throw error;
  }
}


function processCoffeeData(data) {
  const coffees = Array.isArray(data.posts.row) ? data.posts.row : [data.posts.row];

  return coffees.map(row => ({
    question: {
      Id: row.Id,
      PostTypeId: row.PostTypeId,
      AcceptedAnswerId: row.AcceptedAnswerId,
      CreationDate: new Date(row.CreationDate),
      Score: parseInt(row.Score),
      ViewCount: parseInt(row.ViewCount),
      Body: row.Body,
      OwnerUserId: row.OwnerUserId || null,
      LastActivityDate: row.LastActivityDate ,
      Title: row.Title,
      Tags: row.Tags,
      AnswerCount: row.AnswerCount ,
      CommentCount: row.CommentCount,
      ContentLicense: row.ContentLicense
    }
  }));
}



async function loadDataToMongoDB() {
  const uri = process.env.MONGODB_URI || 'mongodb://root:password@localhost:27017/';
  const client = new MongoClient(uri);

  try {
    await client.connect();
    logger.info('Connectat a MongoDB')

    const database = client.db('coffee_db');
    const collection = database.collection('coffee');
    

    
    logger.info('Llegint el fitxer XML...');

    const xmlData = await parseXMLFile(xmlFilePath);

    logger.info('Processant les dades...')

    let questions = processCoffeeData(xmlData);

    logger.info('Eliminant dades existents...');

    await collection.deleteMany({});
 
    //TODO -> ORDENAR las 10000 preguntas con mas viewCount
    
    logger.info('Inserint dades a MongoDB...');

    const result = await collection.insertMany(questions);

    logger.info(`${result.insertedCount} documents inserits correctament.`);


  } catch (error) {

    logger.error('Error carregant les dades a MongoDB:', error);
  } finally {
    await client.close();
    logger.info('Connexió a MongoDB tancada')

  }
}

  logger.add(new winston.transports.Console({
    format: winston.format.simple(),
  }));

loadDataToMongoDB();