/********************************************************************************
 * Copyright (c) 2019-2020 [Open Lowcode SAS](https://openlowcode.com/)
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0 .
 *
 * SPDX-License-Identifier: EPL-2.0
 ********************************************************************************/

package org.openlowcode.design.data;

import org.openlowcode.tools.misc.Named;

/**
 * Argument to a data access method
 * 
 * @author <a href="https://openlowcode.com/" rel="nofollow">Open Lowcode
 *         SAS</a>
 *
 */
public class MethodArgument
		extends
		Named {

	private ArgumentContent content;

	/**
	 * creates a method argument with the given name and content
	 * 
	 * @param name    name of the argument
	 * @param content payload of the argument
	 */
	public MethodArgument(String name, ArgumentContent content) {
		super(name);
		this.content = content;
	}

	/**
	 * @return get the payload
	 */
	public ArgumentContent getContent() {
		return this.content;
	}
}
