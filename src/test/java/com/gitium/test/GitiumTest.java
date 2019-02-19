package com.gitium.test;

import com.gitium.api.GitiumApi;
import com.gitium.api.SeedCreator;
import com.gitium.api.dto.DataWithStatus;
import com.gitium.api.model.*;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class GitiumTest {

    private static String seed;
    private static GitiumApi api;

    @BeforeClass
    public static void init() {
        try {
            seed = SeedCreator.newSeed("username1", "password1");
            api = new GitiumApi.Builder("http://106.15.251.200")
                    .setAddressCreateCount(100)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetAddressList() {
        DataWithStatus<List<String>> dataWithStatus = api.getAddressList(seed);
        Assert.assertEquals(1, dataWithStatus.getStatus());
    }

    @Test
    public void testFirstAddress() {
        DataWithStatus<String> dataWithStatus = api.getFirstAddress(seed);
        Assert.assertEquals(1, dataWithStatus.getStatus());
    }

    @Test
    public void testNewestAddress() {
        DataWithStatus<String> dataWithStatus = api.getNewestAddress(seed);
        Assert.assertEquals(1, dataWithStatus.getStatus());
    }

    @Test
    public void testGetGitiumTransactions() {
        DataWithStatus<List<GitiumTransaction>> dataWithStatus = api.getGitiumTransactions(seed);
        Assert.assertEquals(1, dataWithStatus.getStatus());
    }

    @Test
    public void testTotalBalance() {
        DataWithStatus<Long> dataWithStatus = api.getTotalBalance(seed);
        Assert.assertEquals(1, dataWithStatus.getStatus());
    }

    @Test
    public void testTransfer() {
        String transferTarget = null;
        try {
            transferTarget = SeedCreator.newSeed("username2", "password2");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(transferTarget != null) {
            DataWithStatus<String> dataWithStatus = api.getNewestAddress(transferTarget);
            if(dataWithStatus.getStatus() == 1) {
                String transferTargetAddress = dataWithStatus.getData();
                DataWithStatus<String> dataWithStatus2 = api.transfer(seed, transferTargetAddress, 1);
                Assert.assertEquals(1, dataWithStatus2.getStatus());
                return;
            }
        }
        Assert.fail();
    }

    @Test
    public void testQueryTransaction() {
        String hash = "YRTAWDCWHDIEBGTZBHYGFIHJJFZKMWNJX9OWUGXJJAKHCGKRWSG9PAWLLFLNDJBVPVXW9HEQCJYEIQJH9";
        DataWithStatus<QueryTransaction> dataWithStatus = api.getTransactionByHash(hash);
        Assert.assertEquals(1, dataWithStatus.getStatus());
    }

    @Test
    public void testGetChargeCurrencyList() {
        DataWithStatus<List<ChargeCurrency>> dataWithStatus = api.getChargeCurrencyList();
        Assert.assertEquals(1, dataWithStatus.getStatus());
    }

    @Test
    public void testCharge() {
        double payValue = 0.01;
        String userPayAddress = "ox123456";
        String phone = "13700000000";

        DataWithStatus<String> receiveAddressData = api.getNewestAddress(seed);
        if(receiveAddressData.getStatus() == 1) {
            String receiveAddress = receiveAddressData.getData();

            DataWithStatus<List<ChargeCurrency>> currencyData = api.getChargeCurrencyList();
            if(currencyData.getStatus() == 1 && !currencyData.getData().isEmpty()) {
                ChargeCurrency chargeCurrency = currencyData.getData().get(0);
                String payCurrency = chargeCurrency.getName();
                long chargeValue = new BigDecimal(payValue).divide(new BigDecimal(chargeCurrency.getRate()), 0, RoundingMode.DOWN).longValue();
                DataWithStatus<Object> dataWithStatus = api.charge(seed, payCurrency, payValue, userPayAddress, chargeValue, receiveAddress, phone);
                Assert.assertEquals(1, dataWithStatus.getStatus());
                return;
            }
        }
        Assert.fail();
    }

    @Test
    public void testGetChargeList() {
        DataWithStatus<ChargeListData> dataWithStatus = api.getChargeList(seed, 1, 20);
        Assert.assertEquals(1, dataWithStatus.getStatus());
    }

    @Test
    public void testGetExchangeRate() {
        DataWithStatus<BigDecimal> dataWithStatus = api.getExchangeRate("GIT", "USD");
        System.out.println(dataWithStatus.getData().toPlainString());
        Assert.assertEquals(1, dataWithStatus.getStatus());
    }

}