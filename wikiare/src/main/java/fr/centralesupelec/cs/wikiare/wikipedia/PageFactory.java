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
import org.neo4j.driver.v1.Value;
import org.neo4j.driver.v1.types.Node;

/**
 * This class is used to create a page given the corresponding
 * node in the underlying Neo4j database. 
 *
 */
public class PageFactory {

	/**
	 * The driver used to connect to the underlying Neo4j database.
	 */
	private Driver driver;

	/**
	 * Creates a new {@code PageFactory}.
	 * @param driver The driver used to connect to the underlying Neo4j database.
	 */
	public PageFactory(Driver driver) {
		this.driver = driver;
	}

	/**
	 * Creates the article corresponding to a given node in the underlying Neo4j database.
	 * @param node A node in the underlying Neo4j database.
	 * @return The article corresponding to a given node in the underlying Neo4j database, or {@code null}
	 * if the node does not correspond to any article.
	 */
	public Article createArticle(Node node) {
		if (node == null)
			return null;
		Article article = null;
		Session session = driver.session();
		if ( node.hasLabel("Article") ) {
			if ( node.hasLabel("Redirect") )
				article = new RedirectArticle(driver, node.id());
			else if ( node.hasLabel("Disambiguation") )
				article = new DisambiguationArticle(driver, node.id());
			else
				article = new Article(driver, node.id());
		}
		session.close();
		return article;
	}
	
	/**
	 * Returns a set of articles as a result of a query.
	 * @param query The query.
	 * @param parameters The parameters of the query, if any, {@code null} otherwise.
	 * @param targetArticleVariable The name of the variable in the query that indicates the target article that is returned by the query.
	 * @return The set of articles as the result of the query.
	 */
	public Set<Article> createArticles(String query, Value parameters, String targetArticleVariable) {
		Set<Article> targetArticles = new HashSet<Article>();
		Session session = driver.session();
		StatementResult result = parameters != null ? session.run(query, parameters) : session.run(query);
		for ( Record record : result.list() ) {
			Node targetNode = record.get(targetArticleVariable).asNode();
			Article targetArticle = createArticle(targetNode);
			if ( targetArticle.isRedirect() )
				targetArticle = ((RedirectArticle)targetArticle).redirectsTo();
			if ( targetArticle != null )
				targetArticles.add(targetArticle);
		}
		session.close();
		return targetArticles;
	}
	
	/**
	 * Returns a set of categories as a result of a query.
	 * @param query The query.
	 * @param parameters The parameters of the query, if any, {@code null} otherwise.
	 * @param targetCategoryVariable The name of the variable in the query that indicates the target category that is returned by the query.
	 * @return The set of categories as the result of the query.
	 */
	public Set<Category> createCategories(String query, Value parameters, String targetCategoryVariable) {
		Set<Category> targetCategories = new HashSet<Category>();
		Session session = driver.session();
		StatementResult result = parameters != null ? session.run(query, parameters) : session.run(query);
		for ( Record record : result.list() ) {
			Node targetNode = record.get(targetCategoryVariable).asNode();
			Category targetCategory = createCategory(targetNode);
			if ( targetCategory.isRedirect() )
				targetCategory = ((RedirectCategory)targetCategory).redirectsTo();
			if ( targetCategory != null )
				targetCategories.add(targetCategory);
		}
		session.close();
		return targetCategories;
	}
	
	/**
	 * Creates the category corresponding to a given node in the underlying Neo4j database.
	 * @param node A node in the underlying Neo4j database.
	 * @return The category corresponding to a given node in the underlying Neo4j database, or {@code null}
	 * if the node does not correspond to any category.
	 */
	public Category createCategory(Node node) {
		if ( node == null )
			return null;
		Category category = null;
		Session session = driver.session();
		if ( node.hasLabel("Category") ) {
			if ( node.hasLabel("Redirect") )
				category = new RedirectCategory(driver, node.id());
			else
				category = new Category(driver, node.id());
		}
		session.close();
		return category;
	}
	
	
}
