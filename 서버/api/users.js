var express  = require('express');
var router   = express.Router();
var User     = require('../models/User');
var util     = require('../util');

// 회원 목록 가져오기
router.get('/', util.isLoggedin, function(req,res,next){
  User.find({})
  .sort({username:1})
  .exec(function(err,users){
    res.json(err||!users? util.successFalse(err): util.successTrue(users));
  });
});

// 회원 가입
router.post('/', function(req,res,next){
  var newUser = new User(req.body);
  newUser.save(function(err,user){
    res.json(err||!user? util.successFalse(err): util.successTrue());
  });
});

// 사용자 아이디로 사용자 찾기 - 추후 관리자 앱에서 사용
router.get('/:username', util.isLoggedin, function(req,res,next){
  User.findOne({username:req.params.username})
  .exec(function(err,user){
    res.json(err||!user? util.successFalse(err): util.successTrue(user));
  });
});

// 사용자 정보 업데이트 - 추후 관리자 앱에서 사용
router.put('/:username', util.isLoggedin, checkPermission, function(req,res,next){
  User.findOne({username:req.params.username})
  .select({password:1})
  .exec(function(err,user){
    if(err||!user) return res.json(util.successFalse(err));

    // 사용자 속성 업데이트 - 추후 관리자 앱에서 사용
    user.originalPassword = user.password;
    user.password = req.body.newPassword? req.body.newPassword: user.password;
    for(var p in req.body){
      user[p] = req.body[p];
    }

    // 변경한 사용자 저장 - 추후 관리자 앱에서 사용
    user.save(function(err,user){
      if(err||!user) return res.json(util.successFalse(err));
      else {
        user.password = undefined;
        res.json(util.successTrue(user));
      }
    });
  });
});

// 사용자 삭제 - 추후 관리자 앱에서 사용
router.delete('/:username', util.isLoggedin, checkPermission, function(req,res,next){
  User.findOneAndRemove({username:req.params.username})
  .exec(function(err,user){
    res.json(err||!user? util.successFalse(err): util.successTrue(user));
  });
});

module.exports = router;

// private functions
function checkPermission(req,res,next){ 
  User.findOne({username:req.params.username}, function(err,user){
    if(err||!user) return res.json(util.successFalse(err));
    else if(!req.decoded || user._id != req.decoded._id) 
      return res.json(util.successFalse(null,'You don\'t have permission'));
    else next();
  });
}