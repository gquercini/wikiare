//
// Copyright (c) 2016 Gianluca Quercini
//
// Permission is hereby granted, free of charge, to any person obtaining a
// copy of this software and associated documentation files (the "Software"),
// to deal in the Software without restriction, including without limitation
// the rights to use, copy, modify, merge, publish, distribute, sublicense,
// and/or sell copies of the Software, and to permit persons to whom the
// Software is furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included
// in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
// THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR
// OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
// ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
// OTHER DEALINGS IN THE SOFTWARE.
//
package fr.centralesupelec.cs.wikiare.wikipedia;

import org.neo4j.driver.v1.Driver;

/**
 * A link between two Wikipedia pages.
 *
 */
public abstract class Link {
	
	/**
	 * The driver used to connect to the underlying Neo4j database.
	 */
	private Driver driver;
	
	/**
	 * The source page.
	 */
	private Page source;
	
	/**
	 * The target page.
	 */
	private Page target;
	
	/**
	 * Creates a new link.
	 * @param driver The driver used to connect to the underlying Neo4j database.
	 * @param source The source page of the link.
	 * @param target The target page of the link.
	 */
	Link(Driver driver, Page source, Page target) {
		this.driver = driver;
		this.source = source;
		this.target = target;
	}
	
	/**
	 * Returns the source page of this link.
	 * @return The source page of this link.
	 */
	public Page source() {
		return this.source;
	}
	
	/**
	 * Returns the target page of this link.
	 * @return The target page of this link.
	 */
	public Page target() {
		return this.target;
	}
	
	/**
	 * Returns the driver used to connect to the underlying Neo4j database.
	 * @return The driver used to connect to the underlying Neo4j database.
	 */
	public Driver driver() {
		return this.driver;
	}
}
