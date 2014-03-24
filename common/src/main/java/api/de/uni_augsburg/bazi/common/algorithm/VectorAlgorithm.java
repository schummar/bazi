package de.uni_augsburg.bazi.common.algorithm;

/** An algorithm whose output is narrowed down to a subtype of {@link de.uni_augsburg.bazi.common.algorithm.VectorOutput} */
public interface VectorAlgorithm<O extends VectorOutput> extends Algorithm<O>
{}
