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
 * A Wikipedia article.
 *
 */
public class Article extends Page {
	

	/**
	 * The number of articles to which this article links.
	 */
	private int outdegree;
	
	/**
	 * The number of articles that link to this article.
	 */
	private int indegree;
	
	/**
	 * If this article represents a spatial entity, the globe where the entity is.
	 */
	private String globe;
	
	/**
	 * If this article represents a spatial entity, the latitude of the entity.
	 */
	private Double latitude;
	
	/**
	 * If this article represents a spatial entity, the longitude of the entity.
	 */
	private Double longitude;
	
	/**
	 * If this article represents a spatial entity, the type of the entity.
	 */
	private String type; 
	
	/**
	 * Creates a new article.
	 * @param driver The driver used to connect to the underlying Neo4j database.
	 * @param nodeIdentifier The identifier in the underlying Neo4j database of the node
	 * corresponding to the new article.
	 */
	protected Article(Driver driver, long nodeIdentifier) {
		super(driver, nodeIdentifier);
		this.outdegree = -1;
		this.indegree = -1;
		this.globe = null;
		this.latitude = null;
		this.longitude = null;
		this.type = null;
	}
	
	/**
	 * Loads the attributes of this article from the underlying Neo4j database.
	 */
	private void loadAttributesFromNeo4j() {
		if (this.loadAttributes()) {
			Session session = driver().session();
			StatementResult result = session.run("MATCH (n:Article) WHERE id(n)={node-identifier} return n.title, n.lang, n.wikiid, n.parents, n.outdegree, "
					+ "n.indegree, n.globe, n.latitude, n.longitude, n.type", 
					Values.parameters("node-identifier", this.nodeIdentifier()));
			for ( Record record : result.list() ) {
				title(record.get("n.title").asString());
				language(record.get("n.lang").asString());
				wikiid(record.get("n.wikiid").asString());
				parents(record.get("n.parents").asInt());
				outdegree(record.get("n.outdegree").asInt());
				indegree(record.get("n.indegree").asInt());
				if ( !record.get("n.globe").isNull() )
					globe(record.get("n.globe").asString());
				if ( !record.get("n.latitude").isNull() )
					latitude(record.get("n.latitude").asDouble());
				if ( !record.get("n.longitude").isNull() )
					latitude(record.get("n.longitude").asDouble());
				if ( !record.get("n.type").isNull() )
					type(record.get("n.type").asString());
			}
			
			session.close();
			this.loadAttributes(false);
		}
	}
	
	@Override
	public String title() {
		loadAttributesFromNeo4j();
		return super.title();
	}
	
	@Override
	public String language() {
		loadAttributesFromNeo4j();
		return super.language();
	}
	
	
	@Override
	public String wikiid() {
		loadAttributesFromNeo4j();
		return super.wikiid();
	}
	
	
	@Override
	public int parents() {
		loadAttributesFromNeo4j();
		return super.parents();
	}
	
	/**
	 * Returns the number of articles to which this article links.
	 * @return The number of articles to which this article links.
	 */
	public int outdegree() {
		loadAttributesFromNeo4j();
		return this.outdegree;
	}
	
	
	/**
	 * Sets the number of articles to which this article links.
	 * @param outdegree The number of articles to which this article links.
	 */
	public void outdegree(int outdegree) {
		this.outdegree = outdegree;
	}
	
	/**
	 * Returns the number of articles that link to this article.
	 * @return The number of articles that link to this article.
	 */
	public int indegree() {
		loadAttributesFromNeo4j();
		return this.indegree;
	}
	
	/**
	 * Sets the number of articles that link to this article.
	 * @param indegree The number of articles that link to this article.
	 */
	public void indegree(int indegree) {
		loadAttributesFromNeo4j();
		this.indegree = indegree;
	}
	
	/**
	 * Returns the globe of the spatial entity described by this article, if any.
	 * @return The globe of the spatial entity described by this article, if any, {@code null} otherwise.
	 */
	public String globe() {
		loadAttributesFromNeo4j();
		return globe;
	}
	
	/**
	 * Sets the globe of the spatial entity described by this article, if any.
	 * @param globe The globe of the spatial entity described by this article, if any.
	 */
	public void globe(String globe) {
		this.globe = globe;
	}
	
	/**
	 * Returns the latitude of the spatial entity described by this article, if any.
	 * @return The latitude of the spatial entity described by this article, if any, {@code null} otherwise.
	 */
	public Double latitude() {
		loadAttributesFromNeo4j();
		return this.latitude;
	}
	
	/**
	 * Sets the latitude of the spatial entity described by this article, if any.
	 * @param latitude The latitude of the spatial entity described by this article, if any.
	 */
	public void latitude(double latitude) {
		this.latitude = latitude;
	}
	
	/**
	 * Returns the longitude of the spatial entity described by this article, if any.
	 * @return The longitude of the spatial entity described by this article, if any, {@code null} otherwise.
	 */
	public Double longitude() {
		loadAttributesFromNeo4j();
		return this.longitude;
	}
	
	/**
	 * Sets the longitude of the spatial entity described by this article, if any.
	 * @param longitude The longitude of the spatial entity described by this article, if any.
	 */
	public void longitude(double longitude) {
		this.longitude = longitude;
	}
	
	/**
	 * Returns the type of the spatial entity described by this article, if any.
	 * @return The type of the spatial entity described by this article, if any, {@code null} otherwise.
	 */
	public String type() {
		loadAttributesFromNeo4j();
		return this.type;
	}
	
	/**
	 * Sets the type of the spatial entity described by this article, if any.
	 * @param type The type of the spatial entity described by this article, if any.
	 */
	public void type(String type) {
		this.type = type;
	}
	
	/**
	 * Returns whether this article describes a spatial entity.
	 * @return {@code true} if this article describes a spatial entity, {@code false} otherwise. 
	 */
	public boolean describesSpatialEntity() {
		loadAttributesFromNeo4j();
		return latitude != null;
	}
	
	
	/**
	 * Returns the set of articles to which  this article links.
	 * @return The set of articles to which  this article links.
	 */
	public Set<Article> linksTo() {
		PageFactory factory = new PageFactory(driver());
		return factory.createArticles("MATCH (n:Article)-[:link]->(m:Article) WHERE id(n)={id-node} return m as target-node", 
				Values.parameters("id-node", this.nodeIdentifier()), "target-node");
	}
	
	/**
	 * Returns the set of articles to which  this article links such that the first occurrence 
	 * of the link in the text of this article is within the given maximum offset.
	 * @param maxOffset The maximum offset
	 * @return The set of articles to which  this article links such that the first occurrence 
	 * of the link in the text of this article is within the given maximum offset.
	 */
	public Set<Article> linksToOffset(int maxOffset) {
		PageFactory factory = new PageFactory(driver());
		return factory.createArticles("MATCH (n:Article)-[l:link]->(m:Article) WHERE id(n)={id-node} "
				+ "AND l.offset<={max-offset} return m as target-node", 
				Values.parameters("id-node", this.nodeIdentifier(), "max-offset", maxOffset), "target-node");
	}
	
	/**
	 * Returns the set of articles to which this article links such that the first occurrence of the link 
	 * is within  the maximum rank specified in the text of this article.
	 * @param maxRank The maximum rank.
	 * @return The set of articles to which this article links such that the first occurrence of the link 
	 * is within  the maximum rank specified in the text of this article.
	 */
	public Set<Article> linksToRank(int maxRank) {
		PageFactory factory = new PageFactory(driver());
		return factory.createArticles("MATCH (n:Article)-[l:link]->(m:Article) WHERE id(n)={id-node} "
				+ "AND l.rank<={max-rank} return m as target-node", 
				Values.parameters("id-node", this.nodeIdentifier(), "max-rank", maxRank), "target-node");
	}
	
	/**
	 * Returns the set of articles to which this article links such that the first occurrence of the link
	 * occurs in the introduction of this article.
	 * @return Returns the set of articles to which this article links such that the first occurrence of the link
	 * occurs in the introduction of this article.
	 */
	public Set<Article> linksToIntro() {
		PageFactory factory = new PageFactory(driver());
		return factory.createArticles("MATCH (n:Article)-[l:link]->(m:Article) WHERE id(n)={id-node} "
				+ "AND exists(l.intro) return m as target-node", 
				Values.parameters("id-node", this.nodeIdentifier()), "target-node");
	}
	
	/**
	 * Returns the set of articles to which this article links such that the first occurrence of the link
	 * occurs in the infobox of this article.
	 * @return Returns the set of articles to which this article links such that the first occurrence of the link
	 * occurs in the infobox of this article.
	 */
	public Set<Article> linksToInfobox() {
		PageFactory factory = new PageFactory(driver());
		return factory.createArticles("MATCH (n:Article)-[l:link]->(m:Article) WHERE id(n)={id-node} "
				+ "AND exists(l.infobox) return m as target-node", 
				Values.parameters("id-node", this.nodeIdentifier()), "target-node");
	}
	
	/**
	 * Returns the set of articles that link to this article.
	 * @return The set of articles that link to this article.
	 */
	public Set<Article> linksFrom() {
		PageFactory factory = new PageFactory(driver());
		return factory.createArticles("MATCH (n:Article)<-[:link]-(m:Article) WHERE id(n)={id-node} return m as target-node", 
				Values.parameters("id-node", this.nodeIdentifier()), "target-node");
	}
	
	/**
	 * Returns the set of articles that link to this article such that the first occurrence 
	 * of the link is within the given maximum offset.
	 * @param maxOffset The maximum offset
	 * @return The set of articles that link to this article such that the first occurrence 
	 * of the link is within the given maximum offset.
	 */
	public Set<Article> linksFromOffset(int maxOffset) {
		PageFactory factory = new PageFactory(driver());
		return factory.createArticles("MATCH (n:Article)<-[l:link]-(m:Article) WHERE id(n)={id-node} "
				+ "AND l.offset<={max-offset} return m as target-node", 
				Values.parameters("id-node", this.nodeIdentifier(), "max-offset", maxOffset), "target-node");
	}
	
	/**
	 * Returns the set of articles that link to this article such that the first occurrence of the link 
	 * is within  the maximum rank specified.
	 * @param maxRank The maximum rank.
	 * @return The set of articles that link to this article such that the first occurrence of the link 
	 * is within  the maximum rank specified.
	 */
	public Set<Article> linksFromRank(int maxRank) {
		PageFactory factory = new PageFactory(driver());
		return factory.createArticles("MATCH (n:Article)<-[l:link]-(m:Article) WHERE id(n)={id-node} "
				+ "AND l.rank<={max-rank} return m as target-node", 
				Values.parameters("id-node", this.nodeIdentifier(), "max-rank", maxRank), "target-node");
	}
	
	/**
	 * Returns the set of articles that link to this article such that the first occurrence of the link
	 * occurs in the introduction.
	 * @return Returns the set of articles that link to this article such that the first occurrence of the link
	 * occurs in the introduction.
	 */
	public Set<Article> linksFromIntro() {
		PageFactory factory = new PageFactory(driver());
		return factory.createArticles("MATCH (n:Article)<-[l:link]-(m:Article) WHERE id(n)={id-node} "
				+ "AND exists(l.intro) return m as target-node", 
				Values.parameters("id-node", this.nodeIdentifier()), "target-node");
	}
	
	/**
	 * Returns the set of articles that link to this article such that the first occurrence of the link
	 * occurs in the infobox of this article.
	 * @return Returns the set of articles that link to this article such that the first occurrence of the link
	 * occurs in the infobox.
	 */
	public Set<Article> linksFromInfobox() {
		PageFactory factory = new PageFactory(driver());
		return factory.createArticles("MATCH (n:Article)<-[l:link]-(m:Article) WHERE id(n)={id-node} "
				+ "AND exists(l.infobox) return m as target-node", 
				Values.parameters("id-node", this.nodeIdentifier()), "target-node");
	}
	
	/**
	 * Returns the categories that contain this article.
	 * @return The categories that contain this article.
	 */
	public Set<Category> parentCategories() {
		PageFactory factory = new PageFactory(driver());
		return factory.createCategories("MATCH (n:Article)-[:belongTo]->(m:Category) WHERE id(n)={id-node} "
				+ "return m as target-node", 
				Values.parameters("id-node", this.nodeIdentifier()), "target-node");
	}
	
	/**
	 * Returns the set of articles that are linked through a cross-link to this article.
	 * @return The set of articles that are linked through a cross-link to this article.
	 */
	public Set<Article> crossLinkedArticles() {
		PageFactory factory = new PageFactory(driver());
		return factory.createArticles("MATCH (n:Article)-[:crosslink]->(m:Article) WHERE id(n)={id-node} "
				+ "return m as target-node", 
				Values.parameters("id-node", this.nodeIdentifier()), "target-node");
	}
	
	/**
	 * Returns the set of articles in the specified language that are linked through a cross-link to this article.
	 * @param language The code of the language of the target articles.
	 * @return The set of articles in the specified language that are linked through a cross-link to this article.
	 */
	public Set<Article> crossLinkedArticles(String language) {
		PageFactory factory = new PageFactory(driver());
		return factory.createArticles("MATCH (n:Article)-[:crosslink]->(m:Article) WHERE id(n)={id-node} "
				+ "AND m.language={lang} return m as target-node", 
				Values.parameters("id-node", this.nodeIdentifier(), "lang", language), "target-node");
	}
	
	/**
	 * Returns the set of articles in the specified languages that are linked through a cross-link to 
	 * this article.
	 * @param languages The codes of the languages of the target articles.
	 * @return The set of articles in the specified languages that are linked through a cross-link to this article.
	 */
	public Set<Article> crossLinkedArticles(String[] languages) {
		//TODO
		return null;
	}

}
