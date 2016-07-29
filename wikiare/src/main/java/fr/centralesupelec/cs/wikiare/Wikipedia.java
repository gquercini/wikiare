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
package fr.centralesupelec.cs.wikiare;



import java.util.ArrayList;
import java.util.List;

import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Values;
import org.neo4j.driver.v1.types.Node;

import fr.centralesupelec.cs.wikiare.wikipedia.Article;
import fr.centralesupelec.cs.wikiare.wikipedia.PageFactory;

/**
 * The Wikipedia graph. 
 *
 */
public class Wikipedia {
	
	/**
	 * The driver used to connect to the underlying Neo4j database.
	 */
	private Driver driver;
	
	/**
	 * Creates a new instance of Wikipedia.
	 */
	public Wikipedia() {
		
	}
	
	/**
	 * Connects to the Wikipedia. The underlying Neo4j database is assumed to be in the local host.
	 */
	public void connect() {
		connect("localhost");
	}
	
	/**
	 * Connects to the Wikipedia. The underlying Neo4j database runs in a remote host.
	 * @param host The name or IP address of the remote host where the Neo4j database runs.
	 */
	public void connect(String host) {
		this.driver = GraphDatabase.driver( "bolt://" + host );
	}
	
	
	/**
	 * Closes the connection to the Neo4j database hosting the Wikikpedia.
	 */
	public void disconnect() {
		this.driver.close();
	}
	
	/**
	 * Returns the article with the given title in the given language edition of this Wikipedia.
	 * @param title The title of an article.
	 * @param language The language of the Wikipedia edition where the article is searched.
	 * @return The article with the given {@code title} in the given language edition, if any; {@code null}
	 * otherwise.
	 */
	public Article getArticle(String title, String language) {
		Session session = driver.session();
		PageFactory factory = new PageFactory(driver);
		StatementResult result = session.run("MATCH (n:Article) WHERE n.title={title} AND n.lang={lang} "
				+ "return n as node", Values.parameters("title", title, "lang", language));
		Record record = result.single();
		Node node = record.get("node").asNode();
		System.out.println(node.id());
		Article article = factory.createArticle(node);
		session.close();
		return article;
	}
	
	/**
	 * Returns the article with the given identifier in the given language edition of this Wikipedia.
	 * @param identifier The identifier of an article (as assigned by Wikimedia).
	 * @param language The language of the Wikipedia edition where the article is searched.
	 * @return The article with the given {@code identifier} in the given language edition, if any; {@code null}
	 * otherwise.
	 */
	public Article getArticle(long identifier, String language) {
		//TODO
		return null;
	}
	
	/**
	 * Returns all the articles that have a given title.
	 * @param title A title.
	 * @return The list of the articles with the given title, if any; an empty list otherwise.
	 */
	public List<Article> getArticles(String title) {
		List<Article> articles = new ArrayList<Article>();
		//TODO
		return articles;
	}
	
	/**
	 * Returns all the articles that have a given identifier.
	 * @param identifier A title.
	 * @return The list of the articles with the given identifier, if any; an empty list otherwise.
	 */
	public List<Article> getArticles(long identifier) {
		List<Article> articles = new ArrayList<Article>();
		//TODO
		return articles;
	}

}
