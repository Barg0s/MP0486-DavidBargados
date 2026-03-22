const fs = require('fs');
const path = require('path');
const { MongoClient } = require('mongodb');
const xml2js = require('xml2js');
require('dotenv').config();
const PDFDocument = require('pdfkit');

//https://stackoverflow.com/questions/35321004/mongodb-query-in-with-regex-array-of-element
const paraules =  ["pug", "wig", "yak", "nap", "jig", "mug", "zap", "gag", "oaf","elf"] 
const regex = paraules.join("|");
const outDir = path.join(__dirname, '../../data/out');

async function ViewCountMajor(){
  const uri = process.env.MONGODB_URI || 'mongodb://root:password@localhost:27017/';
  const client = new MongoClient(uri);
  try {
    await client.connect();

    const database = client.db('ai_db');
    const collection = database.collection('ai');


    const result = await collection.aggregate([
    {
        $group: {
        _id: null,
        averageViewCount: { $avg: "$question.ViewCount" } 
        }
    }
    ]).toArray();

    const average = result[0]?.averageViewCount;
    const preguntasViewCount  = await collection.find({ "question.ViewCount": {$gte: average} }).toArray();

    const informe1 = new PDFDocument();
    //https://www.npmjs.com/package/pdfkit
    informe1.pipe(fs.createWriteStream(path.join(outDir, 'informe1.pdf')));
    informe1.fontSize(22).font('Helvetica-Bold').text('PREGUNTES AMB MÉS VIEW COUNT QUE LA MITJANA', { align: 'center' });    
    informe1.fontSize(12.5).font('Helvetica').text(`Mitjana: ${average}`, 100, 150);

    preguntasViewCount.forEach(p =>{
        
            informe1.fontSize(12).font('Helvetica-Bold').text(p.question.Title);
            informe1.fontSize(12).font('Helvetica').text(`ViewCount: ${p.question.ViewCount}`);
    })
    informe1.end();


    
    if (average > 0){
            console.log("La mitjana es:", average)

    }else{
        console.log("No h'hi ha mitjana")

    }
    
    } catch (error) {
        console.log("error");
    } finally {
        await client.close();
    }
    
}

async function Paraules(){
  //https://www.mongodb.com/community/forums/t/is-there-a-way-to-do-string-contains-type-of-search-with-multiple-keywords/229656
  //https://www.mongodb.com/docs/manual/reference/operator/query/regex/
  const uri = process.env.MONGODB_URI || 'mongodb://root:password@localhost:27017/';
  const client = new MongoClient(uri);
  try {
    await client.connect();

    const database = client.db('ai_db');
    const collection = database.collection('ai');

    const titol = await collection.find({ "question.Title": { $regex: regex, $options: 'i'} }).toArray();

    const informe2 = new PDFDocument();
    //https://www.npmjs.com/package/pdfkit
    informe2.pipe(fs.createWriteStream(path.join(outDir, 'informe2.pdf')));
    informe2.fontSize(22).font('Helvetica-Bold').text('Preguntes amb paraules clau', { align: 'center' });    
    informe2.fontSize(22).font('Helvetica-Bold').text('Paraules clau', { align: 'center' });    
    informe2.fontSize(12).font('Helvetica-Bold').text('Paraules clau: ' + paraules.join(', '));



    titol.forEach(p =>{
        
            informe2.fontSize(12).font('Helvetica-Bold').text(p.question.Title);
    })
    informe2.end();
    
    } catch (error) {
        console.log("error");
    } finally {
        await client.close();
    }
    
} 
ViewCountMajor();
Paraules();
