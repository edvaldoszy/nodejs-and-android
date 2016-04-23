var bcrypt = require('bcrypt-nodejs');

module.exports = function(sequelize, DataTypes) {
    
    var fields = {
        id: {
            type: DataTypes.INTEGER,
            primaryKey: true,
            autoIncrement: true
        },
        name: {
            type: DataTypes.STRING(60),
            allowNull: false
        },
        email: {
            type: DataTypes.STRING(100),
            allowNull: false
        },
        password: {
            type: DataTypes.STRING(70),
            allowNull: false
        }
    };
    
    var options = {
        timestamps: false,
        
        hooks: {
            beforeCreate: function(user) {
                user.password = bcrypt.hashSync(user.password);
            }
        },

        instanceMethods: {
            verifyPassword: function(password) {
                return bcrypt.compareSync(password, this.password);
            }
        }
    };

    return sequelize.define('user', fields, options);
};
