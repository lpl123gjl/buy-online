<template>
<div class="wrapper">
    <div class="header" :style="{opacity: opa +'%'}">
        <i class="fa fa-chevron-left" aria-hidden="true" @click="goBack" />
        <p>欢迎登录XX外卖平台</p>
    </div>
    <div class="upload">
        <el-upload class="avatar-uploader" action="" :show-file-list="false" :http-request="uploadFileFn" accept=".jpg,.png" :on-change="getFile">
            <img v-if="account.accountImg" :src="account.accountImg" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
    </div>
    <div class="telephone" :style="{opacity: opa +'%'}">
        <p>+86<i class="fa fa-chevron-right" aria-hidden="true" /></p>
        <input type="text" placeholder="请输入您的手机号码" v-model="account.accountId" @blur="checkAccountId">
    </div>

    <div class="newpassword" :style="{opacity: opa +'%'}">
        <p>密码：</p>
        <input type="password" placeholder="请输入您的密码" v-model="account.password">
    </div>

    <div class="confirmpassword" :style="{opacity: opa +'%'}">
        <p>确认密码：</p>
        <input type="password" placeholder="请您再次确认密码" v-model="confirmpassword">
    </div>

    <div class="username" :style="{opacity: opa +'%'}">
        <p>用户名称：</p>
        <input type="text" placeholder="请输入用户名" v-model="account.accountName">
    </div>

    <div class="sex" :style="{opacity: opa +'%'}">
        <p>性&nbsp;&nbsp;&nbsp;别：</p>
        <input type="radio" name="sex" v-model="account.accountSex" value="1">
        <p class="s">男</p>
        <input type="radio" name='sex' v-model="account.accountSex" value="0">
        <p>女</p>
    </div>

    <div class="agree" :style="{opacity: opa +'%'}">
        <input type="checkbox" v-model="bool" />
        <p>我已经阅读并同意<b>《XX用户协议》</b>、<b>《隐私政策》</b>，并授权XX使用该XX账号信息(如昵称、头像、收货地址)进行统一管理</p>
    </div>

    <button @click="register">立即注册</button>
    <div v-if="show" class="pop">
        <p>请仔细阅读并同意相关协议及隐私政策</p>
        <button class="real" @click="real()">确认</button>
    </div>
    <div v-if="show1" class="pop">
        <p>请输入正确的手机号码</p>
        <button class="real" @click="real()">确认</button>
    </div>
    <div v-if="show2" class="pop">
        <p>您输入的密码不得多余18位且不得少于6位</p>
        <button class="real" @click="real()">确认</button>
    </div>
    <div v-if="show3" class="pop">
        <p>您输入的密码与之前不符</p>
        <button class="real" @click="real()">确认</button>
    </div>
    <div v-if="show4" class="pop">
        <p>用户名不能为空</p>
        <button class="real" @click="real()">确认</button>
    </div>
    <div v-if="show5" class="pop">
        <p>请选择您的性别</p>
        <button class="real" @click="real()">确认</button>
    </div>
    <div v-if="show6" class="pop">
        <p>注册成功!</p>
        <button class="real" @click="real()">确认</button>
    </div>
    <Footer></Footer>
</div>
</template>

<script>
import Footer from "@/components/Footer.vue"
import ElementUI from "element-ui";
export default {
    data() {
        return {
            confirmpassword: '',
            username: '',
            password: '',
            number: '',
            show4: false,
            bool: false,
            show1: false,
            show3: false,
            show2: false,
            show: false,
            show5: false,
            show6: false,
            opa: 100,
            account: {
                accountId: '',
                password: '',
                accountSex: '1',
                accountName: '',
                accountImg:''
            }
        }
    },
    components: {
        Footer,
        ElementUI
    },
    methods: {

        checkAccountId() {
            if (this.account.accountId != '') {
                // let url = '/account/check/'+this.account.accountId;
                let url = '/AccountHandler/check?accountId=' + this.account.accountId;
                this.$axios.get(url).then((response) => {
                    if (response.data.code == 20005) {
                        this.$message({
                            showClose: true,
                            message: response.data.message,
                            type: 'warning'
                        });
                        this.account.accountId = ''
                    }
                }).catch((error) => {
                    console.log(error);
                })
            }
        },
        register() {
            if (this.account.accountId == '') {
                this.$message({
                    showClose: true,
                    message: '手机号不能为空',
                    type: 'warning'
                });
                return
            }
            if (this.account.password == '' || this.confirmpassword == '') {
                this.$message({
                    showClose: true,
                    message: '密码或确认密码不能为空',
                    type: 'warning'
                });
                return
            }
            if (this.account.password != this.confirmpassword) {
                this.$message({
                    showClose: true,
                    message: '确认密码错误',
                    type: 'warning'
                });
                return
            }
            if (this.account.accountName == '') {
                this.$message({
                    showClose: true,
                    message: '昵称不能为空',
                    type: 'warning'
                });
                return
            }

            // let url = '/account/register';
            let url = '/AccountHandler/register'; 
            this.$axios.post(url, this.$qs.stringify(this.account)).then((response) => {
                if (response.data.code == 20000) {
                    this.$message({
                        showClose: true,
                        message: '注册成功',
                        type: 'success'
                    });
                    this.$router.push('/Login')
                } else {
                    this.$message({
                        showClose: true,
                        message: '注册失败',
                        type: 'error'
                    });
                }
            }).catch(error => {
                console.log(error)
            })

        },
        goBack() {
            this.$router.go(-1);
        },
        real() {
            this.show = false
            this.show1 = false
            this.show2 = false
            this.show3 = false
            this.show4 = false
            this.show5 = false
            this.show6 = false
            this.opa = 100
        },
        uploadFileFn() {
            console.log("图片上传转换成功...");
        },
        // 图片转base64
        getFile(file, fileList) {
            this.getBase64(file.raw).then(res => {
                const params = res.split(',')
                if (params.length > 0) {
                    //哪个属性需要存储上传图片，赋值给哪个属性。
                    this.account.accountImg = 'data:image/jpeg;base64,' + params[1]
                    // console.log(this.account.accountImg);
                }
            })
        },
        // 获取图片转base64
        getBase64(file) {
            return new Promise(function (resolve, reject) {
                const reader = new FileReader()
                let imgResult = ''
                reader.readAsDataURL(file)
                reader.onload = function () {
                    imgResult = reader.result
                }
                reader.onerror = function (error) {
                    reject(error)
                }
                reader.onloadend = function () {
                    resolve(imgResult)
                }
            })
        }
    }
}
</script>

<style scoped>
.wrapper .pop {
    width: 50%;
    height: 20vw;
    position: absolute;
    font-size: 3.7vw;
    left: 24%;
    bottom: 36%;
    font-weight: 700;
    text-align: center;
    color: #2c3e50;
    border: 0.1vw solid #ff9068;
    border-radius: 1vw;
    padding: 3.5vw;
    background-color: #dfdddd;
}

.wrapper .pop .real {
    width: 100%;
    height: 10vw;
    position: absolute;
    right: 0;
    bottom: 0;

}

.wrapper .header {
    width: 100%;
    height: 12vw;
    background: linear-gradient(to right, #403A3E, #BE5869);
    color: #79859e;
    font-size: 5vw;
    position: fixed;
    left: 0;
    top: 0;
    display: flex;
    align-items: center;
}

.header i {
    display: flex;
    justify-content: left;
    padding-left: 3vw;
    cursor: pointer;
}

.header p {
    font-weight: 500;
    padding-left: 23vw;
}

.wrapper .telephone {
    width: 100%;
    height: 13vw;
    display: flex;
    align-items: center;
    border-bottom: 0.1vw solid #dddcdc;
    margin-top: 12vw;
}

.wrapper .telephone p {
    font-size: 3.5vw;
    color: #999;
    margin-left: 4vw;
}

.wrapper .telephone input {
    margin-left: 12vw;
    border: 0
}

.wrapper .newpassword {
    width: 100%;
    height: 13vw;
    display: flex;
    align-items: center;
    border-bottom: 0.1vw solid #dddcdc;
}

.wrapper .newpassword p {
    font-size: 3.5vw;
    color: #999;
    margin-left: 4vw;
}

.wrapper .newpassword input {
    border: 0;
    margin-left: 11.5vw;
}

.wrapper .confirmpassword {
    width: 100%;
    height: 13vw;
    display: flex;
    align-items: center;
    border-bottom: 0.1vw solid #dddcdc;
}

.wrapper .confirmpassword p {
    font-size: 3.5vw;
    color: #999;
    margin-left: 4vw;
}

.wrapper .confirmpassword input {
    border: 0;
    margin-left: 4.7vw;
}

.wrapper .username {
    width: 100%;
    height: 13vw;
    display: flex;
    align-items: center;
    border-bottom: 0.1vw solid #dddcdc;
}

.wrapper .username p {
    font-size: 3.5vw;
    color: #999;
    margin-left: 4vw;
}

.wrapper .username input {
    margin-left: 4.7vw;
    border: 0
}

.wrapper .sex {
    width: 100%;
    height: 13vw;
    display: flex;
    align-items: center;
    border-bottom: 0.1vw solid #dddcdc;
}

.wrapper .sex p {
    font-size: 3.5vw;
    color: #999;
    margin-left: 4vw;
}

.wrapper .sex input {
    margin-left: 9vw;
}

.wrapper .sex .s {
    margin-right: 2vw;
}

.wrapper button {
    width: 100%;
    height: 11vw;
    border: 0;
    background: linear-gradient(to right bottom, #fd746c, #ff9068);
    color: rgb(0, 0, 0);
    font-size: 4vw;
    letter-spacing: 0.5vw;
    border-radius: 1vw;
    margin: 0 auto;
}

.wrapper .agree {
    display: flex;
    justify-content: left;
    align-items: first baseline;
    margin-right: 3.6vw;
}

.wrapper .agree input {
    margin: 3.5vw 0;
    margin-left: 4vw;
    margin-right: 4vw;
}

.wrapper .agree p,
b {
    letter-spacing: 0.5vw;
    font-size: 3.5vw;
}

.wrapper .agree b {
    color: #f7ec29;
}

.wrapper .telephone i {
    font-size: 3vw;
    font-weight: 100;
    margin-left: 2vw;
}

/* 上传头像css样式 */
.upload {
    margin: auto;
    background-color: #efefef;
    border-radius: 3vw;
}

.avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
}

.avatar-uploader .el-upload:hover {
    border-color: #409EFF;
}

.avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
}

.avatar {
    width: 178px;
    height: 178px;
    display: block;
    border-radius: 3vw;
}
</style>
