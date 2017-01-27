package it.unipi.di.acubelab.wikipediarelatedness.wikipedia.webgraph.subgraph.topk

import it.unipi.di.acubelab.wikipediarelatedness.wikipedia.topk.TopKEmbeddingsCache
import it.unipi.di.acubelab.wikipediarelatedness.wikipedia.webgraph.graph.WikiBVGraph
import org.slf4j.LoggerFactory


class W2VSubWikiBVGraph(srcWikiID: Int, dstWikiID: Int, wikiGraph: WikiBVGraph, threshold: Int)
  extends TopKSubWikiBVGraph(srcWikiID, dstWikiID, wikiGraph, threshold) {


  override def getLogger() = LoggerFactory.getLogger(classOf[W2VSubWikiBVGraph])


  override def neighborhood(wikiID: Int) : Array[Int] = {
    val embeddings = TopKEmbeddingsCache.corpusSG.getTopK(wikiID).map(_._1)

    val noDisEmbeddings = embeddings.filter(wikiGraph.contains)

    logger.debug("Embedding with/without disambiguation %d vs %d".format(embeddings.length, noDisEmbeddings.length))

    noDisEmbeddings.toArray
  }

}