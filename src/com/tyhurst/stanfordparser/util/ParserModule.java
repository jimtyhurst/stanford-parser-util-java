package com.tyhurst.stanfordparser.util;

/**
 * Builds utility objects for accessing the Stanford Parser with default configuration values.
 */

public interface ParserModule {
	
	SimpleParser buildParser();
	
	TreeUtil buildTreeUtil();
	
}
