import {
  ACCOUNT_MIN_LEN,
  ACCOUNT_MAX_LEN,
  PASSWORD_MIN_LEN,
  PASSWORD_MAX_LEN
} from '@/constant/account_rules'

// LoginAccount.vue 中 form 表单规则
const rules = {
  userAccount: [
    {
      required: true,
      message: '账号不能为空',
      trigger: 'blur'
    },
    {
      pattern: new RegExp(
        '^[A-Za-z0-9]{' + ACCOUNT_MIN_LEN + ',' + ACCOUNT_MAX_LEN + '}$'
      ),
      message: `账号是 ${ACCOUNT_MIN_LEN}~${ACCOUNT_MAX_LEN} 位字母或数字`,
      trigger: 'blur'
    }
  ],
  userPassword: [
    {
      required: true,
      message: '密码不能为空',
      trigger: 'blur'
    },
    {
      pattern: new RegExp(
        '^[A-Za-z0-9]{' + PASSWORD_MIN_LEN + ',' + PASSWORD_MAX_LEN + '}$'
      ),
      message: `密码必须是 ${PASSWORD_MIN_LEN}~${PASSWORD_MAX_LEN} 位字符`,
      trigger: 'blur'
    }
  ],
  checkPassword: [
    {
      required: true,
      message: '重复密码不能为空',
      trigger: 'blur'
    },
    {
      pattern: new RegExp(
        '^[A-Za-z0-9]{' + PASSWORD_MIN_LEN + ',' + PASSWORD_MAX_LEN + '}$'
      ),
      message: `重复密码必须是 ${PASSWORD_MIN_LEN}~${PASSWORD_MAX_LEN} 位字符`,
      trigger: 'blur'
    }
  ]
}

export { rules }
