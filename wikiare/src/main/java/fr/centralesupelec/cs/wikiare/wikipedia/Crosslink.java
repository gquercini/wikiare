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
 * A cross-link between two Wikipedia pages belonging to two different Wikipedia
 * language editions.
 *
 */
public class Crosslink extends Link {

	/**
	 * Creates a new cross-link.
	 *  @param driver The driver used to connect to the underlying Neo4j database.
	 * @param source The source page.
	 * @param target The target page.
	 */
	Crosslink(Driver driver, Page source, Page target) {
		super(driver, source, target);
	}

}
