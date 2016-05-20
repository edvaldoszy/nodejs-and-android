var self = {};

self.intval = function(v, d) {
    try {
        v = parseInt(v);
        if (isNaN(v))
            return (d || 0);

        return v;
    } catch (err) {
        return 0;
    }
};

module.exports = self;
