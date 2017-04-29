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
    // console.log("ret:"+JSON.stringify(ret))
    res.send(ret);
  })
})

router.post('/getMainInfo',function (req,res) {
  db.getMainInfo(function (ret) {
    console.log("ret:"+JSON.stringify(ret))
    res.send(ret);
  })
})

router.post("/createDetails",function (req,res) {
  console.log("req:"+req.body.req)
  var data = JSON.parse(req.body.req);
  var type = data.type;
  console.log("time:"+data.date);
  var time = data.time;
  var accountId = data.accountId;
  var tagId = data.tagId;
  var money = data.money;
  var desc = data.name;
  db.createDetail(type,time,accountId,tagId,money,desc,function (ret) {
    res.send(ret);
  })
})

router.post("/getList",function (req,res) {
  db.getList(function (ret) {
    res.send(ret);
  })
})

router.post("/createTransfer",function (req,res) {
  var data = JSON.parse(req.body.req);
  var time = data.time;
  var source = data.source;
  var target = data.target;
  var money = data.money;
  var desc = data.desc;
  db.createTransfer(time,source,target,money,desc,function (ret) {
    res.send(ret);
  })
})

router.post("/getDetails",function (req,res) {
  var data = JSON.parse(req.body.req)
  var accountId = data.accountId;
  db.getDetails(accountId,function (ret) {
    res.send(ret);
  })
})
module.exports = router;
