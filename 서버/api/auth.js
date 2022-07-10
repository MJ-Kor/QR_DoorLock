var express  = require('express');
var router   = express.Router();
var User     = require('../models/User');
var util     = require('../util');
var jwt      = require('jsonwebtoken');
require("dotenv").config({ path: "variables.env"});

// 로그인
router.post('/login',
  function(req,res,next){
    var isValid = true;
    var validationError = {
      name:'ValidationError',
      errors:{}
    };
    // 아이디가 없으면..
    if(!req.body.username){
      isValid = false;
      validationError.errors.username = {message:'Username is required!'};
    }
    // 패스워드가 없으면..
    if(!req.body.password){
      isValid = false;
      validationError.errors.password = {message:'Password is required!'};
    }

    if(!isValid) return res.json(util.successFalse(validationError));
    else next();
  },
  function(req,res,next){
    //입력한 아이디를 스키마에서 찾음
    User.findOne({username:req.body.username})
    .select({password:1,username:1,name:1,email:1})
    // 패스워드 일치 여부 판단
    .exec(function(err,user){
      // 에러
      if(err) return res.json(util.successFalse(err));
      // 패스워드 불일치
      else if(!user||!user.authenticate(req.body.password))
         return res.json(util.successFalse('Username or Password is invalid'));
      // 로그인 성공
      else {
        var payload = {
          _id : user._id,
          username: user.username
        }; // token에 저장될 정보
        var secretOrPrivateKey = process.env.JWT_SECRET;                            // hash 생성에 사용되는 key문자열, 해독 시 생성에 같은 문자열을 사용해야 해독할 수 있다.
        var options = {expiresIn: 60*60*24};                                               // 24시간이 지나면 token이 무효
        // token 생성 함수
        jwt.sign(payload, secretOrPrivateKey, options, function(err, token){
          if(err) return res.json(util.successFalse(err));
          res.json(util.successTrue(token));
        });
      }
    });
  }
);

// token을 해독해서 DB에서 user 정보를 return하는 API
router.get('/me', util.isLoggedin,
  function(req,res,next) {
    User.findById(req.decoded._id)
    .exec(function(err,user){
      if(err||!user) return res.json(util.successFalse(err));
      res.json(util.successTrue(user));
    });
  }
);

// token의 유효기간이 끝나기전에 새로운 토큰을 발행하는 API
router.get('/refresh', util.isLoggedin,
  function(req,res,next) {
    User.findById(req.decoded._id)
    .exec(function(err,user){
      if(err||!user) return res.json(util.successFalse(err));
      else {
        var payload = {
          _id : user._id,
          username: user.username
        };
        var secretOrPrivateKey = process.env.JWT_SECRET;
        var options = {expiresIn: 60*60*24}; // 24시간
        jwt.sign(payload, secretOrPrivateKey, options, function(err, token){
          if(err) return res.json(util.successFalse(err));
          res.json(util.successTrue(token));
        });
      }
    });
  }
);


module.exports = router;