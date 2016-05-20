var router = require('express').Router();
var config = require('../config')('jwt');
var jwt = require('jsonwebtoken');
var moment = require('moment-timezone');

var UserModel = require('../models')('user');

router.post('/auth', function(request, response) {

    try {
        UserModel.findOne({
            where: {
                email: request.body.email
            }

        }).then(function(user) {

            if (!user) {
                response.status(401).json({
                    message: 'Invalid credentials'
                });
                return;
            }

            if (!user.validate(request.body.password)) {
                response.status(401).json({
                    message: 'Invalid credentials'
                });
                return;
            }

            var payload = {
                user: {
                    id: user.id,
                    name: user.name,
                    email: user.email,
                    group_id: user.group_id
                },
                expires: moment().add(10, 'minute').unix()
            };

            var token = jwt.sign(payload, config['secret']);
            response.status(200).json({
                token: token,
                expires: payload.expires,
                user: payload.user
            });

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

    var token = request.query['token'] || request.headers['x-access-token'];
    response.status(200).json({
        token: token
    });
});

router.use(function(request, response, next) {

    if (/^\/auth/i.test(request.path)) {
        return next();
    }

    var token = request.query['token'] || request.headers['x-access-token'];
    if (!token) {
        return response.status(401).json({
            message: 'No access token provided'
        });
    }

    jwt.verify(token, config['secret'], function(err, decoded) {
        if (err) {
            return response.status(401).json({
                message: 'Invalid access token'
            });
        }

        if (decoded.expires < moment().unix()) {
            return response.status(403).json({
                message: 'Access token expired'
            });
        }

        request.user = decoded.user;
        return next();
    });
});

module.exports = function(app) {
    app.use('/api', router);
    return router;
};
