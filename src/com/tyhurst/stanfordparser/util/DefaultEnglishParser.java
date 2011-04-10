package com.tyhurst.stanfordparser.util;

import java.io.StringReader;
import java.util.List;

import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.objectbank.TokenizerFactory;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.WordTokenFactory;
import edu.stanford.nlp.trees.Tree;

/**
 * DefaultEnglishParser is a facade for a LexicalizedParser,
 * providing very limited functionality, but it is easy to
 * use.
 */
public class DefaultEnglishParser implements SimpleParser {

	private LexicalizedParser parser;
	private TreeUtil treeUtil;
	private TokenizerFactory<Word> tokenizerFactory;
	
	public DefaultEnglishParser(LexicalizedParser parser, TreeUtil treeUtil) {
		this.parser = parser;
		this.treeUtil = treeUtil;
	}

	public Tree getBestParse(String sentence) {
		List<Word> tokens = tokenize(sentence);
		LexicalizedParser parser = getParser();
		parser.parse(tokens);
		Tree tree = parser.getBestParse();
		Tree stringLabeledTree = getTreeUtil().treeToStringLabeledTree(tree);
		return stringLabeledTree;
	}
	
	private List<Word> tokenize(String sentence) {
		return buildTokenizer(sentence).tokenize();
	}
	
	private Tokenizer<Word> buildTokenizer(String sentence) {
		return getTokenizerFactory().getTokenizer(new StringReader(sentence));
	}
	
	private LexicalizedParser getParser() {
		parser.reset();
		return parser;
	}
	
	private TreeUtil getTreeUtil() {
		return treeUtil;
	}
	
	private TokenizerFactory<Word> getTokenizerFactory() {
		if (tokenizerFactory == null) {
			tokenizerFactory =  PTBTokenizer.factory(false, new WordTokenFactory());
		}
		return tokenizerFactory;
	}
	
}
