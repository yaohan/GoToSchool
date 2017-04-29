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
conn.query("SET time_zone = '+8:00'")
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
    var ret =create(type,time,accountId,tagId,money,desc)
    console.log("ret:"+JSON.stringify(ret))
    callback(ret);
}

function create(type,time,accountId,tagId,money,desc) {
    var sql = "SELECT money FROM account where id =" + accountId;
    conn.query(sql,function (err,rows) {
        if(err){
            return ({
                status:false,
                desc:err
            })
        }else{
            var old = rows[0].money;
            var sum;
            if(type == 1){//收入
                sum = old+money
            }else{
                sum = old-money;
            }
            console.log("type:"+type +"  old:"+old+"  money:"+money+"  sum:"+sum)
            var sql = "INSERT INTO details(type,name,tagId,accountId,money,sum,time) VALUES ("+type+",'"+desc+"'," +
                +tagId+","+accountId+","+money+","+sum+","+conn.escape(time)+")";
            console.log("sql:"+sql)
            conn.query(sql,function (err,rows) {
                if(err){
                    return {
                        status:false,
                        desc:err
                    }
                }else{
                    var sql = "UPDATE account set money = "+sum+ " WHERE id = "+accountId;
                    console.log("sql:"+sql);
                    conn.query(sql,function (err,rows) {
                        if(err){
                            var ret ={status:false,desc:err}
                            return ret;
                        }else{
                            var ret ={status:true,desc:"添加成功"}
                            return ret;
                        }
                    })
                }
            })
        }
    })
}
exports.getList = function (callback) {
    var sql = "SELECT id,name FROM account";
    conn.query(sql,function (err,accounts) {
        if(err){
            callback({status:false,desc:err})
        }else{
            var sql = "SELECT id,name FROM tag";
            conn.query(sql,function (err,tags) {
                if(err){callback({status:false,desc:err})}
                else{
                    callback({
                        status:true,
                        desc:"获取成功",
                        data:{
                            account:accounts,
                            tag:tags
                        }
                    })
                }
            })
        }
    })
}
exports.createTransfer = function (time,source,target,money,desc,callback) {
    create(0,time,source,0,money,desc)
    create(1,time,target,0,money,desc)
    callback({
        status:true,
        desc:"提交成功"
    })
}

function getAccountName(accountId) {
    var sql = "SELECT name FROM account WHERE id="+accountId;
    conn.query(sql,function (err,rows) {
        console.log("rows:"+JSON.stringify(rows))
        if(err){
            return ""
        }else{
            var name =rows[0].name;
            console.log("name:"+name)
            return name
        }
    })
}
exports.getDetails = function (accountId,callback) {
    var sql = "SELECT * FROM details WHERE accountId = "+accountId +" order by time desc";
    console.log("sql:"+sql);
    conn.query(sql,function (err,rows) {
        if(err){
            callback({
                status:false,
                desc:err
            })
        }else{
            callback({
                status:true,
                desc:"获取成功",
                data:rows
            })
        }
    })
}

