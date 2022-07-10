// 시스템에서 모든 오류와 성공 여부를 담당하는 함수들

var jwt = require('jsonwebtoken');
require("dotenv").config({ path: "variables.env"});
var util = {};

util.successTrue = function(data){ 
  console.log("success");
  if(data == null)
  {
    return {
      success:true,
      message:"None",
      errors:"None",
      data:"None"
    };
  }
  else{
    return {
      success:true,
      message:"None",
      errors:"None",
      data:data
    };
  }
};

util.successFalse = function(err, message){ 
  if(!err&&!message){ 
    message = 'data not found';
  }
if(message == null)
{
  return {
    success:false,
    message:"None",
    error:(err)? util.parseError(err): "None",
    data:"None"
  };
}
return {
  success:false,
  message:message,
  error:(err)? util.parseError(err): "None",
  data:"None"
};
};

util.parseError = function(errors){
  var parsed = {};
  if(errors.name == 'ValidationError'){
    for(var name in errors.errors){
      var validationError = errors.errors[name];
      if(name == "passwordConfirmation")                                         // 회원가입 암호 불일치
      {
        parsed = validationError.message;
      }
      else if(name == "password")                                                    // 패스워드 에러
      {
        parsed = validationError.message;
      }
      else if(name == "username")                                                    // 학번 에러
      {
        parsed = validationError.message;
      }
      else
      parsed[name] = { text:validationError.message };
    }
  } else if(errors.code == '11000' && errors.errmsg.indexOf('username') > 0) {
    parsed = '해당 학번은 이미 등록되어 있습니다.'
  } else {
    if(errors.code == '11000')
      parsed = '중복된 이름입니다.'
    else if (errors == "Username or Password is invalid")
    {
      parsed = '잘못된 회원 정보입니다.'
    }
    else
      console.log(errors);
      parsed.unhandled = errors;
  }
  return parsed;
};


// middlewares - 토큰이 있으면 토큰 해독
util.isLoggedin = function(req,res,next){ //4
  var token = req.headers['x-access-token'];                                       // req 헤더에서 넘어온 token값 저장
  if (!token) return res.json(util.successFalse(null,'유효하지 않은 토큰입니다.'));
  else {
    jwt.verify(token, process.env.JWT_SECRET, function(err, decoded) {
      if(err) return res.json(util.successFalse(err));
      else{
        req.decoded = decoded;
        next();
      }
    });
  }
};

module.exports = util;