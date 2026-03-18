const fs = require('fs');
const path = require('path');
const { MongoClient } = require('mongodb');
const xml2js = require('xml2js');
require('dotenv').config();


//https://stackoverflow.com/questions/35321004/mongodb-query-in-with-regex-array-of-element
const paraules =  ["pug", "wig", "yak", "nap", "jig", "mug", "zap", "gag", "oaf","elf"] 
regex = paraules.join("|");
async function ViewCountMajor(){
  const uri = process.env.MONGODB_URI || 'mongodb://root:password@localhost:27017/';
  const client = new MongoClient(uri);
  try {
    await client.connect();

    const database = client.db('coffee_db');
    const collection = database.collection('coffee');


    const result = await collection.aggregate([
    {
        $group: {
        _id: null,
        averageViewCount: { $avg: "$question.ViewCount" } // campo del documento
        }
    }
    ]).toArray();

    const average = result[0]?.averageViewCount;
    const titol = await collection.find({ "question.ViewCount": {$gte: average} }).toArray();
    console.log(titol)


    
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

    const database = client.db('coffee_db');
    const collection = database.collection('coffee');

    const titol = await collection.find({ "question.Title": { $regex: regex, $options: 'i'} }).toArray();
    console.log(titol)


    
    } catch (error) {
        console.log("error");
    } finally {
        await client.close();
    }
    
} 
//ViewCountMajor();
Paraules();