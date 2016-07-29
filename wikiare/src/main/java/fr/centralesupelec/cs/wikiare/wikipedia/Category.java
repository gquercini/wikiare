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

import java.util.Set;

import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Values;

/**
 * A Wikipedia category.
 *
 */
public class Category extends Page {
	
	/**
	 * The number of categories that are children of this category.
	 */
	private int children;
	
	/**
	 * The number of articles that this category contains.
	 */
	private int size;
	
	/**
	 * Creates a new category.
	 * @param driver The driver used to connect to the underlying Neo4j database.
	 * @param nodeIdentifier The identifier in the underlying Neo4j database of the node
	 * corresponding to the new category.
	 */
	protected Category(Driver driver, long nodeIdentifier) {
		super(driver, nodeIdentifier);
		this.children = -1;
		this.size = -1;
	}
	
	/**
	 * Loads the attributes of this article from the underlying Neo4j database.
	 */
	private void loadAttributesFromNeo4j() {
		if (this.loadAttributes()) {
			Session session = driver().session();
			StatementResult result = session.run("MATCH (n:Category) WHERE id(n)={node-identifier} return n.title, n.lang, n.wikiid, n.parents, n.children, "
					+ "n.size", 
					Values.parameters("node-identifier", this.nodeIdentifier()));
			for ( Record record : result.list() ) {
				super.title(record.get("n.title").asString());
				super.language(record.get("n.lang").asString());
				super.wikiid(record.get("n.wikiid").asString());
				super.parents(record.get("n.parents").asInt());
				this.children = record.get("n.children").asInt();
				this.size = record.get("n.size").asInt();
			}
			session.close();
			this.loadAttributes(false);
		}
	}

	
	/**
	 * Returns the number of categories of which this category is parent.
	 * @return The number of categories of which this category is parent.
	 */
	public int children() {
		loadAttributesFromNeo4j();
		return this.children;
	}
	
	/**
	 * Sets the number of categories of which this category is parent.
	 * @param children The number of categories of which this category is parent.
	 */
	public void children(int children) {
		this.children = children;
	}
	
	/**
	 * Returns the number of articles that this category contains.
	 * @return The number of articles that this category contains.
	 */
	public int size() {
		loadAttributesFromNeo4j();
		return this.size;
	}
	
	/**
	 * Sets the number of articles that this category contains.
	 * @param size The number of articles that this category contains.
	 */
	public void size(int size) {
		this.size = size;
	}
	
	/**
	 * Returns the parent categories of this category.
	 * @return The parent categories of this category.
	 */
	public Set<Category> parentCategories() {
		PageFactory factory = new PageFactory(driver());
		return factory.createCategories("MATCH (n:Category)-[:belongTo]->(m:Category) WHERE id(n)={id-cat}"
				+ " RETURN m as target-node", Values.parameters("id-cat", nodeIdentifier()), "target-node");
	}
	
	/**
	 * Returns the categories of which this category is the parent.
	 * @return The categories of which this category is the parent.
	 */
	public Set<Category> childrenCategories() {
		PageFactory factory = new PageFactory(driver());
		return factory.createCategories("MATCH (n:Category)<-[:belongTo]-(m:Category) WHERE id(n)={id-cat}"
				+ " RETURN m as target-node", Values.parameters("id-cat", nodeIdentifier()), "target-node");
	}
	
	/**
	 * Returns the categories that have a cross-link to this category.
	 * @return The categories that have a cross-link to this category.
	 */
	public Set<Category> getCrossLinkedCategories() {
		PageFactory factory = new PageFactory(driver());
		return factory.createCategories("MATCH (n:Category)-[:crosslink]->(m:Category) WHERE id(n)={id-cat}"
				+ " RETURN m as target-node", Values.parameters("id-cat", nodeIdentifier()), "target-node");
	}
	
	/**
	 * Returns the categories in the specified language that have a cross-link to this category
	 * @param language The code of the language of the target categories. 
	 * @return The categories in the specified language that have a cross-link to this category
	 */
	public Set<Category> getCrossLinkedCategories(String language) {
		PageFactory factory = new PageFactory(driver());
		return factory.createCategories("MATCH (n:Category)-[:crosslink]->(m:Category) WHERE id(n)={id-cat} "
				+ "AND m.lang={lang} "
				+ " RETURN m as target-node", Values.parameters("id-cat", nodeIdentifier(), "lang", language), "target-node");
	}
	
	/**
	 * Returns the categories in the specified languages that have a cross-link to this category.
	 * @param languages The languages of the target categories.
	 * @return The categories in the specified languages that have a cross-link to this category.
	 */
	public Set<Category> getCrossLinkedCategories(String[] languages) {
		// TODO
		return null;
	}

}
