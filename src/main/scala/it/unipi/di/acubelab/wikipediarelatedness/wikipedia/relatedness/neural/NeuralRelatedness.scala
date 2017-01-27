package it.unipi.di.acubelab.wikipediarelatedness.wikipedia.relatedness.neural

import it.unipi.di.acubelab.wat.dataset.embeddings.EmbeddingsDataset
import it.unipi.di.acubelab.wikipediarelatedness.wikipedia.relatedness.{Relatedness, RelatednessOptions}
import org.slf4j.LoggerFactory


/**
  *
  * @param options
  *                {
  *                   "model": corpus/deepWalk/deepCorpus/coOccurrence
  *                }
  */
class NeuralRelatedness(options: RelatednessOptions) extends Relatedness  {
    val logger = LoggerFactory.getLogger(classOf[NeuralRelatedness])
    val w2v = loadw2v(options.model)

    def loadw2v(modelName : String) : EmbeddingsDataset  = {
      logger.info("Loading w2v %s model...".format(modelName))
      //val w2vPath = OldConfiguration.wikipedia(modelName)


      null
      //EmbeddingsDataset.apply(new File(w2vPath))
    }

    def computeRelatedness(srcWikiID: Int, dstWikiID: Int) : Float = {
      val srcEntWikiID = "ent_" + srcWikiID
      val dstEntWikiID = "ent_" + dstWikiID

      val cosine = w2v.similarity(srcEntWikiID, dstEntWikiID)
      //if (cosine < 0) logger.warn("Negative cosine between %s.".format(wikiRelTask.wikiTitleString()))

      cosine
    }

    override def toString(): String = {
      "W2V-%s".format(options.model)
    }
}