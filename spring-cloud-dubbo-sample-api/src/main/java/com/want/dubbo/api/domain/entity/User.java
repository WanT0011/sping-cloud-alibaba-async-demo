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

package com.want.dubbo.api.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * User Entity.
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 */
@Data
@NoArgsConstructor
public class User implements Serializable {

	private Integer id;

	private String name;

	private Long money;


	public User(Integer id) {
		this.id = id;
		this.name = "用户-"+id;
		this.money =100 * 1000 * 1000L;
	}
}
