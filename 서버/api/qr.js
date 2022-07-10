var express = require('express')
var router = express.Router();
var User = require('../models/User');
var util = require('../util');
var jwt = require('jsonwebtoken');
var QRcode = require('qrcode');

// 현재 로그인 토큰으로 사용자 학번 담긴 QR코드 생성
router.get('/', util.isLoggedin, function(req,res,next){
  let inputStr = "{\"username\" : " + "\"" +req.decoded.username+ "\"" + "}";    // {"username" : "학번"}의 형태의 문자열을 QR에 담아준다.
  console.log("QRsuccess");
  QRcode.toDataURL(inputStr, function(err, url) {
    let line = url.replace(/.*,/,'');                                                                    // /.*/, 은 이미지 생성에 필요없는 문자열 제거
    res.json(util.successTrue(line));
  })
})
// 라우터 내보내기
module.exports = router;