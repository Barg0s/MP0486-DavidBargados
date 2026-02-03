const { DataTypes } = require('sequelize');
const { sequelize } = require('../config/database');

const SentimentAnalysis = sequelize.define(
  'SentimentAnalysis',
  {
    id: {
      type: DataTypes.UUID,
      defaultValue: DataTypes.UUIDV4,
      primaryKey: true
    },
    text: {
      type: DataTypes.TEXT,
      allowNull: false
    },
    sentiment: {
      type: DataTypes.STRING,
      allowNull: false
    }
  },
  {
    tableName: 'sentiment_analysis',
    timestamps: true
  }
);

module.exports = SentimentAnalysis;
