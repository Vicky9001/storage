package com.example.bdd.auto;

import com.example.bdd.check.CheckStepRunner;
import com.example.bdd.stokeIn.StokeInStepRunner;
import com.example.bdd.stokeOut.OutStepRunner;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CheckStepRunner.class,
        StokeInStepRunner.class,
        OutStepRunner.class
})
public class WarehousingTestSuite {
    // 这里不需要添加任何内容
}