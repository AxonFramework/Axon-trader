package org.axonframework.samples.trader.query.kds.repositories;

import org.axonframework.samples.trader.query.kds.StallView;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StallViewRepository extends JpaRepository<StallView, String> {
}
