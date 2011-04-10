package com.tyhurst.stanfordparser.util;

import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.TreebankLanguagePack;

/**
 * Builds utility objects for accessing the Stanford Parser with default configuration values
 * for an English parser.
 */
public class EnglishParserModule implements ParserModule {
	
	private static final String PARSER_CONFIGURATION_FILE = "./resources/englishPCFG.ser.gz";
	
	public SimpleParser buildParser() {
		return new DefaultEnglishParser(new LexicalizedParser(PARSER_CONFIGURATION_FILE), buildTreeUtil());
	}
	
	public TreeUtil buildTreeUtil() {
		TreeUtil util = new TreeUtil(buildGrammaticalStructureFactory());
		return util;
	}
	
	private GrammaticalStructureFactory buildGrammaticalStructureFactory() {
		TreebankLanguagePack languagePack = new PennTreebankLanguagePack();
		return languagePack.grammaticalStructureFactory(languagePack.punctuationWordRejectFilter());
	}
	
}
