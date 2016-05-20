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
        }
    };
    
    var options = {
        timestamps: false
    };
    
    return sequelize.define('group', fields, options);
};
