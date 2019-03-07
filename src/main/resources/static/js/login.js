/**
 * Created by Administrator on 2017/10/16.
 */
$(function () {
    new Vue({
        el: "#vueBox",
        data: function () {
            return {
                loginHeight: window.innerHeight + 'px',
                marginHeight: (window.innerHeight - 592) / 2 + 'px',
                form: {username: '', passwd: ''}
            }
        },
        methods: {
            login: function () {

                //保存(新增修改)
                var self = this;
                var uname = self.form.username;
                var pwd = hex_md5(self.form.passwd);

                //获取当前时间戳
                var timestamp = new Date();
                // params.pageindex = 0;
                $.ajax({
                    url: "/login/login",
                    // url: "http://localhost:80/login",
                    method: "post",
                    // contentType: "application/json;charset=utf-8",
                    dataType: "json",
                    data: {
                        "uname":uname,
                        "pwd":pwd
                    } ,
                    success: function (res) {
                        debugger;
                        if(res.res.errcode == 0){
                            var  r = res.res;
                            //登陆成功
                            var accesstoken = r.data.accesstoken;
                            var loginUid = r.data.uid;
                            var loginNick = r.data.nick;
                            var loginHead = r.data.head;
                            var loginAge = r.data.age;
                            var loginGender = r.data.gender;
                            var loginExpire_utc = r.data.expire_utc;
                            $.cookie("accesstoken",accesstoken);
                            $.cookie("loginUid",loginUid);
                            $.cookie("loginNick",loginNick);
                            $.cookie("loginHead",loginHead);
                            $.cookie("loginAge",loginAge);
                            $.cookie("loginGender",loginGender);
                            $.cookie("loginExpire_utc",loginExpire_utc);

                            window.location.href = '/login/index';
                        }else{
                            alert(res.res.errmsg);
                        }
                    },
                });


            }

        }
    });

})




