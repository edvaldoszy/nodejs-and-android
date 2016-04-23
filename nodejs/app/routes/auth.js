var router = require('express').Router();
var config = require('../config')('jwt');
var jwt = require('jsonwebtoken');

var UserModel = require('../models')('user');

router.post('/auth', function(request, response) {

    try {
        UserModel.findOne({
            where: {
                email: request.body.email
            }

        }).then(function(user) {

            if (!user) {
                response.status(403).json({
                    message: 'Invalid credentials'
                });
                return;
            }

            if (!user.verifyPassword(request.body.password)) {
                response.status(403).json({
                    message: 'Invalid credentials'
                });
            } else {
                var payload = {
                    name: user.name,
                    email: user.email
                };

                var token = jwt.sign(payload, config['secret']);
                response.status(200).json({
                    token: token
                });
            }
        }).catch(function (err) {
            throw err;
        });

    } catch (ex) {
        console.error(ex);
        response.status(500).json({
            message: ex.message
        });
    }
});

router.get('/auth/validate', function(request, response) {

    response.status(200).send();
});

router.use(function(request, response, next) {

    if (request.path == '/auth') {
        next();
        return;
    }

    var token = request.query['token'] || request.headers['token'];
    if (!token) {
        response.status(403).json({
            message: 'No access token provided'
        });
        return;
    }

    jwt.verify(token, config['secret'], function(err, decoded) {
        if (err) {
            return response.status(403).json({
                message: 'Invalid access token'
            });
        }

        request.decoded = decoded;
        next();
    });
});

module.exports = function(app) {
    app.use('/api', router);
    return router;
};
