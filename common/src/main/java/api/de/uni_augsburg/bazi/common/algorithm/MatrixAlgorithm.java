package de.uni_augsburg.bazi.common.algorithm;

/** An algorithm whose output is narrowed down to a subtype of {@link de.uni_augsburg.bazi.common.algorithm.MatrixOutput} */
public interface MatrixAlgorithm<O extends MatrixOutput> extends Algorithm<O>
{}
