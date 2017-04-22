var mysql = require('mysql');

var conn = mysql.createConnection({
    host: '5835638b397af.gz.cdb.myqcloud.com',
    user: 'root',
    // password: 'root',
    password:'smxsjk123456',
    database: 'yao',
    port: 12409
})
conn.connect();

setInterval(function () {
    conn.query('SELECT 1');
}, 5000);

exports.createAccount = function (name,money,desc,callback) {
    var sql = "INSERT INTO account(name,des,money) VALUES ('"+name+"','"+desc+"',"+money+")";
    console.log("sql:"+sql)
    conn.query(sql,function (err,rows) {
        if(err){
            callback({
                status:false,
                desc:err
            })
        }else{
            callback({
                status:true,
                desc:"创建成功"
            })
        }
    })
}
exports.getMainInfo = function (callback) {
    var sql = "SELECT sum(money) as sum from account;"
    conn.query(sql,function (err,rows) {
        if (err) {
            callback({
                status: false,
                desc: err
            })
        } else {
            var money = rows[0].sum;
            var sql = "SELECT * from account"
            conn.query(sql, function (err, rows) {
                if(err){
                    callback({
                        status:false,
                        desc:err
                    })
                }else{
                    callback({
                        status:true,
                        data:{
                            money:money,
                            list:rows
                        }
                    })
                }
            })
        }
    })
}
exports.createDetail = function (type,time,accountId,tagId,money,desc,callback) {
    var sql = "SELECT money FROM account where id =" + accountId;
    conn.query(sql,function (err,rows) {
        if(err){
            callback({
                status:false,
                desc:err
            })
        }else{
            var old = rows[0].money;
            if(type == 0){//收入
                var sum = old+money
            }else{
                var sum = old-money;
            }
            var sql = "INSERT INTO details(type,desc,tagId,accountId,money,sum,time) VALUES ("+type+","+desc+"," +
                +tagId+","+accountId+","+money+","+sum+","+conn.escape(time)+")";
            conn.query(sql,function (err,rows) {
                if(err){
                    callback({
                        status:false,
                        desc:err
                    })
                }else{
                    callback({
                        status:true,
                        desc:"添加成功"
                    })
                }
            })
        }
    })
}
