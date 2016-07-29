package fr.centralesupelec.cs.wikiare;

import fr.centralesupelec.cs.wikiare.wikipedia.Article;

public class Test {

	public static void main(String[] args) {
		Wikipedia wikipedia = new Wikipedia();
		System.out.println("connecting to Wikipedia");
		wikipedia.connect();
		
		Article article = wikipedia.getArticle("Barack Obama", "en");
		System.out.println("Article found! " + article.title());
		
		System.out.println("diconnecting from Wikipedia");
		wikipedia.disconnect();

	}

}
