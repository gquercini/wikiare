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
 * This class represents a page that redirects to an article.
 *
 */
public class RedirectArticle extends Article {

	/**
	 * Creates a new redirect article.
	 * @param driver The driver used to connect to the underlying Neo4j database.
	 * @param nodeIdentifier The identifier in the underlying Neo4j database of the node
	 * corresponding to the new article.
	 */
	protected RedirectArticle(Driver driver, long nodeIdentifier) {
		super(driver, nodeIdentifier);
	}
	
	/**
	 * Returns the target article of this redirection.
	 * The result is never a redirect article. 
	 * If a loop of redirects is detected, this method returns {@code null}.
	 * @return The target article of this redirection, or {@code null} if no target article can be determined. 
	 */
	public Article redirectsTo() {
		PageFactory pageFactory = new PageFactory(driver());
		return redirectsTo(nodeIdentifier(), pageFactory);
	}
	
	/**
	 * Auxiliary function of {@code redirectsTo}.
	 * It is invoked recursively on a redirect article until either a non-redirect article is found or a loop of redirects
	 * is detected.
	 * @param initialNode The identifier of the node in the underlying Neo4j database that corresponds to the 
	 * redirect article from which the recursion starts.
	 * @param pageFactory The object used to create articles from Neo4j nodes. 
	 * @return The article that is the target of a redirection, if any, or {@code null} if no target can be found.
	 */
	private Article redirectsTo(long initialNode, PageFactory pageFactory) {
		Article targetArticle = null;
		Session session = driver().session();
		StatementResult result = session.run("MATCH (n:Redirect)-[:redirectTo]->(m:Article) WHERE id(n)={node-identifier} return m as target-node", 
				Values.parameters("node-identifier", this.nodeIdentifier()));
		Record record = result.single();
		Node targetNode = record.get("target-node").asNode();
		targetArticle = pageFactory.createArticle(targetNode);
		session.close();
		if ( targetArticle.nodeIdentifier() == initialNode ) // loop of redirects.
			return null;
		if ( targetArticle.isRedirect() ) 
			return ((RedirectArticle)targetArticle).redirectsTo(initialNode, pageFactory);
		return targetArticle;
	}
	
	@Override
	public boolean isRedirect() {
		return true;
	}

}
