/*
 * Copyright (c) 2010. Gridshore
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

package org.axonframework.samples.trader.tradeengine.api.order;

import org.axonframework.domain.AggregateIdentifier;

/**
 * <p>Create a new OrderBook for the Company represented by the provided companyIdentifier.</p>
 *
 * @author Jettro Coenradie
 */
public class CreateOrderBookCommand {
    private AggregateIdentifier companyIdentifier;

    public CreateOrderBookCommand(AggregateIdentifier companyIdentifier) {
        this.companyIdentifier = companyIdentifier;
    }

    public AggregateIdentifier getCompanyIdentifier() {
        return companyIdentifier;
    }
}
