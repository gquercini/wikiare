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
 * A Wikipedia page.
 *
 */
public abstract class Page {
	
	/**
	 * The driver used to connect to the underlying Neo4j database.
	 */
	private Driver driver;
	
	/**
	 * The identifier of the node in the Neo4j database that corresponds to this page.
	 */
	private long nodeIdentifier;
	
	/**
	 * Whether the attributes of this page need to be loaded to memory from the underlying Neo4j database.
	 * The first time a page is loaded from the underlying Neo4j database, only its identifier is 
	 * obtained (this to reduce the response time).
	 * The first time an attribute of this page is requested, all its attributes are loaded to memory and
	 * the field loadAttirbutes is set to false.    
	 */
	private boolean loadAttributes;
	
	/**
	 * The title of this page.
	 */
	private String title;
	
	/**
	 * The language of the Wikipedia edition of this page.
	 */
	private String language;
	
	/**
	 * The identifier in Wikipedia of this page.
	 */
	private String wikiid;
	
	/**
	 * The number of categories that are parent of this page.
	 */
	private int parents;
	
	/**
	 * Initializes the fields of the new page.
	 * @param driver The driver used to connect to the underlying Neo4j database.
	 * @param nodeIdentifier The identifier in the underlying Neo4j database of the node
	 * corresponding to the new page.
	 */
	protected Page(Driver driver, long nodeIdentifier) {
		this.driver = driver;
		this.nodeIdentifier = nodeIdentifier;
		this.title = null;
		this.language = null;
		this.wikiid = null;
		this.parents = -1;
	}
	
	
	/**
	 * Returns the title of this page.
	 * @return The title of this page.
	 */
	public String title() {
		return this.title;
	}
	
	/**
	 * Sets the title of this page.
	 * @param title The title of this page.
	 */
	public void title(String title) {
		this.title = title;
	}
	
	/**
	 * Returns the code of the language of the Wikipedia edition to which this page belongs.
	 * @return The code of the language of the Wikipedia edition to which this page belongs.
	 */
	public String language() {
		return this.language;
	}
	
	/**
	 * Sets the code of the language of the Wikipedia edition of this page. 
	 * @param language The code of the language of the Wikipedia edition of this page.
	 */
	public void language(String language) {
		this.language = language;
	}
	
	/**
	 * Returns the Wikipedia identifier of this page.
	 * @return The Wikipedia identifier of this page.
	 */
	public String wikiid() {
		return this.wikiid;
	}
	
	/**
	 * Sets the Wikipedia identifier of this page.
	 * @param wikiid The Wikipedia identifier of this page.
	 */
	public void wikiid(String wikiid) {
		this.wikiid = wikiid;
	}
	
	/**
	 * Returns the number of categories that contain this page.
	 * @return The number of categories that contain this page.
	 */
	public int parents() {
		return this.parents;
	}
	
	/**
	 * Sets the number of categories that contain this page.
	 * @param parents The number of categories that contain this page.
	 */
	public void parents(int parents){
		this.parents = parents;
	}

	/**
	 * Returns the driver used to connect to the underlying Neo4j database.
	 * @return The driver used to connect to the underlying Neo4j database.
	 */
	protected Driver driver() {
		return this.driver;
	}
	
	/**
	 * Returns whether the attributes of this page need to be loaded from the underlying Neo4j database.
	 * @return {@code true} if the attributes of this page need to be loaded from the underlying Neo4j database,
	 * {@code false} otherwise.
	 */
	protected boolean loadAttributes() {
		return this.loadAttributes;
	}
	
	/**
	 * Sets whether the attributes of this page need to be loaded from the underlying Neo4j database.
	 * @param loadAttributes {@code true} if the the attributes of this page need to be loaded from the underlying Neo4j database,
	 * {@code false} otherwise.
	 */
	protected void loadAttributes(boolean loadAttributes) {
		this.loadAttributes = loadAttributes;
	}
	
	
	/**
	 * Returns the identifier of the node in the underyling Neo4j database that corresponds to this page.
	 * @return The identifier of the node in the underyling Neo4j database that corresponds to this page.
	 */
	protected long nodeIdentifier() {
		return this.nodeIdentifier;
	}
	
	/**
	 * Returns whether this page is a redirect.
	 * @return {@code true} if this page is a redirect, {@code false} otherwise (default).
	 */
	public boolean isRedirect() {
		return false;
	}
	
	/**
	 * Returns whether this page is a disambiguation.
	 * @return {@code true} if this page is a disambiguation page, {@code false} otherwise (default).
	 */
	public boolean isDisambiguation() {
		return false;
	}
	

}
