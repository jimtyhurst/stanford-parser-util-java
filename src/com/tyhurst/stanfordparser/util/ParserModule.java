package com.tyhurst.stanfordparser.util;

import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.Tokenizer;

public interface ParserModule {
	
	LexicalizedParser buildParser();
	
	Tokenizer<Word> buildTokenizer(String sentence);

	TreeUtil buildTreeUtil();
	
}
