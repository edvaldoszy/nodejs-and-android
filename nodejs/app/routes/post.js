var router = require('express').Router();
var moment = require('moment-timezone');

var UserModel = require('../models')('user');
var PostModel = require('../models')('post');

router.get('/', function(request, response) {

    try {
        PostModel.belongsTo(UserModel);
        PostModel.findAll({
            attributes: {
                exclude: ['user_id']
            },

            include: {
                model: UserModel,
                required: true,
                attributes: {
                    exclude: ['password']
                }
            }

        }).then(function(results) {
            console.info('Listing posts');
            response.status(200).json({
                results: results
            });

        }).catch(function(err) {
            throw err;
        });

    } catch (ex) {
        console.error(ex);
        response.status(400).json({
            message: ex.message
        });
    }
});

router.post('/', function(request, response) {

    try {
        PostModel.create({
            title: request.body.title,
            content: request.body.content,
            user_id: request.body.user_id

        }).then(function(post) {
            post.save();

            console.log('Post created');
            response.setHeader('location', '/posts/' + post.id);
            response.status(204).send();

        }).catch(function(err) {
            throw err;
        });

    } catch (ex) {
        console.error(ex);
        response.status(500).json({
            message: ex.message
        });
    }
});

module.exports = function(app) {
    app.use('/api/posts', router);
    return router;
};
