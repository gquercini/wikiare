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
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Values;
import org.neo4j.driver.v1.types.Node;

/**
 * This class represents a redirect category.
 * 
 */
public class RedirectCategory extends Category {

	/**
	 * Creates a new redirect category.
	 * @param driver The driver used to connect to the underlying Neo4j database.
	 * @param nodeIdentifier The identifier in the underlying Neo4j database of the node
	 * corresponding to the new article.
	 */
	protected RedirectCategory(Driver driver, long nodeIdentifier) {
		super(driver, nodeIdentifier);
	}
	
	/**
	 * Returns the target category of this redirection.
	 * The result is never a redirect category. 
	 * If a loop of redirects is detected, this method returns {@code null}.
	 * @return The target category of this redirection, or {@code null} if no target category can be determined. 
	 */
	public Category redirectsTo() {
		PageFactory pageFactory = new PageFactory(driver());
		return redirectsTo(nodeIdentifier(), pageFactory);
	}
	
	/**
	 * Auxiliary function of {@code redirectsTo}.
	 * It is invoked recursively on a redirect category until either a non-redirect category is found or a loop of redirects
	 * is detected.
	 * @param initialNode The identifier of the node in the underlying Neo4j database that corresponds to the 
	 * redirect category from which the recursion starts.
	 * @param pageFactory The object used to create categories from Neo4j nodes. 
	 * @return The category that is the target of this redirection, if any, or {@code null} if no target can be found.
	 */
	private Category redirectsTo(long initialNode, PageFactory pageFactory) {
		Category targetCategory = null;
		Session session = driver().session();
		StatementResult result = session.run("MATCH (n:Redirect)-[:redirectTo]->(m:Category) "
				+ "WHERE id(n)={node-identifier} return m as target-node", 
				Values.parameters("node-identifier", this.nodeIdentifier()));
		Record record = result.single();
		Node targetNode = record.get("target-node").asNode();
		targetCategory = pageFactory.createCategory(targetNode);
		session.close();
		if ( targetCategory.nodeIdentifier() == initialNode ) // loop of redirects.
			return null;
		if ( targetCategory.isRedirect() ) 
			return ((RedirectCategory)targetCategory).redirectsTo(initialNode, pageFactory);
		return targetCategory;
	}
	
	@Override
	public boolean isRedirect() {
		return true;
	}

}
