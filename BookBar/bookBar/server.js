var express = require('express');
var app = express();
var db = require('./db');

var server = app.listen(8081,function(){
	var host = server.address().address;
	var port = server.address().port;
	console.log("应用实例，访问地址为http://%s:%s",host,port);
});

app.get('/',function(req,res){
	res.send('Hello World');
})
app.get('/login',function(req,res){
	var phoneNumber = req.query.phoneNumber;
	var password = req.query.password;
	db.login(phoneNumber,password,function(ret){
		res.send(JSON.stringify(ret));
	})
})
app.get('/register',function(req,res){
	var phoneNumber = req.query.phoneNumber;
	var password = req.query.password;
	db.register(phoneNumber,password,function(ret){
		res.send(JSON.stringify(ret));
	})
})
app.get('/forgetPassword',function(req,res){
	var phoneNumber = req.query.phoneNumber;
	var password = req.query.password;
	db.forgetPassword(phoneNumber,password,function(ret){
		res.send(JSON.stringify(ret));
	})
})
app.get('/createBook',function(req,res){
	var url = req.query.bookUrl;
	var name = req.query.name;
	var publisher = req.query.publisher;
	var author = req.query.author;
	var summary = req.query.summary;
	var isbn = req.query.isbn;
	db.createBook(name,publisher,author,summary,url,isbn,function(ret){
		res.send(JSON.stringify(ret));
	})
})
app.get('/getRecommendBooks',function(req,res){
	db.getRecommendBooks(function(ret){
		res.send(JSON.stringify(ret));
	})
})
app.get('/getBookByISBN',function(req,res){
	var isbn = req.query.isbn;
	db.getBookByISBN(isbn,function(ret){
		res.send(JSON.stringify(ret));
	})
})

app.get('/getBookById',function(req,res){
	var bookId = req.query.bookId;
	db.getBookById(bookId,function(ret){
		res.send(JSON.stringify(ret));
	})
})
app.get('/search',function(req,res){
	var keyword = req.query.keyWord;
	db.search(keyword,function(ret){
		res.send(JSON.stringify(ret))
	})
})
app.get('/collectionBook',function(req,res){
	var userId = req.query.userId;
	var bookId = req.query.bookId;
	var time = req.query.time;
	db.collectionBook(userId,bookId,time,function(ret){
		res.send(JSON.stringify(ret));
	})
})
app.get('/getCollection',function(req,res){
	var userId = req.query.userId;
	db.getCollection(userId,function(ret){
		res.send(JSON.stringify(ret));
	})
})
app.get('/reservationBook',function(req,res){
	var userId = req.query.userId;
	var bookId = req.query.bookId;
	var time = req.query.time;
	db.reservationBook(userId,bookId,time,function(ret){
		res.send(JSON.stringify(ret));
	})
})
app.get('/getReservation',function(req,res){
	var userId = req.query.userId;
	db.getReservation(userId,function(ret){
		res.send(JSON.stringify(ret));
	})
})
app.get('/borrowBook',function(req,res){
	var userId = req.query.userId;
	var bookId = req.query.bookId;
	var time = req.query.time;
	db.borrowBook(userId,bookId,time,function(ret){
		res.send(JSON.stringify(ret));
	})
})
app.get('/getBorrow',function(req,res){
	var userId = req.query.userId;
	db.getBorrow(userId,function(ret){
		res.send(JSON.stringify(ret));
	})
})
app.get('/getLibrary',function(req,res){
	db.getLibrary(function(ret){
		res.send(JSON.stringify(ret));
	})
})
app.get('/uploadPrint',function(req,res){
	var locationX = req.query.locationX;
	var locationY = req.query.locationY;
	var wifiInfoList = req.query.wifiInfoList;
	db.uploadPrint(locationX,locationY,wifiInfoList,function(ret){
		res.send(JSON.stringify(ret));
	})
})
app.get('/getLocation',function(req,res){
	var wifiInfoList = req.query.wifiInfoList;
	db.getLocation(wifiInfoList,function(ret){
		res.send(ret);
	})
})