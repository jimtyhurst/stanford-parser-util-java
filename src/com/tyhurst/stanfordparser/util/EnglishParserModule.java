package com.tyhurst.stanfordparser.util;

import java.io.StringReader;

import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.objectbank.TokenizerFactory;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.WordTokenFactory;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.TreebankLanguagePack;


public class EnglishParserModule implements ParserModule {
	
	private static final String PARSER_CONFIGURATION_FILE = "./resources/englishPCFG.ser.gz";
	
	public LexicalizedParser buildParser() {
		return new LexicalizedParser(PARSER_CONFIGURATION_FILE);
	}
	
	public Tokenizer<Word> buildTokenizer(String sentence) {
		TokenizerFactory<Word> factory = PTBTokenizer.factory(false, new WordTokenFactory());
		return factory.getTokenizer(new StringReader(sentence));
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
