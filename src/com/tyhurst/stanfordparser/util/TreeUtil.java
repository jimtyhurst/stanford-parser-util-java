package com.tyhurst.stanfordparser.util;

import java.util.List;

import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeFunctions;
import edu.stanford.nlp.trees.TypedDependency;

public class TreeUtil {

	private GrammaticalStructureFactory grammaticalStructureFactory;
	
	public TreeUtil() {
	}
	
	public TreeUtil(GrammaticalStructureFactory grammaticalStructureFactory) {
		this.grammaticalStructureFactory = grammaticalStructureFactory;
	}

	public List<TypedDependency> getTypedDependencies(Tree tree) {
		return getTypedDependencies(tree, getGrammaticalStructureFactory());
	}

	public List<TypedDependency> getTypedDependencies(Tree tree, GrammaticalStructureFactory grammaticalStructureFactory) {
		if (grammaticalStructureFactory == null) {
			throw new IllegalArgumentException("grammaticalStructureFactory cannot be null.");
		}
		Tree stringLabeledTree = TreeFunctions.getLabeledTreeToStringLabeledTreeFunction().apply(tree);
		GrammaticalStructure surfaceStructure = grammaticalStructureFactory.newGrammaticalStructure(stringLabeledTree);
		List<TypedDependency> dependencies = surfaceStructure.typedDependenciesCCprocessed(true);
		return dependencies;
	}
	
	public Tree treeToStringLabeledTree(Tree tree) {
		Tree stringLabeledTree = TreeFunctions.getLabeledTreeToStringLabeledTreeFunction().apply(tree);
		return stringLabeledTree;
	}
	
	public GrammaticalStructureFactory getGrammaticalStructureFactory() {
		return grammaticalStructureFactory;
	}
	
	public void setGrammaticalStructureFactory(GrammaticalStructureFactory grammaticalStructureFactory) {
		this.grammaticalStructureFactory = grammaticalStructureFactory;
	}
	
}
