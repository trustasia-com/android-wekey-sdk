## Installation

Step 1. Add it in your root build.gradle at the end of repositories:

```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

Step 2. Add the dependency
```groovy
dependencies {
        implementation 'com.github.trustasia-com:android-wekey-sdk:v1.0.1'
}
```

## Quick Examples

The following code example shows the three main steps to use this SDK :

1. Call `FIDOClient.getInstance().init(RP_BASE_URL)` to initial SDK

2. Call `make` or `get` API request and set parameters.

3. Handle the API response in callback


```java
public interface IWebauthn {

    void init(String url);

    /**
     * 发起make请求并自动调用api返回rp
     *
     * @param activity 弹出层上下文
     */
    void make(FragmentActivity activity, String userName, String displayName, boolean rk, Callback<String> callback);


    /**
     * 发起get请求并自动调用api返回rp
     *
     * @param activity 弹出层上下文
     */
    void get(FragmentActivity activity, String userName, String displayName, boolean rk, Callback<String> callback);
}
```

Make a get request
```java
FIDOClient.getInstance().get(this, userName, displayName, rk, new Callback<String>() {
    @Override
    public void onResp(String data) {
        //handle rp server data
    }

    @Override
    public void onError(Throwable throwable) {
        //handle error
    }
});
```

## Error Code
This sdk use `onActivityResult(int requestCode, int resultCode, @Nullable Intent data)` to pass error code.
The following shows the code mapping table:

| Code        | Description |
| ----------- | ----------- |
| -1          | RESULT_OK         |
| 0           | RESULT_CANCEL     |
| 400         | Bad Request       |
| 403         | Forbidden         |
| 404         | Not Found         |
| 0x01        | INVALID_COMMAND   |
| 0x02        | INVALID_PARAMETER |
| 0x03        | INVALID_LENGTH    |
| 0x11        | CBOR_UNEXPECTED_TYPE |
| 0x12        | INVALID_CBOR |
| 0x14        | MISSING_PARAMETER |
| 0x16        | UNSUPPORTED_EXTENSION |
| 0x19        | CREDENTIAL_EXCLUDED |
| 0x26        | UNSUPPORTED_ALGORITHM |
| 0x27        | OPERATION_DENIED |
| 0x28        | UNSUPPORTED_OPTION |
| 0x2c        | INVALID_OPTION |
| 0x20        | KEEP_ALIVE_CANCEL |
| 0x2E        | NO_CREDENTIALS |
| 0x2F        | USER_ACTION_TIMEOUT |
| 0x30        | NOT_ALLOWED |
| 0x31        | PIN_INVALID |
| 0x32        | PIN_BLOCKED |
| 0x30        | NOT_ALLOWED |
| 0x33        | PIN_AUTH_INVALID |
| 0x34        | PIN_AUTH_BLOCKED |
| 0x35        | PIN_NOT_SET |
| 0x36        | PIN_REQUIRED |
| 0x37        | PIN_POLICY_VIOLATION |
| 0x38        | PIN_TOKEN_EXPIRED |
| 0x39        | REQUEST_TOO_LARGE |
| 0x3c        | ERR_UV_BLOCKED |
| 0x7F        | OTHER |


## Issues
[Opening an Issue](https://github.com/trustasia-com/android-wekey-sdk/issues/new), Issues not conforming to the guidelines may be closed immediately.
