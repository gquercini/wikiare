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
 * A link between two pages of the same Wikipedia language edition.
 *
 */
public class InternalLink extends Link {

	/**
     * The rank of the first occurrence of this link in the source page.
     */
    private int rank;
    
    /**
     * Whether this link occurs in the infobox of the source Wikipedia page.
     */
    private boolean infobox;
    
    /**
     * Whether this link occurs in the introduction of the source Wikipedia page.
     */
    private boolean intro;
    
    /**
     * The number of occurrences of this link in the source Wikipedia page.
     */
    private int occurrences;
	
	/**
	 * Creates a new internal link.
	 * @param driver The driver used to connect to the underlying Neo4j database.
	 * @param source The source page.
	 * @param target The target page.
	 * @param rank The rank of the first occurrence of the link in the source page.
	 * @param infobox Whether the link occurs in the infobox of the source Wikipedia page.
	 * @param intro Whether the link occurs in the introduction of the source Wikipedia page.
	 * @param occurrences The number of occurrences of the link in the source Wikipedia page.
	 */
	InternalLink(Driver driver, Page source, Page target, int rank, boolean infobox,
			boolean intro, int occurrences) {
		super(driver, source, target);
		this.rank = rank;
		this.infobox = infobox;
		this.intro = intro;
		this.occurrences = occurrences;
	}
	
	/**
	 * Returns the rank of the first occurrence of this link in the source page.
	 * The first link in the source page has rank 1, the second has rank 2....
	 * @return The rank of the first occurrence  of this link in the source page.
	 */
	public int rank() {
		return this.rank;
	}
	
	/**
	 * Returns whether this link occurs in the infobox of the source Wikipedia page.
	 * @return {@code true} if this link occurs in the infobox of the source Wikipedia page, 
	 * {@code false} otherwise.
	 */
	public boolean infobox() {
		return this.infobox;
	}
	
	/**
	 * Returns whether this link occurs in the introduction of the source Wikipedia page.
	 * @return {@code true} if this link occurs in the introduction of the source Wikipedia page,
	 * {@code false} otherwise.
	 */
	public boolean intro() {
		return this.intro;
	}
	
	/**
	 * Returns the number of occurrences of this link in the source Wikipedia page.
	 * @return The number of occurrences of this link in the source Wikipedia page.
	 */
	public int occurrences() {
		return this.occurrences;
	}

}
