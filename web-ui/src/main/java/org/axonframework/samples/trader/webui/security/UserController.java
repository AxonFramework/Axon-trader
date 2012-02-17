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

package org.axonframework.samples.trader.webui.security;

import org.axonframework.samples.trader.query.users.repositories.UserQueryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Jettro Coenradie
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private UserQueryRepository userRepository;

    @Autowired
    public UserController(UserQueryRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showUsers(Model model) {
        model.addAttribute("items", userRepository.findAll());
        return "user/list";
    }

    @RequestMapping(value = "/{identifier}", method = RequestMethod.GET)
    public String detail(@PathVariable("identifier") String userIdentifier, Model model) {
        model.addAttribute("item", userRepository.findByIdentifier(userIdentifier));
        return "user/detail";
    }
}
