let HOST: string
let PORT: string
let PREFIX: string

if (process.env.NODE_ENV === 'development') {
  HOST = '127.0.0.1'
  PORT = '8080'
  PREFIX = '/dev'
} else if (process.env.NODE_ENV === 'production') {
  HOST = '124.223.218.206'
  PORT = '8472'
  PREFIX = '/nim'
} else {
  HOST = '127.0.0.1'
  PORT = '7421'
  PREFIX = '/test'
}

export { HOST, PORT, PREFIX }
