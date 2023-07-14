<template>
  <div class="register-container">
    <article class="header">
      <header>
        <span class="login" style="margin-top:15px">
          <a class="bold" href="/login">已有账号？</a>
        </span>
      </header>
    </article>
    <section>
      <el-form
        ref="ruleForm"
        :model="ruleForm"
        :rules="rules"
        label-width="200px"
        autocomplete="off"
        hide-required-asterisk="true"
        size="medium"
      >
        <div style="padding-top: 10px">
          <el-form-item label="用户名" prop="userName">
            <el-input v-model="ruleForm.userName" type="用户名" />
          </el-form-item>
          <el-form-item label="真实姓名" prop="realName">
            <el-input
              v-model="ruleForm.realName"
            />
          </el-form-item>
          <el-form-item label="手机号" prop="phone">
            <el-input
              v-model="ruleForm.phone"
            />
          </el-form-item>
          <el-form-item label="密码" prop="pwd">
            <el-input v-model="ruleForm.pwd" type="password" />
          </el-form-item>
          <el-form-item label="确认密码" prop="cpwd">
            <el-input v-model="ruleForm.cpwd" type="password" />
          </el-form-item>
          <el-form-item>
            <el-button
              type="primary"
              style="width: 615px;"
              @click="register"
            >注册</el-button>
          </el-form-item>
        </div>
      </el-form>
    </section>

    <div class="error">{{ error }}</div>
  </div>
</template>

<script>
import { register } from '@/api/user'

export default {
  name: 'Register',
  data() {
    return {
      error: '',
      isDisable: false,
      codeLoading: false,
      ruleForm: {
        userName: '',
        realName: '',
        phone: '',
        pwd: '',
        cpwd: ''
      },
      rules: {
        userName: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        realName: [
          { required: true, message: '请输入真实姓名', trigger: 'blur' },
          { pattern: /^[\u4e00-\u9fa5]+$/, message: '请输入中文字符', trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '请输入手机号', trigger: 'blur' },
          { pattern: /^\d{11}$/, message: '请输入11位数字', trigger: 'blur' }
        ],
        pwd: [{
          required: true,
          message: '创建密码',
          trigger: 'blur'
        }, { min: 3, max: 8, message: '长度在 3 到 8 个字符', trigger: 'blur' }],
        cpwd: [{
          required: true,
          message: '确认密码',
          trigger: 'blur'
        }, {
          validator: (rule, value, callback) => {
            if (value === '') {
              callback(new Error('请再次输入密码'))
            } else if (value !== this.ruleForm.pwd) {
              callback(new Error('两次输入密码不一致'))
            } else {
              callback()
            }
          },
          trigger: 'blur'
        }]

      }
    }
  },
  methods: {
    // 用户注册
    register: function() {
      this.$refs['ruleForm'].validate((valid) => {
        if (valid) {
          const user = {
            userName: this.ruleForm.userName,
            realName: this.ruleForm.realName,
            phone: this.ruleForm.phone,
            password: this.ruleForm.pwd
          }
          register(user).then(res => {
            this.$message({
              showClose: true,
              message: '注册成功',
              type: 'success'
            })
            setTimeout(() => {
              this.$router.push('/')
            }, 2000)
          }).catch(error => {
            console.log(error)
          })
        }
      })
    }
  }
}
</script>

<style lang="scss">
$bg: #3b2e21;
$light_gray: #464545;
$cursor: #262626;

@supports (-webkit-mask: none) and (not (cater-color: #464545)) {
  .register-container .el-input input {
    color: #464545;
  }
}

/* reset element-ui css */
.register-container {

  .el-input {
    display: inline-block;
    height: 20px;
    width: 95%;

    input {
      width: 615px;
      background: rgba(0, 0, 0, 0.1);
      border-radius: 5px;
      border: 1px solid rgba(255, 255, 255, 0.1);
      -webkit-appearance: none;
      padding: 12px 5px 12px 15px;
      color: $light_gray;
      height: 47px;
      caret-color: #262626;

      &:-webkit-autofill {
        box-shadow: 0 0 0px 1000px $bg inset !important;
        -webkit-text-fill-color: #262626 !important;
      }
    }
  }

  .el-form-item {
    label {
      width: 150px !important;
      font-style: normal;
      font-size: 15px;
      color: $light_gray;
    }
  }
}
</style>

<style lang="scss" scoped>
$bg: #262626;
$dark_gray: #262626;
$light_gray: #464545;

.register-container {
  width: 100%;
  height: 100%;
  background-image: url("../../assets/login/back2.png");
  background-size: cover;
  background-position: center;
  position: relative;

  .header {
    height: 50px;
    background: #343434;
    border-bottom: 2px solid rgb(45, 45, 45);
    min-width: 980px;
    color: #ffffff;

    header {
      margin: 0 auto;
      width: 980px;

      .login {
        float: right;
      }

      .bold {
        font-style: normal;
        color: #ffffff;
      }
    }
  }

  > section {
    position: relative;
    margin: 0 auto 100px;
    padding-top: 30px;
    width: 500px;
    left: 200px;
    top:150px;
    height: 300px;
    box-sizing: border-box;

    .status {
      font-size: 12px;
      margin-left: 20px;
      color: #e6a23c;
    }

    .error {
      color: red;
    }
  }

  .tips {
    float: right;
    font-size: 14px;
    color: #2d2d2d;
    margin-bottom: 10px;

    span {
      &:first-of-type {
        margin-right: 16px;
      }
    }
  }
}
</style>

