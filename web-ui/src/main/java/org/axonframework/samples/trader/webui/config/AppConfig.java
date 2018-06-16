/*
 * Copyright (c) 2010-2016. Axon Framework
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.axonframework.samples.trader.webui.config;

import org.axonframework.samples.trader.company.config.CompanyConfig;
import org.axonframework.samples.trader.infra.config.CQRSInfrastructureConfig;
import org.axonframework.samples.trader.infra.config.PersistenceInfrastructureConfig;
import org.axonframework.samples.trader.listener.config.ExternalListenersConfig;
import org.axonframework.samples.trader.orders.config.OrderConfig;
import org.axonframework.samples.trader.query.config.QueryConfig;
import org.axonframework.samples.trader.tradeengine.config.TradeEngineConfig;
import org.axonframework.samples.trader.users.config.UserConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

@Configuration
@Import({
        CQRSInfrastructureConfig.class,
        PersistenceInfrastructureConfig.class,
        TradeEngineConfig.class,
        CompanyConfig.class,
        OrderConfig.class,
        ExternalListenersConfig.class,
        UserConfig.class,
        QueryConfig.class
})
@ImportResource("classpath:META-INF/spring/security-context.xml")
public class AppConfig {
}
