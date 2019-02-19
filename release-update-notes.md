#### v1.0.6
- 增加汇率换算API: `getExchangeRate`，详情请参考文档
- 使用spongycastle替换bouncycastle，缩短在Android环境下地址生成算法的时间为原来的1/3
- `GitiumApi`可使用`GitiumApi.Builder`构建。Android环境下可通过`setAddressCreateCount`设置每次生成地址的个数为1-10之间(默认为100)
#### v1.0.5
- Base64算法引用第三方，以兼容Android系统
#### v1.0.4
- 修改由于充值币种引起的充值失败情况
#### v1.0.3
- `ChargeRecord`增加status字段
#### v1.0.2
- 增加获取用户第一个地址API：`getFirstAddress`
- 增加获取充值币种列表API：`getChargeCurrencyList`
- 增加充值API：`charge`
- 增加获取充值历史列表API：`getChargeCurrencyList`
