/*
 * Copyright (c) 2010-2012. Axon Framework
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

package org.axonframework.samples.trader.webui.init;

import org.cloudfoundry.runtime.env.CloudEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * <p>Class used by the spring container to set the active profile. The profiles are used to set environment specific
 * beans like the mongo factory for connecting to the mongo database.</p>
 * <p>We detect whether we can find an active CloudFoundry environment, if so we set the cloud profile as the active
 * profile. If not, we set the default profile as the active one.</p>
 *
 * @author Jettro Coenradie
 */
public class CloudApplicationContextInitializer
        implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private final static Logger logger = LoggerFactory.getLogger(CloudApplicationContextInitializer.class);

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        CloudEnvironment env = new CloudEnvironment();
        if (env.getInstanceInfo() != null) {
            logger.info("Cloud API: {}", env.getCloudApiUri());
            applicationContext.getEnvironment().setActiveProfiles("cloud");
        } else {
            logger.info("Activating the default profile within the application context.");
            applicationContext.getEnvironment().setActiveProfiles("default");
        }
    }
}
