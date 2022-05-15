const pkgJson = require('./package.json');
const webpack = require('webpack');
const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const UglifyJSPlugin = require('uglifyjs-webpack-plugin');
const OptimizeCssAssetsPlugin = require('optimize-css-assets-webpack-plugin');

module.exports = {
  mode: process.env.NODE_ENV,
  entry: {
    aliplayercomponents: [path.resolve(__dirname, './src/components/index.js'), path.resolve(__dirname, './src/assets/scss/index.scss')]
  },
  output: {
    path: path.resolve(__dirname, './dist/aliplayer-components'),
    publicPath: '',
    filename: `./[name]-${pkgJson.version}.min.js`
  },
  resolve: {
    extensions: ['.js', '.json'],
    alias: {
      'utils': path.resolve(__dirname, './src/utils')
    }
  },
  module: {
    rules: [{
      test: /\.js$/,
      use: 'babel-loader'
    }, {
      test: /\.(png|jpe?g|gif|svg)(\?.*)?$/,
      use: {
        loader: 'url-loader',
        options: {
          limit: 10240,
          name: 'images/[name].[hash:7].[ext]'
        }
      }
    }, {
      test: /\.(woff2?|eot|ttf|otf|)(\?.*)?$/, 
      use: {
        loader: 'url-loader',
        options: {
          limit: 8192,
          name: 'fonts/[name].[hash:7].[ext]'
        }
      }
    }, {
      test: /\.html$/,
      include: path.resolve(__dirname, 'src/components'),
      use: [{
        loader: 'html-loader',
        options: {
          interpolate: true
        }
      }]
    }, {
      test: /\.s?css$/,
      use: [{
        loader: "style-loader" 
      }, {
        loader: "css-loader"
      }, {
        loader: "sass-loader"
      }]
    }]
  },
  plugins: [
    new webpack.DefinePlugin({
      'process.env.NODE_ENV': JSON.stringify('production')
    }),
    new UglifyJSPlugin({
      uglifyOptions: {
        compress: {
          drop_console: true
        }
      }
    })
  ]
}

