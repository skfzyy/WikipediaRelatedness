package it.unipi.di.acubelab.wikipediarelatedness.utils

import java.io.FileWriter

import it.unimi.dsi.logging.ProgressLogger
import it.unipi.di.acubelab.wikipediarelatedness.wikipedia.graph.WikiGraphTSVReader
import it.unipi.di.acubelab.wikipediarelatedness.wikipedia.relatedness.WikiRelatedness
import org.slf4j.LoggerFactory


class Data2Rel2TSV(wikiRelatedness: WikiRelatedness) {

  private val logger = LoggerFactory.getLogger(classOf[Data2Rel2TSV])

  /**
    * Apply a relatedness method to a set of ids (reader) and write them with writer.
    *
    * @param reader
    * @param writer
    */
  def apply(reader: Traversable[(Int, Int)], writer: FileWriter) : Unit = {

    val pl = new ProgressLogger(logger)

    val wikiIDsRel =  reader.map(pair => {
      pl.update()
      (pair._1, pair._2, wikiRelatedness.relatedness(pair._1, pair._2))
    } )

    pl.done()

    wikiIDsRel.foreach( triple => writer.write( s"${triple._1}\t${triple._2}\t%1.5f\n".format(triple._3) ))
    writer.flush()

  }


  /**
    * Parallel application of th relatedness method to a set of ids.
    *
    * @param reader
    * @param writer
    */
  def parApply(reader: Traversable[(Int, Int)], writer: FileWriter) : Unit = {

    // read all pairs
    val wikiIDsPairs = reader.map(pair => pair).toList

    // parallel computation
    val pl = new ProgressLogger(logger)

    val wikiIDsRel =  wikiIDsPairs.map(pair => {
      pl.update()
      (pair._1, pair._2, wikiRelatedness.relatedness(pair._1, pair._2))
    } )

    pl.done()

    // writing
    wikiIDsRel.foreach( triple => writer.write( s"${triple._1}\t${triple._2}\t%1.5f\n".format(triple._3) ))
    writer.flush()

  }


  def apply(dataFilename: String, outFilename: String)  : Unit = {

    val reader = new WikiGraphTSVReader(dataFilename)
    val writer = new FileWriter(outFilename)

    logger.info(s"Applying ${wikiRelatedness.name()} on file $dataFilename and writing results in $outFilename")

    apply(reader, writer)
    writer.close()

  }
}
