/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.want.dubbo.api.service;

import com.want.dubbo.api.domain.entity.User;

import java.util.concurrent.CompletableFuture;

/**
 * {@link User} Service.
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 */
public interface UserService {

	/**
	 * 获取用户的信息
	 * @param uid
	 * @return
	 */
	User info(Integer uid);

	/**
	 * async获取用户的信息
	 * @param uid
	 * @return
	 */
	default CompletableFuture<User> info(Integer uid, Boolean sync){
		return CompletableFuture.completedFuture(info(uid));
	}
}
