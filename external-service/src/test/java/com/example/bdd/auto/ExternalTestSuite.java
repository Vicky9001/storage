package com.example.bdd.auto;

import com.example.bdd.outBound.OutBoundStepRunner;
import com.example.bdd.purchase.PurchaseStepRunner;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({
        PurchaseStepRunner.class,
        OutBoundStepRunner.class,
})
public class ExternalTestSuite {

}