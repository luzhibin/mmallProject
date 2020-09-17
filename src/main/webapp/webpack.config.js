const path = require('path');

module.exports = {
  entry: './src/page/index/index.js',
  output: {
    path: path.resolve(__dirname, 'dist'),
    filename: 'app.js'
  }
};