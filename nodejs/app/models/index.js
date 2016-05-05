var config = require('../config')('sequelize');
var Sequelize = require('sequelize');

var sequelize = new Sequelize(
    config['name'],
    config['username'],
    config['password'],
    config['options']
);

var names = [
    'user',
    'post'
];

var models = {
    sequelize: sequelize
};

names.forEach(function(name) {
    console.log('Loading model ' + name);
    models[name] = sequelize.import(__dirname + '\\' + name);
});

module.exports = function(name) {
    return (name) ? models[name] : models;
};
