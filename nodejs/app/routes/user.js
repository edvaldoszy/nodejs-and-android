var router = require('express').Router();
var parser = require('../util/parser');

var UserModel = require('../models')('user');
var GroupModel = require('../models')('group');
var PostModel = require('../models')('post');

router.get('/', function(request, response) {

    var limit = parser.intval(request.query.limit, 20);
    var page = parser.intval(request.query.page, 1);
    var offset = (page > 1) ? ((page - 1) + limit) : 0;

    var options = {
        attributes: {
            exclude: ['password']
        },

        offset: offset
    };

    // Só limita o resultado se for passado um valor acima de 0
    if (limit > 0)
        options.limit = limit;

    try {
        UserModel.findAndCountAll(
            options

        ).then(function(users) {
            console.info('Listing users');
            response.status(200).json({
                metadata: {
                    count: users.count,
                    limit: limit,
                    page: page
                },
                results: users.rows
            });

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

router.get('/me', function(request, response) {

    try {

        UserModel.belongsTo(GroupModel);

        UserModel.findOne({
            attributes: {
                exclude: ['password']
            },

            include: {
                model: GroupModel,
                required: true
            },

            where: {
                id: request.user.id
            }
        }).then(function(user) {
            if (!user) {
                console.log('User not found');
                response.status(404).json({
                    message: 'User not found'
                });
                return;
            }

            console.info('Showing user');
            response.status(200).json(user);

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

router.get('/:id', function(request, response) {

    try {
        UserModel.findOne({
            attributes: {
                exclude: ['password']
            },
            where: {
                id: request.params.id
            }

        }).then(function(user) {
            if (!user) {
                console.log('User not found');
                response.status(404).json({
                    message: 'User not found'
                });
                return;
            }

            console.info('Showing user');
            response.status(200).json(user);

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

router.get('/:user_id/posts', function(request, response) {

    var limit = parseInt(request.query.limit) || 20;
    var page = parseInt(request.query.page) || 1;
    var offset = (page > 1) ? ((page - 1) + limit) : 0;

    try {
        PostModel.findAll({
            attributes: {
                exclude: ['user_id']
            },

            where: {
                user_id: request.params.user_id
            },

            limit: limit,
            offset: offset

        }).then(function(posts) {
            console.info('Listing posts from user');
            response.status(200).json({
                metadata: {
                    count: posts.length,
                    limit: limit,
                    page: page
                },
                results: posts
            });

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

router.post('/:user_id/posts', function(request, response) {

    try {
        PostModel.create({
            title: request.body.title,
            content: request.body.content,
            user_id: request.params.user_id

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

router.post('/', function(request, response) {

    try {
        UserModel.create({
            name: request.body.name,
            email: request.body.email,
            password: request.body.password,
            group_id: request.body.group_id

        }).then(function(user) {
            user.save();

            console.info('User created');
            response.setHeader('location', '/users/' + user.id);
            response.status(201).send();

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

router.delete('/:id', function(request, response) {

    try  {
        UserModel.destroy({
            where: {
                id: request.params.id
            }

        }).then(function(rows) {
            if (rows == 1) {
                console.log('User deleted');
                response.status(204).send();
                return;
            }

            console.log('User not found');
            response.status(404).json({
                message: 'User no found'
            });

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
    app.use('/api/users', router);
    return router;
};
