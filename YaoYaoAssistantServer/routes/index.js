var express = require('express');
var router = express.Router();
var db = require('../db/dbHelper')
/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Express' });
});

router.post('/createAccount',function (req,res) {
  var data = JSON.parse(req.body.req);
  var name = data.name;
  var money = data.money;
  var desc = data.desc;
  db.createAccount(name,money,desc,function (ret) {
    console.log("ret:"+JSON.stringify(ret))
    res.send(ret);
  })
})

router.post('/getMainInfo',function (req,res) {
  db.getMainInfo(function (ret) {
    console.log("ret:"+JSON.stringify(ret))
    res.send(ret);
  })
})
module.exports = router;
