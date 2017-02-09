package it.unipi.di.acubelab.wikipediarelatedness.wikipedia.topk

import it.unipi.di.acubelab.wikipediarelatedness.utils.Config
import it.unipi.di.acubelab.wikipediarelatedness.wikipedia.topk.neighbors.MilneWittenTopK
import it.unipi.di.acubelab.wikipediarelatedness.wikipedia.topk.neural.W2VTopK


object TopKFactory {

  def make(name: String) = name match {

    //
    // ESA
    case "esa" => new ESATopK
    case "esaentity" => new ESAEntityTopK

    //
    // Neural
    case "sg" => new W2VTopK(
      Config.getString("wikipedia.topk.neural.w2v.sg"),
      Config.getString("wikipedia.topk.cache.neural.sg.entity2entities")
    )

    case "dwsg" => new W2VTopK(
      Config.getString("wikipedia.topk.neural.deepwalk.dwsg"),
      Config.getString("wikipedia.topk.cache.neural.dwsg.entity2entities")
    )


    //
    // MilneWitten
    case "mw.in" => new MilneWittenTopK("in")
    case "mw.out" => new MilneWittenTopK("out")
    case "mw.sym" => new MilneWittenTopK("sym")
  }

}
