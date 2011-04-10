package com.tyhurst.stanfordparser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.tyhurst.stanfordparser.util.EnglishParserModule;
import com.tyhurst.stanfordparser.util.ParserModule;
import com.tyhurst.stanfordparser.util.SimpleParser;
import com.tyhurst.stanfordparser.util.TreeUtil;

import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TypedDependency;

/**
 * LexicalizedParserTest tests a SimpleParser for a few basic cases,
 * where a SimpleParser is a simplified facade to the LexicalizedParser class.
 */
public class LexicalizedParserTest {
	
	private static final String EXPECTED_LABEL_JJ = "JJ";
	private static final String EXPECTED_LABEL_NP = "NP";
	private static final String EXPECTED_LABEL_ROOT = "ROOT";
	private static final String EXPECTED_LABEL_S = "S";
	private static final String EXPECTED_LABEL_VBP = "VBP";
	private static final String EXPECTED_LABEL_VP = "VP";
	
	private static ParserModule parserModule;
	private static SimpleParser parser;
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
		Tree tree = getParser().getBestParse("every male student read some book");

		assertEquals(EXPECTED_LABEL_ROOT, tree.label().value());
		Tree actualSNode = tree.firstChild();
		assertEquals(EXPECTED_LABEL_S, actualSNode.label().value());
		Tree actualNPNode = actualSNode.getChild(0);
		assertEquals(EXPECTED_LABEL_NP, actualNPNode.label().value());
		Tree actualVPNode = actualSNode.getChild(1);		
		assertEquals(EXPECTED_LABEL_VP, actualVPNode.label().value());
		Tree actualVNode = actualVPNode.getChild(0);		
		assertEquals(EXPECTED_LABEL_VBP, actualVNode.label().value());
	}
	
	/**
	 * Expect:
	 * tree = (ROOT (S (NP (DT some) (JJ female) (NN dog)) (VP (VBZ bites) (NP (DT every) (NN student)))))
	 * dependencies = [det(dog-3, some-1), amod(dog-3, female-2), nsubj(bites-4, dog-3), det(student-6, every-5), dobj(bites-4, student-6)]
	 */
	@Test public void testSimpleSVODependencies() {
		Tree tree = getParser().getBestParse("some female dog bites every student");
		assertEquals(EXPECTED_LABEL_ROOT, tree.label().value());
		Tree actualSNode = tree.firstChild();
		assertEquals(EXPECTED_LABEL_S, actualSNode.label().value());
		Tree actualNPNode = actualSNode.getChild(0);
		assertEquals(EXPECTED_LABEL_NP, actualNPNode.label().value());
		Tree actualJJNode = actualNPNode.getChild(1);
		assertEquals(EXPECTED_LABEL_JJ, actualJJNode.label().value());
		Tree actualVPNode = actualSNode.getChild(1);		
		assertEquals(EXPECTED_LABEL_VP, actualVPNode.label().value());

		List<TypedDependency> dependencies = getTreeUtil().getTypedDependencies(tree);
		assertTrue(dependencies.size() == 5);
		TypedDependency detDependency = dependencies.get(0);
		assertEquals("det", detDependency.reln().getShortName());
		assertEquals("some-1", detDependency.dep().toString());
		assertEquals("dog-3", detDependency.gov().toString());
		TypedDependency dobjDependency = dependencies.get(dependencies.size() - 1);
		assertEquals("dobj", dobjDependency.reln().getShortName());
		assertEquals("student-6", dobjDependency.dep().toString());
		assertEquals("bites-4", dobjDependency.gov().toString());
	}
	
	/**
	 * Expect:
	 * tree = (ROOT (S (NP (DT every) (JJ male) (NN student)) (VP (VP (VBD read) (NP (DT some) (NN book))) (CC and) (VP (VBD kissed) (NP (DT a) (NN girl))))))
	 * dependencies = [det(student-3, every-1), amod(student-3, male-2), nsubj(read-4, student-3), nsubj(kissed-8, student-3), det(book-6, some-5), dobj(read-4, book-6), conj_and(read-4, kissed-8), det(girl-10, a-9), dobj(kissed-8, girl-10)]
	 */
	@Test public void testSVODependenciesWithConjunction() {
		Tree tree = getParser().getBestParse("every male student read some book and kissed a girl");
		assertEquals(EXPECTED_LABEL_ROOT, tree.label().value());
		Tree actualSNode = tree.firstChild();
		assertEquals(EXPECTED_LABEL_S, actualSNode.label().value());
		Tree actualNPNode = actualSNode.getChild(0);
		assertEquals(EXPECTED_LABEL_NP, actualNPNode.label().value());
		Tree actualJJNode = actualNPNode.getChild(1);
		assertEquals(EXPECTED_LABEL_JJ, actualJJNode.label().value());
		Tree actualVPNode = actualSNode.getChild(1);		
		assertEquals(EXPECTED_LABEL_VP, actualVPNode.label().value());

		List<TypedDependency> dependencies = getTreeUtil().getTypedDependencies(tree);
		assertTrue(dependencies.size() == 9);
		TypedDependency detDependency = dependencies.get(0);
		assertEquals("det", detDependency.reln().getShortName());
		assertEquals("every-1", detDependency.dep().toString());
		assertEquals("student-3", detDependency.gov().toString());
		TypedDependency dobjDependency = dependencies.get(dependencies.size() - 1);
		assertEquals("dobj", dobjDependency.reln().getShortName());
		assertEquals("girl-10", dobjDependency.dep().toString());
		assertEquals("kissed-8", dobjDependency.gov().toString());
	}
	
	private SimpleParser getParser() {
		if (parser == null) {
			parser = getParserModule().buildParser();
		}
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
