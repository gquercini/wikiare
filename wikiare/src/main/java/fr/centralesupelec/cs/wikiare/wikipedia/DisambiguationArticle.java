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

import java.util.HashSet;
import java.util.Set;

import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Values;
import org.neo4j.driver.v1.types.Node;

/**
 * This class represents a disambiguation article.
 *
 */
public class DisambiguationArticle extends Article {

	/**
	 * Creates a new disambiguation article.
	 * 
	 * @param driver The driver used to connect to the underlying Neo4j database.
	 * @param nodeIdentifier The identifier in the underlying Neo4j database of the node
	 * corresponding to the new article.
	 */
	protected DisambiguationArticle(Driver driver, long nodeIdentifier) {
		super(driver, nodeIdentifier);
	}
	
	/**
	 * Returns all the interpretations of this disambiguation article.
	 * @return The interpretations of this disambiguation article.
	 */
	public Set<Article> interpretations() {
		PageFactory pageFactory = new PageFactory(driver());
		Set<Article> interpretations = new HashSet<Article>();
		Session session = driver().session();
		StatementResult result = session.run("MATCH (n:Disambiguation)-[l:link]->(m:Article) WHERE id(n)={node-identifier} "
				+ "AND exists(l.disambig) return m as interpretation", 
				Values.parameters("node-identifier", this.nodeIdentifier()));
		for (Record record : result.list()) {
			Node interpretationNode = record.get("interpretation").asNode();
			Article interpretationArticle = pageFactory.createArticle(interpretationNode);
			if ( !interpretationArticle.isRedirect() && !interpretationArticle.isDisambiguation() )
				interpretations.add(interpretationArticle);
			else
				if ( interpretationArticle.isRedirect() ) {
					interpretationArticle = ((RedirectArticle)interpretationArticle).redirectsTo();
					if ( interpretationArticle != null )
						interpretations.add(interpretationArticle);
				}
				else
					if ( interpretationArticle.isDisambiguation() ) 
						interpretations.addAll(((DisambiguationArticle)interpretationArticle).interpretations());
		}
		session.close();
		return interpretations;
	}
	
	@Override
	public boolean isDisambiguation() {
		return true;
	}
	
}
