package com.ilgrig.tuum.config;

import com.ilgrig.tuum.TuumApplication;
import com.ilgrig.tuum.mapper.AccountMapper;
import com.ilgrig.tuum.mapper.BalanceMapper;
import com.ilgrig.tuum.mapper.TransactionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(
        classes = TuumApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ContextConfiguration(classes = {TuumApplication.class, RabbitMockConfig.class})
@SqlGroup({
        @Sql("/data/clearAll.sql"),
        @Sql("/data/insertAccounts.sql"),
        @Sql("/data/insertBalances.sql"),
        @Sql("/data/insertTransactions.sql")
})
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@Testcontainers
public abstract class BaseIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:16.2")
                    .withReuse(true);

    static {
        postgreSQLContainer.start();
    }

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected AccountMapper accountMapper;

    @Autowired
    protected BalanceMapper balanceMapper;

    @Autowired
    protected TransactionMapper transactionMapper;
}
