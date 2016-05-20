var app = require('express')();
var load = require('express-load');
var morgan = require('morgan');
var bodyParser = require('body-parser');
var sequelize = require('./app/models')('sequelize');

// Get port number from environment or default number 80
const PORT = parseInt(process.env.PORT) || 80;

app.use(morgan('dev'));
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());
app.set('port', PORT);

// Load API routes
load('routes', { cwd: 'app' })
    .into(app);

sequelize.sync().then(function() {
    app.listen(PORT, function() {
        console.log('Server running at port ' + PORT);
    });
});
