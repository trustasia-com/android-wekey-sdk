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
        implementation 'com.github.trustasia-com:android-wekey-sdk:Tag'
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

## Issues
[Opening an Issue](https://github.com/trustasia-com/android-wekey-sdk/issues/new), Issues not conforming to the guidelines may be closed immediately.
