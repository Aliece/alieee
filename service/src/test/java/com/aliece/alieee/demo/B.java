/*
 * Copyright 2003-2009 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package com.aliece.alieee.demo;

import com.aliece.alieee.annotation.Model;
import com.aliece.alieee.annotation.model.Inject;
import com.aliece.alieee.annotation.model.OnCommand;
import com.aliece.alieee.demo.publisher.EventSourcing;
import com.aliece.alieee.domain.message.DomainMessage;

@Model
public class B {

	@Inject
	public EventSourcing es;

	@OnCommand("CommandtoEventA")
	public void commandA(ParameterVO vo) throws Exception {
		this.es.created(vo);

		System.out.print("event.@OnEvent.mb.." + vo.getValue() + "\n");

	}
}
