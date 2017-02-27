package it.unipi.di.acubelab.wikipediarelatedness.utils

import java.io.{File, FileInputStream}
import java.util.concurrent.TimeUnit
import java.util.zip.GZIPInputStream

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import it.unimi.dsi.fastutil.io.BinIO
import it.unimi.dsi.logging.ProgressLogger
import it.unipi.di.acubelab.wikipediarelatedness.dataset.DatasetFactory
import org.slf4j.LoggerFactory

import scala.io.Source
import scala.util.parsing.json.JSON


object WikipediaCorpus {

  protected val logger = LoggerFactory.getLogger("WikipediaCorpus")


  /**
    * Process Wikipedia Corpus and keep only texts of documents present in the dataset.
    */
  def filterWikipediaDocuments() = {

    val wikiID2text = new Int2ObjectOpenHashMap[Seq[String]]()

    // Get unique Wikipedia IDs from datasets
    val tasks = DatasetFactory.datasets().flatten.toList
    val wikiIDs = tasks.foldLeft(List.empty[Int])((IDs, task) => IDs ++ List(task.src.wikiID, task.dst.wikiID)).distinct


    //
    // Creation of wikiID -> Seq[String] Open Hash Map

    val pl = new ProgressLogger(logger, 1, TimeUnit.MINUTES)
    pl.start("Parsing Wikipedia Corpus...")

    val reader = Source.fromInputStream(
      new GZIPInputStream(
        new FileInputStream(
          new File(Config.getString("wikipedia.corpus")))
        )
    )

    val iter = reader.getLines()
    while(iter.hasNext && wikiID2text.size() != wikiIDs.size) {

      val wikiDoc = iter.next()
      val wikiJson = JSON.parseFull(wikiDoc)

      wikiJson match {
        case Some(jsonMap: Map[String, Any]@unchecked) =>
          val wikiID = jsonMap("wikiId").asInstanceOf[Double].toInt

          if (wikiIDs.contains(wikiID)) {
            val wikiSents = jsonMap("sentences").asInstanceOf[Seq[String]]
            wikiID2text.put(wikiID, wikiSents)

            logger.info("Added WikiDoc %d".format(wikiID))
          }

        case _ => logger.error("Error parsing Wikipedia Document %s".format(wikiJson.toString))
      }

      pl.update()
    }
    pl.done()

    /*
    reader.getLines().foreach {
      case wikiDoc: String =>

          val wikiJson = JSON.parseFull(wikiDoc)
          wikiJson match {
            case Some(jsonMap: Map[String, Any]@unchecked) =>

              val wikiID = jsonMap("wikiId").asInstanceOf[Double].toInt

              if (wikiIDs.contains(wikiID)) {
                val wikiSents = jsonMap("sentences").asInstanceOf[Seq[String]] // vedere come parsare lista
                wikiID2text.put(wikiID, wikiSents)
              }

            case _ => logger.error("Error parsing Wikipedia Document %s".format(wikiJson.toString))
          }

        pl.update()
    }
    pl.done()
    */

    // Storing
    logger.info("Storing Dataset Corpus as OpenHasMap...")
    BinIO.storeObject(wikiID2text, Config.getString("wikipedia.dataset_corpus"))
    logger.info("Dataset Corpus stored.")
  }


  def main(args: Array[String]) {
    this.filterWikipediaDocuments()
  }

}



/*
*
* JSON.parseFull(strResponse)

    jsonResponse match {
      case Some(jsonMap: Map[String, Any] @unchecked) =>

        jsonMap("title") match {
          case jsonTitle: Map[String, Any] @unchecked =>

            if (jsonTitle.contains("redirect_title")) {
              jsonTitle("redirect_title") match {
                case jsonWiki: Map[String, Any] @unchecked  =>

                  val redirectedTitle = jsonWiki("wiki_title").asInstanceOf[String]
                  val redirectedWikiID = jsonWiki("wiki_id").asInstanceOf[Double].toInt

                  logger.warn("Redirected %s with %s.".format(title, redirectedTitle))
                  return (redirectedTitle, redirectedWikiID)

                case _ => ;
              }
            }

            jsonTitle("title") match {
              case jsonWiki: Map[String, Any] @unc
*
*
* */
