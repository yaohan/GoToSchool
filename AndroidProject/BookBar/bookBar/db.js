var mysql = require('mysql');

var conn = mysql.createConnection({
	host:'localhost',
	user:'root',
	password:'root',
	database:'bookbar',
	port:3306
})
conn.connect();

exports.login = function(phoneNumber,password,callback){
	var sql = "SELECT * from account WHERE phoneNumber = '"+phoneNumber+"'";
	conn.query(sql,function(err,rows){
		if(rows!=null && rows[0]!=null){
			var sql = "SELECT * from account WHERE phoneNumber = '"+phoneNumber+"' and  password = '"+password+"'";
			conn.query(sql,function(err,rows){
			if(rows !=null && rows[0] != null){
				callback({
					status:true,
					desc:"登录成功",
					userId:rows[0].userId
				});
			}else{
				callback({
					status:false,
					desc:"用户名或密码错误"
				});
			}
		})
		}else{
			callback({
				status:false,
				desc:"该手机号尚未注册"
			})
		}
	})
	
}

exports.register = function(phoneNumber,password,callback){
	var sql = "SELECT * from account WHERE phoneNumber = '"+phoneNumber+"'";
	conn.query(sql,function(err,rows){
		if(rows!=null && rows[0]!=null){
			callback({
				tatus:false,
				desc:"该手机号已被注册"
			});
		}else{
			var sql = "INSERT INTO account(phoneNumber,password) VALUES ('"+phoneNumber+"','"+password+"')";
			conn.query(sql,function(err,rows){
				if(err){
			console.log(err);
					callback({
						status:false,
						desc:"未知错误"
					})
				}else{
					callback({
						status:true,
						desc:"注册成功"
					})
				}
			})
		}
	})
}

exports.forgetPassword = function(phoneNumber,password,callback){
	var sql = "SELECT * from account WHERE phoneNumber = '"+phoneNumber+"'";
	conn.query(sql,function(err,rows){
		if(rows!=null && rows[0] !=null){
			var sql = "UPDATE account SET password = '"+password+"' WHERE phoneNumber = '"+phoneNumber+"'";
			conn.query(sql,function(err,rows){
				if(err){
			console.log(err);
					callback({
						status:false,
						desc:"未知错误"
					})
				}else{
					callback({
						status:true,
						desc:"修改成功"
					})
				}
			})
		}else{
			callback({
				status:false,
				desc:"该手机号尚未注册"
			})
		}
	})

}

exports.createBook = function(name,publisher,author,summary,bookUrl,isbn,callback){
	var sql = "INSERT INTO book(title,publisher,author,summary,url,isbn13,number) VALUES ('"+name+"','"+publisher+"','"+author+"','"+summary+"','"+bookUrl+"','"+isbn+"',"+1+")";
	conn.query(sql,function(err,rows){
		if(err){
			console.log(err);
			callback({
				status:false,
				desc:"未知错误"
			})
		}else{
			callback({
				status:true,
				desc:"添加成功"
			})
		}
	})
}

exports.getRecommendBooks = function(callback){
	var sql = "SELECT title,url,isbn13 FROM book";
	conn.query(sql,function(err,rows){
		if(err){
			console.log(err);
			callback({
				status:false,
				desc:"未知错误"
			})
		}else{
			callback({
				status:true,
				desc:"获取成功",
				list:rows
			})
		}
	})
}

exports.getBookByISBN = function(isbn,callback){
	var sql = "SELECT * from book where isbn13 = '"+isbn+"'";
	conn.query(sql,function(err,rows){
		if(err){
			console.log(err);
			callback({
				status:false,
				desc:"未知错误"
			})
		}else{
			callback({
				status:true,
				desc:"获取成功",
				book:rows[0]
			})
		}
	})
}

exports.getBookById = function(bookId,callback){
	var sql = "SELECT * from book where bookId = "+bookId;
	conn.query(sql,function(err,rows){
		if(err){
			console.log(err);
			callback({
				status:false,
				desc:"未知错误"
			})
		}else{
			callback({
				status:true,
				desc:"获取成功",
				book:rows[0]
			})
		}
	})
}
exports.search = function(keyWord,callback){
	var sql = "SELECT isbn13,url,title,publisher,author FROM book WHERE CONCAT(title,publisher,author) LIKE '%"+keyWord+"%'";
	conn.query(sql,function(err,rows){
		if(err){
			console.log(err);
			callback({
				status:false,
				desc:"未知错误"
			})
		}else{
			callback({
				status:true,
				desc:"获取成功",
				list:rows
			})
		}
	})
}

exports.collectionBook = function(userId,bookId,time,callback){
	var sql = "SELECT * FROM collection WHERE userId = "+userId +" and bookId = "+bookId;
	console.log("sql:"+sql);
	conn.query(sql,function(err,rows){
	console.log("rows:"+JSON.stringify(rows));
		if(rows!=null && rows[0]!=null){
			callback({
				status:false,
				desc:"您已经收藏过这本书了"
			})
		}else{
			var sql = "INSERT INTO collection(userId,bookId,time) VALUES ("+userId+","+bookId+",'"+time+"')";
			conn.query(sql,function(err,rows){
				if(err){
					console.log(err);
					callback({
						status:false,
						desc:"未知错误"
					})
				}else{
					callback({
						status:true,
						desc:"收藏成功"
					})
				}
			})
		}	
	})
}

exports.getCollection = function(userId,callback){
	var sql = "SELECT b.bookId, b.title, b.url, b.publisher, c.time FROM book b JOIN collection c on b.bookId = c.bookId WHERE c.userId = "+userId;
	conn.query(sql,function(err,rows){
		if(err){
			console.log(err);
			callback({
				status:false,
				desc:"未知错误"
			})
		}else{
			callback({
				status:true,
				desc:"获取成功",
				list:rows
			})
		}
	})
}

exports.reservationBook = function(userId,bookId,time,callback){
	var sql = "SELECT * FROM reservation WHERE userId = "+userId +" and bookId = "+bookId;
	conn.query(sql,function(err,rows){
		if(rows!=null && rows[0]!=null){
			callback({
				status:false,
				desc:"您已经预约过这本书了"
			})
		}else{
			var sql = "INSERT INTO reservation(userId,bookId,time) VALUES ("+userId+","+bookId+",'"+time+"')";
			conn.query(sql,function(err,rows){
				if(err){
					console.log(err);
					callback({
						status:false,
						desc:"未知错误"
					})
				}else{
					callback({
						status:true,
						desc:"预约成功"
					})
				}
			})
		}
	})
}

exports.getReservation = function(userId,callback){
	var sql = "SELECT b.bookId, b.title, b.url, b.publisher, r.time FROM book b JOIN reservation r on b.bookId = r.bookId WHERE r.userId = "+userId;
	conn.query(sql,function(err,rows){
		if(err){
			console.log(err);
			callback({
				status:false,
				desc:"未知错误"
			})
		}else{
			callback({
				status:true,
				desc:"获取成功",
				list:rows
			})
		}
	})
}

exports.borrowBook = function(userId,bookId,time,callback){
	var sql = "SELECT * FROM book WHERE bookId = "+bookId;
	conn.query(sql,function(err,rows){
		if(rows!=null&&rows[0]!=null){
			var num = rows[0].number;
			if(num>0){
				num = num-1;
				var sql = "UPDATE book set number = "+num+" WHERE bookId = " + bookId;
				console.log("sql"+sql);
				conn.query(sql,function(err,rows){
					if(err){
						console.log(err);
						callback({
							status:false,
							desc:"未知错误 update"
						})
					}else{
						var sql = "INSERT INTO borrow(userId,bookId,time) VALUES ("+userId+","+bookId+",'"+time+"')";
						conn.query(sql,function(err,rows){
							if(err){
								console.log(err);
								callback({
									status:false,
									desc:"未知错误 insert"
								})
							}else{
								callback({
									status:true,
									desc:"借阅成功"
								})
							}
						})
					}
				});

				
			}else{
				callback({
					status:false,
					desc:"最后一本书刚被借走了"
				})	
			}
		}else{
			callback({
				status:false,
				desc:"未知错误 select "
			})
		}
	})
}

exports.getBorrow = function(userId,callback){
	var sql = "SELECT b.bookId, b.title, b.url, b.publisher, r.time FROM book b JOIN borrow r on b.bookId = r.bookId WHERE r.userId = "+userId;
	conn.query(sql,function(err,rows){
		if(err){
			console.log(err);
			callback({
				status:false,
				desc:"未知错误"
			})
		}else{
			callback({
				status:true,
				desc:"获取成功",
				list:rows
			})
		}
	})
}

exports.getLibrary = function(callback){
	var sql = "SELECT * FROM library";
	conn.query(sql,function(err,rows){
		if(err){
			console.log(err);
			callback({
				status:false,
				desc:"未知错误"
			})
		}else{
			callback({
				status:true,
				desc:"获取成功",
				list:rows
			})
		}
	})
}
exports.uploadPrint = function(locationX,locationY,wifiInfoList,callback){
	var sql = "INSERT INTO figureprint(locationX,locationY) VALUES ('"+locationX+"','"+locationY+"')";
	conn.query(sql,function(err,rows){
		if(err){
			callback({
				status:false,
				desc:"未知错误"
			})
		}else{
			var wifiInfos = JSON.parse(wifiInfoList);
			wifiInfos.forEach(function(wifiInfo){
				var sql = "INSERT INTO figuredetail(figureId,bssid,level) VALUES ("+rows.insertId+",'"+wifiInfo.bssid+"',"+wifiInfo.level+")";
				conn.query(sql,function(err,rows){
					if(err){
						console.log(err);
					}
				})
			})
			callback({
				status:true,
				desc:"提交成功"
			})
		}
	})
}
exports.getLocation = function(wifiInfoList,callback){
	var sql = "INSERT INTO figureprint(locationX,locationY) VALUES ('"+0+"','"+0+"')";
	conn.query(sql,function(err,rows){
		if(err){
			callback({
				status:false,
				desc:"未知错误"
			})
		}else{
			var insertId = rows.insertId;
			var wifiInfos = JSON.parse(wifiInfoList);
			wifiInfos.forEach(function(wifiInfo){
				var sql = "INSERT INTO figuredetail(figureId,bssid,level) VALUES ("+insertId+",'"+wifiInfo.bssid+"',"+wifiInfo.level+")";
				conn.query(sql,function(err,rows){
					if(err){
						console.log(err);
					}
				})
			})
			var sql = "select sum(abs(a.level-b.level)) as sum, b.figureId from figuredetail a join figuredetail b on a.bssid = b.bssid	where a.figureId= "+ insertId+" and b.figureId != "+insertId+" GROUP BY b.figureId"
			conn.query(sql,function(err,rows){
				if(err){
					console.log(err);
				}else{
					rows.sort(function(a,b){
						return a.sum-b.sum;
					})
					var sql = "select locationX,locationY from figureprint where figureId = "+rows[0].figureId;
					conn.query(sql,function(err,rows){
						if(err || rows == null || rows[0] == null){
							console.log(err);
							return;
						}else{
							console.log("row:"+JSON.stringify(rows));
							var x = rows[0].locationX;
							var y = rows[0].locationY;
							var id = rows[0].figureId
							
							var sql = "DELETE from figureprint where figureId = "+insertId;
							conn.query(sql,function(err,rows){
								console.log("delete figureprint")
								if(err){
									console.log(err);
								}
							})
							var sql = "DELETE from figuredetail where figureId = "+insertId;
							conn.query(sql,function(err,rows){
								console.log("delete figuredetail")
								if(err){
									console.log(err);
								}else{
									console.log("获取成功x:"+x+" y:"+y);
									callback({
										status:true,
										desc:"获取成功",
										locationX:x,
										locationY:y
									})
								}
							})
							
						}
					})
				}
			})
		}
	})
}