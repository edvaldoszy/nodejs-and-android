module.exports = function(sequelize, DataTypes) {

    var fields = {
        title: {
            type: DataTypes.STRING(100),
            allowNull: false
        },
        content: {
            type: DataTypes.TEXT,
            allowNull: false
        },
        created: {
            type: DataTypes.DATE,
            allowNull: false,
            defaultValue: sequelize.fn('NOW')
        },
        updated: {
            type: DataTypes.DATE
        },
        status: {
            type: DataTypes.ENUM('ativo', 'inativo'),
            allowNull: false,
            defaultValue: 'ativo'
        },
        user_id: {
            type: DataTypes.INTEGER,
            allowNull: false
        }
    };

    var options = {
        timestamps: false
    };

    return sequelize.define('post', fields, options);
};
