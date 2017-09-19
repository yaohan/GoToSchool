var express = require('express');
var router = express.Router();
var fs = require('fs');
var path = require('path')
/* GET home page. */
router.get("/downloadExample",function(req,res,next){
  var fileName = "data.json";
  var filePath = path.join(__dirname,"../public",fileName);
  var stats = fs.statSync(filePath);
  if(stats.isFile()){
    res.set({
      'Content-Type':'application/octect-stream',
      'Content-Disposition':'attachment;filename='+fileName,
      'Content-Length':stats.size
    });
    fs.createReadStream(filePath).pipe(res);
  }else{
    res.end(404);
  }
})

router.get('/', function(req, res, next) {
  res.render('main');
});

router.get('/detail',function (req,res,next) {
    res.render('detail')
})
router.get('/showExample',function(req,res,next){
    res.render("showExample");
})
router.post("/upload",function(req,res,next){
    var form = new multiparty.Form({uploadDir:'./public/files/'})
    form.parse(req,function (err,fields,files) {
        var filesTmp = JSON.stringify(files,null,2)
        if(err){
            console.log("parse error: "+err);
        }else{
            console.log("parse files:"+filesTmp)
        }
    })
})


module.exports = router;
