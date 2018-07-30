package org.axonframework.samples.trader.query.kds.repositories;

import org.axonframework.samples.trader.api.kds.StallId;
import org.axonframework.samples.trader.infra.config.PersistenceInfrastructureConfig;
import org.axonframework.samples.trader.query.config.HsqlDbConfiguration;
import org.axonframework.samples.trader.query.kds.StallView;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author luoqi
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PersistenceInfrastructureConfig.class, HsqlDbConfiguration.class})
@ActiveProfiles("hsqldb")
public class StallRepositoryIntegrationTest {

    @Autowired
    private StallViewRepository stallRepository;

    @Test
    public void storeStallInRepository() {
        StallView stallView = new StallView();
        stallView.setIdentifier(new StallId().toString());
        stallView.setAbility(1);
        stallView.setRequirements("烹饪");
        stallView.setRemark("档口1");
        stallView.setPoiId(2);
        stallView.setStallName("档口1");

        stallRepository.save(stallView);
    }
}
