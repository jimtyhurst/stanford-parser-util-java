package com.tyhurst.stanfordparser.util;

import edu.stanford.nlp.trees.Tree;

public interface SimpleParser {

	Tree getBestParse(String sentence);
	
}
