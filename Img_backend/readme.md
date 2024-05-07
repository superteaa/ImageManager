# Logup

## 接口描述
此接口用于用户注册，创建新的用户账户。

## 请求 URL
- `175.178.24.103:3388/logup`

## 请求方式
- POST

## 参数

| 参数名   | 必选 | 类型   | 说明     |
| -------- | ---- | ------ | -------- |
| username | 是   | string | 用户名   |
| password | 是   | string | 密码     |
| email    | 是   | string | 电子邮箱 |

## 请求示例
```json
{
  "username": "new_user",
  "password": "password123",
  "email": "new_user@example.com"
}
```

## 成功响应示例

- **成功注册新用户：**

JSON

```json
"success"
```

- **用户已存在：**

JSON

```json
"用户已存在！"
```

- **没有数据被插入：**

JSON

```json
"没有数据被插入！"
```



# Login

## 接口描述
此接口用于用户登录，验证用户名和密码。

## 请求 URL
- `175.178.24.103:3388/login`

## 请求方式
- POST

## 参数

| 参数名   | 必选 | 类型   | 说明   |
| -------- | ---- | ------ | ------ |
| username | 是   | string | 用户名 |
| password | 是   | string | 密码   |

## 请求示例
```json
{
  "username": "user_example",
  "password": "password_example"
}
```

## 成功响应示例

- **登录成功：**

JSON

```json
"登陆成功！"
```

- **用户名或密码出错：**

JSON

```json
"用户名或密码出错！"
```



# Read Image Files

## 接口描述
此接口用于读取指定用户目录下的所有图片文件，并返回图片文件的URL列表。

## 请求 URL
- `175.178.24.103:3388/images/read`

## 请求方式
- POST

## 参数

| 参数名   | 必选 | 类型   | 说明   |
| -------- | ---- | ------ | ------ |
| username | 是   | string | 用户名 |

## 请求示例
```json
{
  "username": "example_user"
}
```

## 成功响应示例

- **返回图片文件URL列表：**

JSON

```json
[
  "http://175.178.24.103:3388/pic/example_user/image1.jpg",
  "http://175.178.24.103:3388/pic/example_user/image2.png",
  ...
]
```

## 错误响应示例

- **读取文件错误：**

```plaintext
Error reading files: [错误信息]
```