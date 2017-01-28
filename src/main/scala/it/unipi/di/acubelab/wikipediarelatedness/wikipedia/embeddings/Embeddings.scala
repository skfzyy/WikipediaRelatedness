package it.unipi.di.acubelab.wikipediarelatedness.wikipedia.embeddings

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap

/**
  * Manages differnt kind of embeddings, by only overriding loadEmbeddings function.
  *
  */
trait Embeddings {

  // mapping between a wikiID and its embedding
  protected val embeddings: Int2ObjectOpenHashMap[Seq[Tuple2[Int, Float]]] = loadEmbeddings()


  /**
    * Load entity embeddings from file and returns the corresponding hashmap.
    *
    * @return
    */
  protected def loadEmbeddings() : Int2ObjectOpenHashMap[Seq[Tuple2[Int, Float]]]


  /**
    * Retrieves the embedding of wikiID.
    *
    * @param wikiID
    * @return
    */
  protected def apply(wikiID: Int) = embeddings.get(wikiID)
}