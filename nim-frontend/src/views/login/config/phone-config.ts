// LoginAccount.vue 中 form 表单规则
const rules = {
  number: [
    {
      required: true,
      message: '手机号不能为空',
      trigger: 'blur'
    },
    {
      pattern:
        /^(0|\+?86|17951)?(13[0-9]|15[012356789]|17[013678]|18[0-9]|14[57])[0-9]{8}$/,
      message: '请输入正确格式的手机号',
      trigger: 'blur'
    }
  ],
  code: [
    {
      required: true,
      message: '请输入验证码',
      trigger: 'blur'
    },
    {
      min: 6,
      max: 6,
      message: '请输入6位验证码',
      trigger: 'blur'
    }
  ]
}

export { rules }
