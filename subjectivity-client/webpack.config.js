let path = require('path');
let CompressionPlugin = require('compression-webpack-plugin');

module.exports = {
  entry: {},
  output: {
    path: path.resolve(__dirname, 'dist/subjectivity'),
    filename: '[name].[hash].js'
  },
  plugins: [
    new CompressionPlugin()
  ]
};
