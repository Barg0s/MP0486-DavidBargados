const fs = require('fs');
const path = require('path');
const { MongoClient } = require('mongodb');
const xml2js = require('xml2js');
require('dotenv').config();


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
      CreationDate: row.CreationDate,
      Score: row.Score,
      ViewCount: row.ViewCount,
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
    console.log('Conectado a MongoDB');

    const database = client.db('coffee_db');
    const collection = database.collection('coffee');

    console.log('Llegint el fitxer XML...');
    const xmlData = await parseXMLFile(xmlFilePath);

    console.log('Processant les dades...');
    let questions = processCoffeeData(xmlData);

    console.log('Eliminant dades existents...');
    await collection.deleteMany({});
 
    //TODO -> ORDENAR las 10000 preguntas con mas viewCount
    
    console.log('Inserint dades a MongoDB...');
    const result = await collection.insertMany(questions);

    console.log(`${result.insertedCount} documents inserits correctament.`);
    console.log('Dades carregades amb èxit!');


  } catch (error) {
    console.error('Error carregant les dades a MongoDB:', error);
  } finally {
    await client.close();
    console.log('Connexió a MongoDB tancada');
  }
}

loadDataToMongoDB();