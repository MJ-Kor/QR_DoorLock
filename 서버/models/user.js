// models/user.js

var mongoose = require('mongoose');
var bcrypt   = require('bcrypt-nodejs');

// schema
var userSchema = mongoose.Schema({
  name:{
    type:String,
    required:[true,'Name is required!'],
  },
  department:{
    type:String,
    required:[true,'Department is required!']
  },
  username:{
    type:String,
    required:[true,'Username is required!'],
    match:[/^.{6}$/,'학번은 6자입니다!'],
    trim:true,
    unique:true
  },
  password:{
    type:String,
    required:[true,'Password is required!'],
    select:false                                                    // DB에서 해당 모델을 읽어 올때 항목 값을 읽어오지 않음 (password이므로)
  },
  accept:{
    type:String,
    default:'F'
  }
},{
  toObject:{virtuals:true}
});

// virtuals - DB에 저장되는 값 이외의 항목이 필요한 경우
userSchema.virtual('passwordConfirmation')
.get(function(){ return this._passwordConfirmation; })
.set(function(value){ this._passwordConfirmation=value; });

userSchema.virtual('originalPassword')
.get(function(){ return this._originalPassword; })
.set(function(value){ this._originalPassword=value; });

userSchema.virtual('currentPassword')
.get(function(){ return this._currentPassword; })
.set(function(value){ this._currentPassword=value; });

userSchema.virtual('newPassword')
.get(function(){ return this._newPassword; })
.set(function(value){ this._newPassword=value; });

// 패스워드 유효성 검사
var passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,16}$/;
var passwordRegexErrorMessage = '알파벳과 숫자를 섞어 8자 이상으로 입력해주세요.';
userSchema.path('password').validate(function(v) {
  var user = this;

  // 사용자 생성
  if(user.isNew){
    if(!user.passwordConfirmation){
      user.invalidate('passwordConfirmation', '암호 재 확인을 해주세요.');
    }
    if(!passwordRegex.test(user.password)){
      user.invalidate('password', passwordRegexErrorMessage);
    }
    else if(user.password !== user.passwordConfirmation) {
      user.invalidate('passwordConfirmation', '두 암호가 일치하지 않습니다.');
    }
  }

  // 유저 업데이트 : 관리자 기능
  if(!user.isNew){
    if(!user.currentPassword){
      user.invalidate('currentPassword', 'Current Password is required!');
    }
    if(user.currentPassword && !bcrypt.compareSync(user.currentPassword, user.originalPassword)){
      user.invalidate('currentPassword', 'Current Password is invalid!');
    }
    if(user.newPassword && !passwordRegex.test(user.newPassword)){
      user.invalidate('newPassword', passwordRegexErrorMessage);
    } else if(user.newPassword !== user.passwordConfirmation) {
      user.invalidate('passwordConfirmation', 'Password Confirmation does not matched!');
    }
  }
});

// 패스워드 암호화
userSchema.pre('save', function (next){
  var user = this;
  if(!user.isModified('password')){
    return next();
  } else {
    user.password = bcrypt.hashSync(user.password);
    return next();
  }
});

// model methods
userSchema.methods.authenticate = function (password) {
  var user = this;
  return bcrypt.compareSync(password,user.password);
};

var User = mongoose.model('user',userSchema);
module.exports = User;