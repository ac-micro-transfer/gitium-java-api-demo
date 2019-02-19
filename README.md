# Gitium Java Api Demo
版本更新历史
------
[查看版本更新历史记录](https://github.com/ac-micro-transfer/gitium-java-api-demo/blob/master/release-update-notes.md)

下载Jar包
------
最新完整Jar包：[v1.0.6](https://github.com/ac-micro-transfer/gitium-java-api-demo/raw/libs/gitium-java-api_1.0.6_full.jar)

Maven依赖
------
```xml
<repositories>
  ...
  <repository>
    <id>gitium-maven</id>
    <url>https://raw.githubusercontent.com/ac-micro-transfer/mvn/master/repository/releases</url>
  </repository>
  ...
</repositories>
<dependencies>
  ...
  <dependency>
    <groupId>com.acbooking</groupId>
    <artifactId>gitium-java-api</artifactId>
    <version>1.0.6</version>
  </dependency>
  ...
</dependencies>
```
Gradle依赖
------
```groovy
repositories {
  maven { url "https://raw.githubusercontent.com/ac-micro-transfer/mvn/master/repository/releases" }
}

dependencies {
  implementation "com.acbooking:gitium-java-api:1.0.6"
}
```
快速开始
------
#### 1. 关于Seed
Seed是Gitium系统内用户的唯一标识，可以在Gitium系统内做包括转账在内的任意操作。所以要保证Seed的绝对安全，不要在任何的数据传输层暴露出来。
#### 2. Seed生成
可以通过以下代码生成(我们的应用生成Seed的规则)：
```java
import com.gitium.api.SeedCreator;

String username = "username1";
String password = "password1";
String seed = SeedCreator.newSeed(username, password);
```
或者根据以下规则生成：

`只包含`大写字母`A-Z`和`9`的`81位`随机字符串

#### 3. 关于地址
地址是根据用户的Seed生成的，转账的时候是将用户有余额的地址列表里的余额转给第三方，将剩余的金额转入新地址。

#### 4. 关于收款地址
收款地址是`动态`的，在需要的时候通过`getNewestAddress`去获取。收款地址一般绑定的时机是在可预见未来会收款的业务场景中，在业务发起时就进行绑定。比如在抽奖的场景中，用户在发出抽奖指令的时候就应该将用户的收款地址绑定到业务的记录里，而不是在用户中奖后在转账前再去绑定。相当于在业务流程中，永远是`在以收款方为主体的步骤去绑定收款地址`。

#### 5. API调用入口
```java
import com.gitium.api.GitiumApi;

String url = "api url,比如http://106.15.251.200";
GitiumApi api = new GitiumApi(url);
```
or
```java
import com.gitium.api.GitiumApi;

String url = "api url,比如http://106.15.251.200";
GitiumApi api = new GitiumApi.Builder(url)
    .setAddressCreateCount(100)//设置每次生成地址的个数，Android建议在1-10之间。
    .build();
```
由于流程中http请求比较多，建议使用`单例模式`, 当地址较多的时候效率差距比较明显。

API文档
------
#### 1. 获取用户所有地址列表
```java
DataWithStatus<List<String>> getAddressList(String seed)
```
> 参数  
>> seed: 用户身份  

> 返回值  
>> status: -1 -> 获取失败; 1 -> 获取成功  
>> data: 地址列表，只有status为1时返回  
#### 2. 获取用户最新地址
```java
DataWithStatus<String> getNewestAddress(String seed)
```
> 参数  
>> seed: 用户身份  

> 返回值  
>> status: -1 -> 获取失败; 1 -> 获取成功  
>> data: 地址，只有status为1时返回  
#### 3. 获取用户所有交易记录
```java
DataWithStatus<List<GitiumTransaction>> getGitiumTransactions(String seed)
```
> 参数  
>> seed: 用户身份  

> 返回值  
>> status: -1 -> 获取失败; 1 -> 获取成功  
>> data: 交易记录列表，只有status为1时返回  
#### 4. 获取用户总资产
```java
DataWithStatus<Long> getTotalBalance(String seed)
```
> 参数  
>> seed: 用户身份  

> 返回值  
>> status: -1 -> 获取失败; 1 -> 获取成功  
>> data: 用户总资产，只有status为1时返回  
#### 5. 转账
```java
DataWithStatus<String> transfer(String seed, String targetAddress, long value)
```
> 参数  
>> seed: 用户身份  
>> targetAddress: 收款方收款地址  
>> value: 转账金额，不支持小数点  

> 返回值  
>> status: -1 -> 转账提交失败; 1 -> 转账提交成功  
>> data: 此次转账提交成功的唯一标识Hash，只有status为1时返回。记录下来可以用于查询转账状态。  
#### 6. 查询转账状态
```java
DataWithStatus<QueryTransaction> getTransactionByHash(String hash)
```
> 参数  
>> hash: 查询转账状态的唯一标识  

> 返回值  
>> status: -1 -> 获取失败; 1 -> 获取成功; -2 -> 查无该条记录  
>> data: 查询到的转账记录，只有status为1时返回。  
>>> `QueryTransaction`关键字段  
>>>> status: `0 待验证` `1 已验证且交易成功` `-1 已验证且交易失败`  
#### 7.获取用户第一个地址
```java
DataWithStatus<String> getFirstAddress(String seed)
```
> 参数  
>> seed: 用户身份  

> 返回值  
>> status: -1 -> 获取失败; 1 -> 获取成功  
>> data: 用户第一个地址  
#### 8.获取充值币种列表
```java
DataWithStatus<List<ChargeCurrency>> getChargeCurrencyList()
```
> 返回值  
>> status: -1 -> 获取失败; 1 -> 获取成功  
>> data: `ChargeCurrency`列表，只有status为1时返回。  
>>> `ChargeCurrency`字段描述  
>>>> id: 币种ID  
>>>> name: 币种名称  
>>>> symbol: 币种符号  
>>>> address: 币种对应的平台钱包地址  
>>>> rate:币种相对Git的汇率  
>>>> up: 充值上限  
>>>> down: 充值下限  
>>>> upStr: 充值上限字符串形式  
>>>> downStr: 充值下限字符串形式  
#### 9.充值
```java
DataWithStatus<Object> charge(String seed, String payCurrency, double payValue, String userPayAddress, long chargeValue, String receivedAddress, String phone)
```
> 参数  
>> seed: 用户身份  
>> payCurrency: 支付币种名称  
>> payValue: 支付金额  
>> userPayAddress: 用户支付币种钱包地址  
>> chargeValue: 待充值金额（参考，以实际充值时的汇率为准）  
>> receivedAddress: 充值接收地址，用户填写的接收地址  
>> phone: 用户的联系方式  

> 返回值  
>> status: 1 -> 充值意向提交成功; -1 -> 充值意向提交失败  
#### 10.获取充值历史列表
```java
DataWithStatus<ChargeListData> getChargeList(String seed, int currentPage, int pageSize)
```
> 参数  
>> seed: 用户身份  
>> currentPage: 当前页的索引，从1开始  
>> pageSize: 每页数据条数  

> 返回值  
>> status: -1 -> 获取失败; 1 -> 获取成功  
>> data: `ChargeListData`, 只有status为1时返回  
>>> ChargeListData字段  
>>>> totalPage: 总页数  
>>>> chargeList: `ChargeRecord`列表  
>>>>> `ChargeRecord`字段  
>>>>>> id: 充值记录ID  
>>>>>> createDate: 充值记录创建时间  
>>>>>> payCurrency: 支付币种  
>>>>>> payValue: 支付金额  
>>>>>> userPayAddress: 支付钱包地址  
>>>>>> chargeValue: 充值金额  
>>>>>> chargeAddress: 充值用户第一个地址  
>>>>>> receivedAddress: 接收地址  
>>>>>> phone: 联系方式  
>>>>>> status: 状态 `1等待转账` `3确认充值中` `4交易完成` `5交易正在处理` `6交易失败`
#### 11.汇率换算
```java
DataWithStatus<BigDecimal> getExchangeRate(String currency, String referCurrency)
```
> 参数  
>> currency: 币种  
>> referCurrency: 参照币种  

> 返回值  
>> status: 1 -> 获取成功; -1 -> 获取失败  
>> data: 汇率，`N currency = 1 referCurrency`  

> 备注  
>> 由于只精确到小数点后5位，有较大可能值为0，建议把实际价值较大的币种作为referCurrency，这样汇率的整数位较大。比如GIT的汇率很小，如果放在referCurrency，而currency为BTC，这样得到的汇率就是0.00000了。
