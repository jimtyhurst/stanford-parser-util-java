package com.tyhurst.stanfordparser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.tyhurst.stanfordparser.util.EnglishParserModule;
import com.tyhurst.stanfordparser.util.ParserModule;
import com.tyhurst.stanfordparser.util.TreeUtil;

import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TypedDependency;

public class LexicalizedParserTest {
	
	private static final String EXPECTED_LABEL_ROOT = "ROOT";
	private static final String EXPECTED_LABEL_S = "S";
	private static final String EXPECTED_LABEL_NP = "NP";
	private static final String EXPECTED_LABEL_VP = "VP";
	
	private static ParserModule parserModule;
	private static LexicalizedParser parser;
	private static TreeUtil treeUtil;
	
	@BeforeClass
	public static void setup() {
		parserModule = new EnglishParserModule();
	}
	
	/**
	 * Expect:
	 * tree = (ROOT (S (NP (DT every) (JJ male) (NN student)) (VP (VBP read) (NP (DT some) (NN book)))))
	 */
	@Test public void testSimpleSVO() {
		Tree tree = getBestParse("every male student read some book");

		assertEquals(EXPECTED_LABEL_ROOT, tree.label().value());
		Tree actualSNode = tree.firstChild();
		assertEquals(EXPECTED_LABEL_S, actualSNode.label().value());
		Tree actualNPNode = actualSNode.getChild(0);
		assertEquals(EXPECTED_LABEL_NP, actualNPNode.label().value());
		Tree actualVPNode = actualSNode.getChild(1);		
		assertEquals(EXPECTED_LABEL_VP, actualVPNode.label().value());
	}
	
	/**
	 * Expect:
	 * tree = (ROOT (S (NP (DT every) (NN student)) (VP (VBP read) (NP (DT some) (NN book)))))
	 * dependencies = [det(student-3, every-1), amod(student-3, male-2), nsubj(read-4, student-3), det(book-6, some-5), dobj(read-4, book-6)]
	 */
	@Test public void testSimpleSVODependencies() {
		Tree tree = getBestParse("every male student read some book");
		List<TypedDependency> dependencies = getTreeUtil().getTypedDependencies(tree);

		assertTrue(dependencies.size() == 5);
		TypedDependency detDependency = dependencies.get(0);
		assertEquals("det", detDependency.reln().getShortName());
		assertEquals("every-1", detDependency.dep().toString());
		assertEquals("student-3", detDependency.gov().toString());
	}
	
	/**
	 * Expect:
	 * tree = (ROOT (S (NP (DT every) (JJ male) (NN student)) (VP (VP (VBD read) (NP (DT some) (NN book))) (CC and) (VP (VBD kissed) (NP (DT a) (NN girl))))))
	 * dependencies = [det(student-3, every-1), amod(student-3, male-2), nsubj(read-4, student-3), nsubj(kissed-8, student-3), det(book-6, some-5), dobj(read-4, book-6), conj_and(read-4, kissed-8), det(girl-10, a-9), dobj(kissed-8, girl-10)]
	 */
	@Test public void testSVODependenciesWithConjunction() {
		Tree tree = getBestParse("every male student read some book and kissed a girl");
		List<TypedDependency> dependencies = getTreeUtil().getTypedDependencies(tree);

		assertTrue(dependencies.size() == 9);
		TypedDependency detDependency = dependencies.get(0);
		assertEquals("det", detDependency.reln().getShortName());
		assertEquals("every-1", detDependency.dep().toString());
		assertEquals("student-3", detDependency.gov().toString());
	}
	
	private Tree getBestParse(String sentence) {
		List<Word> tokens = tokenize(sentence);
		LexicalizedParser parser = getParser();
		parser.parse(tokens);
		Tree tree = parser.getBestParse();
		Tree stringLabeledTree = getTreeUtil().treeToStringLabeledTree(tree);
		return stringLabeledTree;
	}
	
	private List<Word> tokenize(String sentence) {
		return getParserModule().buildTokenizer(sentence).tokenize();
	}
	
	private LexicalizedParser getParser() {
		if (parser == null) {
			parser = getParserModule().buildParser();
		}
		parser.reset();
		return parser;
	}
	
	private TreeUtil getTreeUtil() {
		if (treeUtil == null) {
			treeUtil = getParserModule().buildTreeUtil();
		}
		return treeUtil;
	}
	
	private static ParserModule getParserModule() {
		if (parserModule == null) {
			parserModule = new EnglishParserModule();
		}
		return parserModule;
	}
	
}
