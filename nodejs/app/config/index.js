var config = {

    development: {
        mongoose: {
            uri: "mongodb://localhost/nodejs_android"
        },

        sequelize: {
            name: "nodejs_android",
            username: "root",
            password: "",
    
            options: {
                dialect: "mysql",
                host: "localhost",
                port: 3306,
        
                pool: {
                    max: 5,
                    min: 0,
                    idle: 10000
                },
        
                define: {
                    underscored: true
                },
        
                timezone: "America/Sao_Paulo"
            }
        },

        jwt: {
            secret: "a_S*1C=dSy?h{yR*o!~5NF}Oj^05k0"
        }
    }
};

module.exports = function(name) {
    var env = (process.env.NODE_ENV || "development");
    return config[env][name];
};
